import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Timer;
import java.util.TimerTask;

public class Controller {
    private Publisher publisher;
    private Receiver receiver;
    private MessageCreator messageCreator;
    private MessageTranslator messageTranslator;
    private MessageQue messageQue;
    private int teamID = 1; //TeamId to connect with
    private Intersection intersection;
    public Controller(){

    }
    public static void main(String[] args) {
        new Controller().start();
    }

    //start the Publisher & MessageCreator and start Receiver in different thread
    public void start()
    {
        intersection = new Intersection(teamID);
        messageCreator = new MessageCreator();
        messageTranslator = new MessageTranslator(this);
        receiver = new Receiver(this, teamID + "/+/+/sensor/+");
        receiver.start();
        publisher = new Publisher();
        publisher.start();

        messageQue = new MessageQue();
        messageQue.start();


        //For testing purposes send a start signal
        //sleep(1000);
        //publisher.sendMessage("1/motor_vehicle/1/light/1", messageCreator.createPayload("TestVanafAldertAlsStartSignaal"));
        //turnLightGreenEver4Seconds();
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
        Message newMessage = new Message(topic, message);
        TrafficLight trafficLight = intersection.getTrafficLights(newMessage.getGroupID());
        trafficLight.increasePriority();

        if(trafficLight.getStatusConflictingTrafficlights()){
            aMotorVehicleArrived(newMessage);
        }
        //sleep(4000);
        //publisher.sendMessage(messageCreator.createTopic(teamID,"motor_vehicle", 1, "light", 1), messageCreator.createPayload("TestVanafAldertAlsReactiesOpSensor"));
        messageTranslator.receiveMessage(topic, message);
        //turnLightToGreen();
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
        publisher.sendMessage(messageCreator.turnLightOn(message),messageCreator.createPayload(2));
        sleep(4000);
        publisher.sendMessage(messageCreator.turnLightOn(message),messageCreator.createPayload(1));
        sleep(4000);
        publisher.sendMessage(messageCreator.turnLightOn(message),messageCreator.createPayload(0));
    }

    public void turnLightGreenEver4Seconds(){
        while(true){
            turnLightToGreen();
            }
    }

    public void turnLightToGreen(){
        sleep(4000);
        publisher.sendMessage(messageCreator.createTopic(teamID,"motor_vehicle", 1, "light", 1),messageCreator.createPayload(2));
        sleep(2000);
        publisher.sendMessage(messageCreator.createTopic(teamID,"motor_vehicle", 1, "light", 1),messageCreator.createPayload(1));
        sleep(2000);
        publisher.sendMessage(messageCreator.createTopic(teamID,"motor_vehicle", 1, "light", 1),messageCreator.createPayload(0));

    }
}
