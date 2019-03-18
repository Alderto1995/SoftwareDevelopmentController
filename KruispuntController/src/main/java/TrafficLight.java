import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.List;

public class TrafficLight {


    private String topic;
    private int status;
    private int groupID;
    private String userType;
    private int priority;
    List<TrafficLight> conflictingTrafficLights;
    public TrafficLight(int groupID, int teamID, String userType){
        priority = 0;
        conflictingTrafficLights = new ArrayList<>();
        status = 0;
        this.groupID = groupID;
        this.userType = userType;
        createTopic(teamID);
    }

    public void addConflictingTrafficLight(TrafficLight trafficLight){
        conflictingTrafficLights.add(trafficLight);
    }
    public void createTopic(int teamID){
        topic = teamID+"/"+userType+"/"+groupID+"/light/+";
    }

    public void setStatus(int status) {

        this.status = status;
        Publisher.instance.sendMessage(topic, MessageCreator.instance.createPayload(status));
    }

    public int getStatus() {
        return status;
    }

    public void turnLightGreen(){
        if(getStatusConflictingTrafficlights())
        {
            setStatus(2);
        }
    }

    public int getGroupID() {
        return groupID;
    }

    public void decreasePriority(){
        priority--;
    }

    public void increasePriority(){
        priority++;
    }

    public int getPriority(){
        return priority;
    }

    public boolean getStatusConflictingTrafficlights(){
        for (TrafficLight tl:conflictingTrafficLights) {
            if(tl.getStatus() == 1 || tl.getStatus() == 2)
                return false;
        }
        return true;
    }
}
