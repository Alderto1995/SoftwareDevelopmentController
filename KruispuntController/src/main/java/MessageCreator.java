import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MessageCreator {
    //<team_id>/<user_type>/<group_id>/<component_type>/<component_id>
    private int qos = 2;
    MessageCreator(){

    }

    //Create MqqtMessage with Payload
    public MqttMessage createPayload(String payload){
        MqttMessage message = new MqttMessage(payload.getBytes());
        message.setQos(qos);
        return message;
    }

    //Create the topic for the message
    public String createTopic(String teamID, String userType, String groupId, String componentType, String componentID){
        return teamID+"/"+userType+"/"+groupId+"/"+componentType+"/"+componentID;
    }
}
