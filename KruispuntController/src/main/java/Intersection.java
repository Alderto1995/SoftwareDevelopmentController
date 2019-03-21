import java.util.ArrayList;
import java.util.List;

public class Intersection {
    private List<TrafficLight> trafficLights;
    private Updater updater;

    public Intersection(int teamID){
        trafficLights = new ArrayList<>();
        TrafficLight TL1 = new TrafficLight(teamID,1,"motor_vehicle");
        TrafficLight TL2 = new TrafficLight(teamID,2,"motor_vehicle");
        trafficLights.add(TL1);
        trafficLights.add(TL2);
        TL1.addConflictingTrafficLight(TL2);
        TL2.addConflictingTrafficLight(TL1);
        updater = new Updater(this);
        updater.start();
    }

    public void update(Message message){
        updateTrafficLight(message);
    }

    public TrafficLight getTrafficLights(int groupID) {
        return trafficLights.get(groupID-1);
    }

    public void updateTrafficLight(Message message){
        TrafficLight tl = getTrafficLight(message.getTopic());

        if(tl != null){
            if(message.getValue() == 0){
                tl.decreasePriority();
            }
            else{
                tl.increasePriority();
            }
        }
    }

    public TrafficLight getTrafficLight(String topic){
        for(TrafficLight tl : trafficLights){
            if(tl.getSensorTopic().equals(topic)){
                return tl;
            }
        }
        return null;
    }

    public void stop(){
        updater.stopThread();
    }

    class Updater extends Thread {
        private Intersection intersection;
        private boolean stop;

        public Updater(Intersection intersection){
            this.intersection = intersection;
            stop = false;
        }

        public void run(){
            while(!stop){
                TrafficLight highestPriorityLight = null;

                for(TrafficLight tl : intersection.trafficLights){
                    tl.update();

                    if(tl.isAvailable() && tl.getPriority() != 0){
                        if(highestPriorityLight == null){
                            highestPriorityLight = tl;
                        }
                        else{
                            int priority = tl.getPriority();
                            if(priority > highestPriorityLight.getPriority()){
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
}
