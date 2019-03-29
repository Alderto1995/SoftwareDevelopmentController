import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.paho.client.mqttv3.*;

public class TrafficLight extends Receiver {

    private String topic;
    private int status;
    private int groupID;
    private String userType;
    private int priority;
    private LocalDateTime endDate;
    List<TrafficLight> conflictingTrafficLights;
    private int durationGreen;
    private int durationYellow;
    private int durationRed;

    public TrafficLight(int groupID, String userType){
        priority = 0;
        conflictingTrafficLights = new ArrayList<>();
        status = 0;
        this.groupID = groupID;
        this.userType = userType;
        topic = teamID+"/"+userType+"/"+groupID+"/light/1";
        sensorTopic = teamID+"/"+userType+"/"+groupID+"/sensor/+";
        durationGreen = 6;
        durationYellow = 3;
        durationRed = 4;
        init();
    }

    public void update(){
        if(endDate != null){
            LocalDateTime now = LocalDateTime.now();
            long duration = Duration.between(now, endDate).getSeconds();

            if(duration <= 0){
                if(status == 2){
                    turnLightYellow();
                }
                else if(status == 1){
                    turnLightRed();
                }
                else if(status == 0){
                    endDate = null;
                }
            }
        }
    }

    public void addConflictingTrafficLight(TrafficLight trafficLight){
        conflictingTrafficLights.add(trafficLight);
    }

    public void setStatus(int status) {
        this.status = status;
        Publisher.instance.sendMessage(topic, Publisher.instance.createPayload(status));
    }

    public int getStatus() {
        return status;
    }

    public void turnLightGreen(){
        setStatus(2);
        endDate = LocalDateTime.now().plusSeconds(durationGreen);
        update();
    }

    public void turnLightYellow() {
        setStatus(1);
        endDate = LocalDateTime.now().plusSeconds(durationYellow);
        update();
    }

    public void turnLightRed(){
        setStatus(0);
        endDate = LocalDateTime.now().plusSeconds(durationRed);
        update();
    }

    public int getGroupID() {
        return groupID;
    }

    public void decreasePriority(){
        if(priority>0){
            priority--;
        }
    }

    public void increasePriority(){
        priority++;
    }

    public int getPriorityTL(){
        return priority;
    }

    public boolean isConflicting(){
        for (TrafficLight tl : conflictingTrafficLights) {
            if(tl.getStatus() == 1 || tl.getStatus() == 2)
                return true;
        }
        return false;
    }

    public boolean isAvailable(){
        if(isConflicting() || endDate != null){
            return false;
        }
        else{
            return true;
        }
    }

    public String getSensorTopic(){
        return sensorTopic;
    }

    public void messageArrived(String topic, MqttMessage message)
            throws Exception {
        System.out.println("Received Topic:"+ topic);
        System.out.println("Received message:"+ message.toString());
        int value = Integer.parseInt(message.toString());
        if(value == 0){
            decreasePriority();
        }
        else if(value == 1){
            increasePriority();
        }
    }
}
