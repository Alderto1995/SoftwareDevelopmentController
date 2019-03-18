import java.util.ArrayList;
import java.util.List;

public class Intersection {
    private List<TrafficLight> trafficLights;
    public Intersection(int teamID){
        trafficLights = new ArrayList<>();
        TrafficLight TL1 = new TrafficLight(1,1,"motor_vehicle");
        TrafficLight TL2 = new TrafficLight(1,2,"motor_vehicle");
        trafficLights.add(TL1);
        trafficLights.add(TL2);
        TL1.addConflictingTrafficLight(TL2);
        TL2.addConflictingTrafficLight(TL1);
    }

    public TrafficLight getTrafficLights(int groupID) {
        return trafficLights.get(groupID-1);
    }

    public void updateTrafficLight(){
        trafficLights.get(1).increasePriority();
    }

}
