import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Intersection extends Thread {
    private List<TrafficLight> trafficLights;
    private List<Sensor> trafficJammedSensors;
    private boolean stop;
    private LocalDateTime waitingTime;

    public Intersection(){

        trafficLights = new ArrayList<>();
        trafficJammedSensors = new ArrayList<>();
        waitingTime = LocalDateTime.now();
        try {
            File inputFile = new File("src/main/java/XMLTrafficLight.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            initTrafficLights(doc.getElementsByTagName("TrafficLight"));
            initConflictingTrafficLights(doc.getElementsByTagName("Conflict"));
            iniGroupTrafficLights(doc.getElementsByTagName("GroupLight"));
            initTrafficJammedSensor();
            System.out.println("Klaar met conflicterende kruispunten");
        }catch (Exception e) {
            e.printStackTrace();
        }
        stop = false;
    }

    private void initTrafficLights(NodeList nList){
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                TrafficLight tl = new TrafficLight(
                        eElement.getElementsByTagName("user_type").item(0).getTextContent(),
                        Integer.parseInt(eElement.getElementsByTagName("group_id").item(0).getTextContent()),
                        Integer.parseInt(eElement.getElementsByTagName("component_id").item(0).getTextContent()),
                        Integer.parseInt(eElement.getElementsByTagName("evacuationTime").item(0).getTextContent()));
                trafficLights.add(tl);
            }
        }
    }

    private void initConflictingTrafficLights(NodeList nList){
        for (int temp = 0; temp < nList.getLength(); temp++) {
            NodeList conflict = nList.item(temp).getChildNodes();

            NodeList trafficLight1 = conflict.item(1).getChildNodes();
            NodeList trafficLight2 = conflict.item(3).getChildNodes();
            String userType = trafficLight1.item(1).getTextContent();
            int groupID = Integer.parseInt(trafficLight1.item(3).getTextContent());
            int componentID = Integer.parseInt(trafficLight1.item(5).getTextContent());
            TrafficLight tl1 = searchTrafficLight(userType, groupID,componentID);
            userType = trafficLight2.item(1).getTextContent();
            groupID = Integer.parseInt(trafficLight2.item(3).getTextContent());
            componentID = Integer.parseInt(trafficLight2.item(5).getTextContent());
            TrafficLight tl2 = searchTrafficLight(userType, groupID,componentID);
            tl1.addConflictingTrafficLight(tl2);
            tl2.addConflictingTrafficLight(tl1);
        }
    }

    private void initTrafficJammedSensor(){
        RoadJammedSensor trafficSensor = new RoadJammedSensor("motor_vehicle", 14);
        for (TrafficLight tl: trafficLights) {
            if(tl.groupID == 3 ||tl.groupID == 7 ||tl.groupID == 10){
                trafficSensor.addConflictingTrafficLight(tl);
            }
        }
        trafficJammedSensors.add(trafficSensor);
    }

    private TrafficLight searchTrafficLight(String userType, int groupID, int componentID){
        for (TrafficLight tl: trafficLights) {
            if(tl.getUserType().equals(userType)){
                if(tl.getGroupID() == groupID){
                    if(tl.getComponentID() == componentID)
                        return tl;
                }
            }
        }
        return null;
    }

    private void iniGroupTrafficLights(NodeList nList) {
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                int groupid = Integer.parseInt(eElement.getElementsByTagName("group_id").item(0).getTextContent());
                String usertype = eElement.getElementsByTagName("user_type").item(0).getTextContent();
                String componenttype = eElement.getElementsByTagName("component_type").item(0).getTextContent();
                List<TrafficLight> lights = new ArrayList<>();
                for (TrafficLight light : trafficLights) {
                    if (light.getGroupID() == groupid && light.getUserType().equals(usertype) && light.getComponentType().equals(componenttype)) {
                        lights.add(light);
                    }
                }
                //niet netjes, moet later verbeterd worden.
                lights.get(0).groupedWith(lights.get(1));
                lights.get(1).groupedWith(lights.get(0));
            }
        }
    }

    public void run(){
        while(!stop){
            List<TrafficLight> nextBatchLights = new ArrayList<>();
            boolean waitForNextGroup = false;
            //Update all TrafficLights
            for(TrafficLight tl: trafficLights){
                tl.update();
                if(tl.getStatus() > 0){
                    waitForNextGroup = true;
                }
            }

            if(!waitForNextGroup && LocalDateTime.now().isAfter(waitingTime)){
                boolean stopLoop = false;
                while(!stopLoop){
                    TrafficLight highestPriorityLight = null;

                    for(TrafficLight tl : trafficLights){
                        if(tl.getPriorityTL() != 0 && tl.isAvailable()){
                            if(highestPriorityLight == null){
                                highestPriorityLight = tl;
                            }
                            else{
                                if(tl.getPriorityTL() > highestPriorityLight.getPriorityTL()){
                                    highestPriorityLight = tl;
                                }
                            }
                        }
                    }

                    if(highestPriorityLight != null){
                        nextBatchLights.add(highestPriorityLight);
                        highestPriorityLight.markForNextGroup();
                    }
                    else{
                        stopLoop = true;
                    }
                }

                for(TrafficLight tl : nextBatchLights){
                    tl.turnLightGreen();
                }
                checkEvacuationTime(nextBatchLights);
            }

            try {//Voor opbouw van de ram
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }//Evalueren priotiteiten dingetje
    }

    private void checkEvacuationTime(List<TrafficLight> nextBatch){
        waitingTime = LocalDateTime.now();
        for (TrafficLight tl: nextBatch) {
            if(waitingTime.isBefore(LocalDateTime.now().plusSeconds(tl.getTotalTime()))){
                waitingTime = LocalDateTime.now().plusSeconds(tl.getTotalTime());
            }
        }
    }

    public void stopThread(){
        stop = true;
    }
}
