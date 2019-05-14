import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Sensor extends Receiver {
    public int value;
    public Sensor(String userType, int groupID){
        sensorTopic = teamID+"/"+userType+"/"+groupID+"/sensor/+";
        init();
    }

    public void messageArrived(String topic, MqttMessage message) throws Exception{
        int value = Integer.parseInt(message.toString());
        this.value = value;
    }
}
