public class Controller {
    private Publisher publisher;
    private Intersection intersection;

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
        intersection = new Intersection();
        intersection.start();
    }

    //Stop the threads and disconnect the Clients
    public void stop(){
        publisher.disconnect();
    }
}
