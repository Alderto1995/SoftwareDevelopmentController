import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.UUID;

public class Receiver extends Communication implements MqttCallback {

    protected String sensorTopic;

    private MqttClient client;

    protected void init(){
        String clientId = UUID.randomUUID().toString();
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            client = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Receiver connecting to broker: "+broker);
            client.connect(connOpts);
            client.setCallback(this);
            client.subscribe(sensorTopic);
            System.out.println("Receiver connected");
        } catch(MqttException me) {
            handleException(me);
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        // TODO Auto-generated method stub
    }

    //If message Arrived, do stuff
    @Override
    public void messageArrived(String topic, MqttMessage message)
            throws Exception {
        System.out.println("Received topic:"+ topic + message.toString());
        //System.out.println("Received message:"+ message.toString());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // TODO Auto-generated method stub
    }

    //Method to disconnect
    public void disconnect(){
        try{
            client.disconnect();
            System.out.println("receiver Disconnected");
        }catch(MqttException me){

            handleException(me);
        }
    }

    //Method to handle exceptions
    private void handleException(MqttException me){
        System.out.println("reason "+me.getReasonCode());
        System.out.println("msg "+me.getMessage());
        System.out.println("loc "+me.getLocalizedMessage());
        System.out.println("cause "+me.getCause());
        System.out.println("excep "+me);
        me.printStackTrace();
    }
}