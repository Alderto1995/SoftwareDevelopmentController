public class Controller {
    private Publisher publisher;
    private Receiver receiver;
    private MessageCreator messageCreator;
    private String teamID = "3"; //TeamId to connect with
    public Controller(){

    }
    public static void main(String[] args) {
        new Controller().start();
    }

    //start the Publisher & MessageCreator and start Receiver in different thread
    public void start(){
        messageCreator = new MessageCreator();
        receiver = new Receiver(this, createTopic());
        receiver.start();
        publisher = new Publisher();

        //For testing purposes send a start signal
        sleep(1000);
        publisher.sendMessage(teamID, messageCreator.createPayload("Begin"));


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
    public void messageArrived(String topic, String message){
        sleep(4000);
        publisher.sendMessage(messageCreator.createTopic(teamID,"motor_vehicle", "1","light","1"), messageCreator.createPayload("1"));
    }

    //Create topic to listen to
    private String createTopic(){
        return teamID + "/#";
    }

    private void sleep(int miliSeconds){
        try {
            Thread.sleep(4000);
        }
        catch(Exception e){
            System.out.println("Ik wil niet slapen");
        }
    }
}
