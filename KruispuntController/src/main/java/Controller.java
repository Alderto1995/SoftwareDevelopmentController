import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Controller {
    private Publisher publisher;
    private Receiver receiver;
    private MessageCreator messageCreator;
    private MessageTranslator messageTranslator;
    private int teamID = 1; //TeamId to connect with
    public Controller(){

    }
    public static void main(String[] args) {
        new Controller().start();
    }

    //start the Publisher & MessageCreator and start Receiver in different thread
    public void start(){
        messageCreator = new MessageCreator();
        messageTranslator = new MessageTranslator(this);
        receiver = new Receiver(this, teamID + "/#");
        receiver.start();
        publisher = new Publisher();
        publisher.start();

        //For testing purposes send a start signal
        sleep(1000);
        publisher.sendMessage("1/motor_vehicle/1/sensor/1", messageCreator.createPayload("1"));
    }

    //Stop the threads and disconnect the Clients
    public void stop(){
        publisher.disconnect();
        try{
            receiver.disconnect();
            receiver.join();
        }catch(Exception e){
            System.out.println("Error sluiten Thread");
        }
    }

    //Do stuff if message arrived
    public void messageArrived(String topic, MqttMessage message){
        sleep(4000);
        messageTranslator.recieveMessage(topic, message);
    }


    private void sleep(int miliSeconds){
        try {
            Thread.sleep(miliSeconds);
        }
        catch(Exception e){
            System.out.println("Ik wil niet slapen");
        }
    }

    public void aMotorVehicleArrived(Message message){
        publisher.sendMessage(messageCreator.turnLightOn(message),messageCreator.createPayload("2"));
        sleep(4000);
        publisher.sendMessage(messageCreator.turnLightOn(message),messageCreator.createPayload("1"));
        sleep(4000);
        publisher.sendMessage(messageCreator.turnLightOn(message),messageCreator.createPayload("0"));
    }
}
