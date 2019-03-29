import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import java.util.Timer;
import java.util.UUID;

public class Publisher extends Communication {
    public static Publisher instance;

    private int qos = 1;
    private String clientId;
    private MqttClient publisherClient;

    public Publisher(){
        if(instance == null){
            clientId = UUID.randomUUID().toString();
            instance = this;
        }
        connect();
    }

    //Connect with broker
    public void connect(){
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            publisherClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Publisher Connecting to broker: "+broker);
            publisherClient.connect(connOpts);
            System.out.println("Publisher connected");
        } catch(MqttException me) {
            handleException(me);
        }
    }

    //Disconnect with the broker
    public void disconnect(){
        try{
            publisherClient.disconnect();
            System.out.println("publisher Disconnected");
        }catch(MqttException me){
            handleException(me);
        }
    }

    //Send a message with topic and a message
    public void sendMessage(String topic, MqttMessage message){
        try{
            System.out.println("Publishing message: "+message.toString());
            System.out.println(topic);
            publisherClient.publish(topic, message);
            System.out.println("Message published");

        }catch(MqttException me){
            handleException(me);
        }
    }

    //Create MqqtMessage with Payload
    public MqttMessage createPayload(int payload){
        MqttMessage message = new MqttMessage(Integer.toString(payload).getBytes());
        message.setQos(qos);
        return message;
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