import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.List;

public class RoadJammedSensor extends Sensor {
    private List<TrafficLight> conflictingTrafficLights;
    public RoadJammedSensor(String userType, int groupID) {
        super(userType, groupID);
    }

    public void addConflictingTrafficLight(TrafficLight trafficLight){
        conflictingTrafficLights.add(trafficLight);
    }

    public void messageArrived(String topic, MqttMessage message) throws Exception{
        System.out.println("Deck sensor"+message);
        int value = Integer.parseInt(message.toString());
        boolean roadJammed;
        if(value == 1){
            roadJammed = true;
        }
        else{
             roadJammed = false;
        }
        for(TrafficLight tl: conflictingTrafficLights){
            tl.trafficIsJammed = roadJammed;
        }
    }
}
