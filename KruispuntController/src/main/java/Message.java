import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Message {
    private int teamID;
    private String userType;
    private int groupID;
    private String componentType;
    private int componentID;
    private int value;
    Message(String[] message, MqttMessage mqttMessage){
        teamID          = Integer.parseInt(message[0]);
        userType        = message[1];
        groupID         = Integer.parseInt(message[0]);
        componentType   = message[3];
        componentID     = Integer.parseInt(message[0]);
        value           = Integer.parseInt(mqttMessage.toString());
    }

    public int getTeamID(){return teamID;}
    public String getUserType(){return userType;}
    public int getGroupID() {return groupID;}
    public String getComponentType() {return componentType;}
    public int getComponentID() {return componentID;}
    public int getValue() {return value;}
}
