import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MessageCreator {
    //<team_id>/<user_type>/<group_id>/<component_type>/<component_id>
    private int qos = 1;
    MessageCreator(){

    }

    //Create MqqtMessage with Payload
    public MqttMessage createPayload(String payload){
        MqttMessage message = new MqttMessage(payload.getBytes());
        message.setQos(qos);
        return message;
    }

    //Create the topic for the message
    public String createTopic(int teamID, String userType, int groupId, String componentType, int componentID){
        return teamID+"/"+userType+"/"+groupId+"/"+componentType+"/"+componentID;
    }

    public String turnLightOn(Message message){
        String topic = message.getTeamID()+"/"+message.getUserType()+"/"+message.getGroupID()+"/"+"light"+"/"+message.getComponentID();
        //System.out.println(topic);
        return topic;
    }
}
