import com.sun.org.apache.xpath.internal.operations.Bool;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Intersection extends Thread {
    private List<TrafficLight> trafficLights;
    private TrafficLight ultimateHighestPriority;
    private boolean stop;

    public Intersection(){

        trafficLights = new ArrayList<>();
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
                        Integer.parseInt(eElement.getElementsByTagName("component_id").item(0).getTextContent()));
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
            TrafficLight highestPriorityLight = null;
            //Update all TrafficLights
            for(TrafficLight tl: trafficLights){
                tl.update();
            }
            if(ultimateHighestPriority != null){
                if(ultimateHighestPriority.isAvailable()){
                    ultimateHighestPriority.turnLightGreen();
                    ultimateHighestPriority = null;
                }
            }

            for(TrafficLight tl : trafficLights){
                if(tl.getPriorityTL() != 0){
                    if(highestPriorityLight == null){
                        highestPriorityLight = tl;
                    }
                    else{
                        int priority = tl.getPriorityTL();
                        if(priority > highestPriorityLight.getPriorityTL()){
                            highestPriorityLight = tl;
                        }
                    }

                }
            }
            if(highestPriorityLight != null){
                if(highestPriorityLight.getPriorityTL() >= 6){
                    if(ultimateHighestPriority == null){
                        highestPriorityLight.setHighestPriority();
                        ultimateHighestPriority = highestPriorityLight;
                    }
                }
            }

            if(highestPriorityLight != null){
                if(highestPriorityLight.isAvailable()){
                    highestPriorityLight.turnLightGreen();
                }
            }
            //Test if this is needed
            //Search Highest trafficLight Priority
            for(TrafficLight tl : trafficLights){
                if(tl.isAvailable() && tl.getPriorityTL() != 0){
                    if(highestPriorityLight == null){
                        highestPriorityLight = tl;
                    }
                    else{
                        int priority = tl.getPriorityTL();
                        if(priority > highestPriorityLight.getPriorityTL()){
                            highestPriorityLight = tl;
                        }
                    }
                }
            }


            if(highestPriorityLight != null){
                if(highestPriorityLight.isAvailable()){
                    highestPriorityLight.turnLightGreen();
                }
            }
            try {//Voor opbouw van de ram
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }//Evalueren priotiteiten dingetje
    }

    public void stopThread(){
        stop = true;
    }
}
