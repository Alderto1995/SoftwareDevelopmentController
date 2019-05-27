import java.util.List;

public class PriorityLight extends Receiver {

    protected int groupID;
    protected String userType;
    protected int priority;
    protected int componentID;
    protected String topic;
    protected int status;
    protected String componentType;

    List<TrafficLight> conflictingTrafficLights;

    ///<team_id>/features/lifecycle/simulator/onconnect payload: 0
    protected void setStatus(int status) {
        this.status = status;
        sendStatus();
    }

    public int getStatus() {
        return status;
    }

    public void addConflictingTrafficLight(TrafficLight trafficLight) {
        conflictingTrafficLights.add(trafficLight);
    }

    public void sendStatus(){
        Publisher.instance.sendMessage(topic, Publisher.instance.createPayload(status));
    }
}