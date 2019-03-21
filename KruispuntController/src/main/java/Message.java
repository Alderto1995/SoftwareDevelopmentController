import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Message {
    private int teamID;
    private String userType;
    private int groupID;
    private String componentType;
    private int componentID;
    private int value;
    private String topic;

    Message(String topic, MqttMessage mqttMessage){
        String[] topics = topic.split("/");
        teamID          = Integer.parseInt(topics[0]);
        userType        = topics[1];
        groupID         = Integer.parseInt(topics[2]);
        componentType   = topics[3];
        componentID     = Integer.parseInt(topics[4]);
        value           = Integer.parseInt(mqttMessage.toString());
        this.topic = teamID + "/" + userType + "/" + groupID + "/" + componentType + "/+";
    }

    public int getTeamID(){return teamID;}
    public String getUserType(){return userType;}
    public int getGroupID() {return groupID;}
    public String getComponentType() {return componentType;}
    public int getComponentID() {return componentID;}
    public int getValue() {return value;}
    public String getTopic(){
        return topic;
    }
}
