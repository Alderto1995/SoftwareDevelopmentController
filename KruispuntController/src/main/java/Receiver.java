import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Receiver extends Thread implements MqttCallback {

    MqttClient receiverClient;
    Controller controller;
    String topic;

    //Initialize controller and Topic
    public Receiver(Controller mController, String mTopic){
        controller = mController;
        topic = mTopic;
    }

    //Start Listening to topic
    public void run(){
        String broker       = "tcp://broker.0f.nl:1883";
        String clientId     = "Listener";
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            receiverClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Receiver connecting to broker: "+broker);
            receiverClient.connect(connOpts);
            receiverClient.setCallback(this);
            receiverClient.subscribe(topic);
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
        System.out.println("Ontvangen Topic:"+ topic);
        System.out.println("Ontvangen bericht:"+ message.toString());
        controller.messageArrived(topic, message.toString());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // TODO Auto-generated method stub
    }

    //Method to disconnect
    public void disconnect(){
        try{
            receiverClient.disconnect();
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