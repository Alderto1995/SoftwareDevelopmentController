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
            System.out.println("Klaar met conficlterende kruispunen");
        }catch (Exception e) {
            e.printStackTrace();
        }

//        TrafficLight MV1 = new TrafficLight(1,"motor_vehicle");
//        TrafficLight MV2 = new TrafficLight(2,"motor_vehicle");
//        TrafficLight MV3 = new TrafficLight(3,"motor_vehicle");
//        TrafficLight MV4 = new TrafficLight(4,"motor_vehicle");
//        TrafficLight MV5 = new TrafficLight(5,"motor_vehicle");
//        TrafficLight MV6 = new TrafficLight(6,"motor_vehicle");
//        TrafficLight MV7 = new TrafficLight(7,"motor_vehicle");
//        TrafficLight MV8 = new TrafficLight(8,"motor_vehicle");
//        TrafficLight MV9 = new TrafficLight(9,"motor_vehicle");
//        TrafficLight MV10 = new TrafficLight(10,"motor_vehicle");
//        TrafficLight MV11 = new TrafficLight(11,"motor_vehicle");
//
//        TrafficLight CL1 = new TrafficLight(1,"cycle");
//
//        trafficLights.add(MV1);
//        trafficLights.add(MV2);
//        trafficLights.add(MV3);
//        trafficLights.add(MV4);
//        trafficLights.add(MV5);
//        trafficLights.add(MV6);
//        trafficLights.add(MV7);
//        trafficLights.add(MV8);
//        trafficLights.add(MV9);
//        trafficLights.add(MV10);
//        trafficLights.add(MV11);
//
//        trafficLights.add(CL1);
//
//        MV1.addConflictingTrafficLight(MV5);
//        MV1.addConflictingTrafficLight(MV8);
//        MV1.addConflictingTrafficLight(CL1);
//
//        MV2.addConflictingTrafficLight(MV5);
//        MV2.addConflictingTrafficLight(MV6);
//        MV2.addConflictingTrafficLight(MV8);
//        MV2.addConflictingTrafficLight(MV9);
//        MV2.addConflictingTrafficLight(MV10);
//        MV2.addConflictingTrafficLight(MV11);
//        MV2.addConflictingTrafficLight(CL1);
//
//        MV3.addConflictingTrafficLight(MV5);
//        MV3.addConflictingTrafficLight(MV6);
//        MV3.addConflictingTrafficLight(MV7);
//        MV3.addConflictingTrafficLight(MV8);
//        MV3.addConflictingTrafficLight(MV10);
//        MV3.addConflictingTrafficLight(MV11);
//        MV3.addConflictingTrafficLight(CL1);
//
//        MV4.addConflictingTrafficLight(MV8);
//        MV4.addConflictingTrafficLight(MV11);
//        MV1.addConflictingTrafficLight(CL1);
//
//        MV5.addConflictingTrafficLight(MV1);
//        MV5.addConflictingTrafficLight(MV2);
//        MV5.addConflictingTrafficLight(MV3);
//        MV5.addConflictingTrafficLight(MV8);
//        MV5.addConflictingTrafficLight(MV11);
//
//        MV6.addConflictingTrafficLight(MV2);
//        MV6.addConflictingTrafficLight(MV3);
//        MV6.addConflictingTrafficLight(MV8);
//        MV6.addConflictingTrafficLight(MV9);
//        MV6.addConflictingTrafficLight(MV10);
//        MV6.addConflictingTrafficLight(MV11);
//
//        MV7.addConflictingTrafficLight(MV3);
//        MV7.addConflictingTrafficLight(MV10);
//
//        MV8.addConflictingTrafficLight(MV1);
//        MV8.addConflictingTrafficLight(MV2);
//        MV8.addConflictingTrafficLight(MV3);
//        MV8.addConflictingTrafficLight(MV4);
//        MV8.addConflictingTrafficLight(MV5);
//        MV8.addConflictingTrafficLight(MV6);
//        MV8.addConflictingTrafficLight(MV10);
//        MV8.addConflictingTrafficLight(MV11);
//        MV8.addConflictingTrafficLight(CL1);
//
//        MV9.addConflictingTrafficLight(MV2);
//        MV9.addConflictingTrafficLight(MV6);
//
//        MV10.addConflictingTrafficLight(MV2);
//        MV10.addConflictingTrafficLight(MV3);
//        MV10.addConflictingTrafficLight(MV6);
//        MV10.addConflictingTrafficLight(MV7);
//        MV10.addConflictingTrafficLight(MV8);
//        MV10.addConflictingTrafficLight(MV1);
//
//        MV11.addConflictingTrafficLight(MV2);
//        MV11.addConflictingTrafficLight(MV3);
//        MV11.addConflictingTrafficLight(MV4);
//        MV11.addConflictingTrafficLight(MV5);
//        MV11.addConflictingTrafficLight(MV8);
//        MV11.addConflictingTrafficLight(CL1);
//
//        CL1.addConflictingTrafficLight(MV1);
//        CL1.addConflictingTrafficLight(MV2);
//        CL1.addConflictingTrafficLight(MV3);
//        CL1.addConflictingTrafficLight(MV4);
//        CL1.addConflictingTrafficLight(MV8);
//        CL1.addConflictingTrafficLight(MV11);


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
//            NodeList transaction = doc.getElementsByTagName("transaction");
//            for(int j = 0; j < transaction.getLength(); j++) {
//                //Traverse down the transaction node till we get the billing info
//                NodeList details = ((Element)transaction.item(j)).getElementsByTagName("details");
//                NodeList account = ((Element)details.item(0)).getElementsByTagName("account");
//                NodeList billinginfo = ((Element)account.item(0)).getElementsByTagName("billing_info");
//                System.out.println("Type: "+((Element)billinginfo.item(0)).getAttribute("type"));
//
//                //Get all children nodes from billing info
//                NodeList billingChildren = billinginfo.item(0).getChildNodes();
//
//                for(int i = 0; i < billingChildren.getLength(); i++) {
//                    Node current = billingChildren.item(i);
//                    //Only want stuff from ELEMENT nodes
//                    if(current.getNodeType() == Node.ELEMENT_NODE) {
//                        System.out.println(current.getNodeName()+": "+current.getTextContent());
//                    }
//                }
//            }
                    //nNode = nList.item(temp);
//            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
//                NodeList TrafficLight = (Element)nNode.item(0).;
//                NodeList Trafficlight =  ((Element)nNode.item(0)).getElementsByTagName("billing_info");nNode.item(0).getChildNodes();
//                TrafficLight tl1 = searchTrafficLight(
//                        eElement.getElementsByTagName("TrafficLight1")..item(0).getTextContent(),
//                        Integer.parseInt(eElement.getElementsByTagName("group_id").item(0).getTextContent()),
//                        Integer.parseInt(eElement.getElementsByTagName("component_id").item(0).getTextContent()));
//                TrafficLight tl2 = searchTrafficLight(
//                        "motor_vehicle", 2, 1);
//
//            }
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

            for(TrafficLight tl : trafficLights){
                tl.update();

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
                highestPriorityLight.turnLightGreen();
            }
            try {//Voor opbouw van de ram
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }//Evalueren priotiteiten dingetje
    }

    public void stopThread(){
        stop = true;
    }
}
