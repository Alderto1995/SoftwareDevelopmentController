import java.util.ArrayList;
import java.util.List;

public class Intersection extends Thread {
    private List<TrafficLight> trafficLights;
    private boolean stop;

    public Intersection(){
        trafficLights = new ArrayList<>();
        TrafficLight TL1 = new TrafficLight(1,"motor_vehicle");
        TrafficLight TL2 = new TrafficLight(2,"motor_vehicle");
        trafficLights.add(TL1);
        trafficLights.add(TL2);
        TL1.addConflictingTrafficLight(TL2);
        TL2.addConflictingTrafficLight(TL1);
        stop = false;
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
        }
    }

    public void stopThread(){
        stop = true;
    }
}
