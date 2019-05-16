import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.paho.client.mqttv3.*;

public class TrafficLight extends PriorityLight {



    private LocalDateTime endDate;
    private LocalDateTime waitingTime;
    private int durationGreen;
    private int durationYellow;
    //private int durationRed;
    private int evacuationTime;
    private int durationWaiting;
    private TrafficLight coupledLight;
    private int localIncrease;
    private boolean markNextGroup;
    public boolean trafficIsJammed = false;

    public TrafficLight(String userType, int groupID, int componentID, int evacuationTime){
        priority = 0;
        conflictingTrafficLights = new ArrayList<>();
        status = 0;
        this.groupID = groupID;
        this.userType = userType;
        this.componentID = componentID;
        this.evacuationTime = evacuationTime;
        componentType = "light";
        topic = "/"+userType+"/"+groupID+"/"+componentType+"/"+componentID;
        sensorTopic = teamID+"/"+userType+"/"+groupID+"/sensor/+";
        durationGreen = 8;
        durationYellow = 4;
        //durationRed = 4;
        durationWaiting = 10;
        localIncrease = 0;
        markNextGroup = false;
        init();
    }



    private void decreasePriority(){
        if(priority>0){
            priority--;
        }
    }

    private void increasePriority(){
        priority++;
    }

    private void increaseLocalPriority(){
        priority++;
        localIncrease++;
    }

    private void removeLocalPriority(){
        priority -= localIncrease;
        localIncrease = 0;
    }

    public void update(){
        LocalDateTime now = LocalDateTime.now();
        if(priority > 0 && waitingTime == null){
            waitingTime = now;
        }
        else if(waitingTime != null){
            if(Duration.between(waitingTime, now).getSeconds() >= durationWaiting){
                waitingTime = null;
                increaseLocalPriority();
            }
        }

        if(endDate != null){
            if(now.isAfter(endDate)){
                if(status == 2){
                    turnLightYellow();
                }
                else if(status == 1){
                    turnLightRed();
                }
//                else if(status == 0){
//                    endDate = null;
//                }
            }
        }
    }


    public void turnLightGreen(){
        setStatus(2);
        if(coupledLight != null){
            if(coupledLight.status != 2){
                coupledLight.turnLightGreen();
            }
        }
        removeLocalPriority();
        endDate = LocalDateTime.now().plusSeconds(durationGreen);
        markNextGroup = false;
        update();
    }

    public void turnLightYellow() {
        setStatus(1);
        endDate = LocalDateTime.now().plusSeconds(durationYellow);
        update();
    }

    public void turnLightRed(){
        setStatus(0);
        endDate = null;
        update();
    }

    public int getGroupID() {
        return groupID;
    }

    public int getTotalTime(){return evacuationTime+durationGreen+durationYellow;}

    public int getPriorityTL(){
        return priority;
    }

    public int getComponentID(){ return componentID;}

    public String getUserType() {return userType;}

    public String getComponentType(){return componentType;}

    public void markForNextGroup(){
        markNextGroup = true;
    }

    public boolean isMarkedNextGroup(){
        return markNextGroup;
    }

    public boolean isConflicting(){
        for (TrafficLight tl : conflictingTrafficLights) {
            if(tl.getStatus() == 1 || tl.getStatus() == 2 || tl.isMarkedNextGroup())
                return true;
        }
        return false;
    }

    public boolean isAvailable(){
        if(endDate != null || isConflicting() || markNextGroup || trafficIsJammed){
            return false;
        }
        return true;
    }

    public String getSensorTopic(){
        return sensorTopic;
    }

    public void groupedWith(TrafficLight light){
        coupledLight = light;
    }

    public void messageArrived(String topic, MqttMessage message) {
        int value = Integer.parseInt(message.toString());
        if(value == 0){
            decreasePriority();
        }
        else if(value == 1){
            increasePriority();
        }
    }
}
