import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class BoatLight extends PriorityLight{


    private LocalDateTime timeSinceLow;
    private LocalDateTime timeSinceHigh;
    private int timeToWaitForNextVessel = 5;
    public BoatLight(String userType, int groupID, int componentID){
        conflictingTrafficLights = new ArrayList<>();
        status = 0;
        this.groupID = groupID;
        this.userType = userType;
        this.componentID = componentID;
        componentType = "light";
        topic = "/"+userType+"/"+groupID+"/"+componentType+"/"+componentID;
        sensorTopic = teamID+"/"+userType+"/"+groupID+"/sensor/+";
        init();
    }

    public void turnLightGreen(){
        setStatus(2);
    }

    private void turnLightRed(){
        setStatus(0);
    }

    public void messageArrived(String topic, MqttMessage message)
            throws Exception {
        int value = Integer.parseInt(message.toString());
        if(value == 0){
            timeSinceLow = LocalDateTime.now();
            timeSinceHigh = null;
        }
        else if(value == 1){
            timeSinceHigh = LocalDateTime.now();
            timeSinceLow = null;
        }
    }

    public void update(){
        if(status != 0){
            if(timeSinceLow != null){
                if(Duration.between(timeSinceLow, LocalDateTime.now()).getSeconds() >= timeToWaitForNextVessel){
                    turnLightRed();
                    System.out.println("Zet bootlicht op rood");
                }
            }
        }
    }

//    public boolean allVesselsGone(){
//        if(timeSinceLow != null){
//            if(Duration.between(timeSinceLow, LocalDateTime.now()).getSeconds() >= timeToWaitForNextVessel){
//                turnLightRed();
//                return true;
//            }
//        }
//        return false;
//    }

    public boolean getPriorityBL(){
        if(timeSinceHigh != null){
            return true;
        }
        return false;
    }
}
