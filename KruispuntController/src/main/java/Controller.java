import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Controller extends  Receiver{
    private Publisher publisher;
    private Intersection intersection;
    private Bridge bridge;

    public Controller(){

    }
    public static void main(String[] args) {
        new Controller().start();
    }

    //start the Publisher & MessageCreator and start Receiver in different thread
    public void start()
    {
        publisher = new Publisher();
        publisher.start();

        sensorTopic = "1/features/lifecycle/simulator/onconnect";
        init();

        intersection = new Intersection();
        bridge = new Bridge();
        
        publisher.sendMessage("1/features/lifecycle/controller/onconnect");
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("Someone connected");
        intersection.start();
        bridge.start();
    }

    //Stop the threads and disconnect the Clients
//    public void stop(){
//        publisher.disconnect();
//    }
}
