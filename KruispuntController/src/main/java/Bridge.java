import java.time.Duration;
import java.time.LocalDateTime;

public class Bridge extends Thread {

    private Sensor deckSensor = new Sensor("bridge", 1);
    private Sensor boatSensor = new Sensor("vessel", 3);
    private String tlBridgeLightTopic = "/bridge/1/light/1";
    private String gateBridgeTopic1 = "/bridge/1/gate/1";
    private String gateBridgeTopic2 = "/bridge/1/gate/2";
    private String bridgeTopic = "/bridge/1/bridge/1";
    private BoatLight boatLight1 = new BoatLight("vessel",1,1);
    private BoatLight boatLight2 = new BoatLight("vessel",2,1);
    private LocalDateTime timeSinceBridgeOpened;
    private int timeToWaitForNextBridgeOpening = 60;
    private BoatLight firstBoatLightToOpen;
    private BoatLight secondBoatLightToOpen;
    private int waitingTimeTl= 6000;
    private int waitingTimeGate = 4000;
    private int waitingTimeBridge = 10000;
    private boolean stop = false;

    public Bridge(){
        timeSinceBridgeOpened = LocalDateTime.now().minusSeconds(timeToWaitForNextBridgeOpening);
    }

    public void run(){
        while(!stop){
            boatLight1.update();
            boatLight2.update();
            if(firstBoatLightToOpen == null) {
                if (boatLight1.getPriorityBL()) {
                    firstBoatLightToOpen = boatLight1;
                    secondBoatLightToOpen = boatLight2;

                } else if (boatLight2.getPriorityBL()) {
                    firstBoatLightToOpen = boatLight2;
                    secondBoatLightToOpen = boatLight2;
                }
            }
            if(Duration.between(timeSinceBridgeOpened,LocalDateTime.now()).getSeconds() >= timeToWaitForNextBridgeOpening){
                if(firstBoatLightToOpen != null){
                    openBridge();
                }
            }
        }
    }





    private void openBridge(){
        Publisher.instance.sendMessage(tlBridgeLightTopic,Publisher.instance.createPayload(2));
        wait(waitingTimeTl);
        Publisher.instance.sendMessage(gateBridgeTopic1, Publisher.instance.createPayload(1));
        while(deckSensor.value == 1){
            wait(100);
        }
        Publisher.instance.sendMessage(gateBridgeTopic2, Publisher.instance.createPayload(1));
        wait(waitingTimeGate);
        Publisher.instance.sendMessage(bridgeTopic,Publisher.instance.createPayload(1));
        wait(waitingTimeBridge);
        firstBoatLightToOpen.turnLightGreen();
        while(firstBoatLightToOpen.status == 1){
            firstBoatLightToOpen.update();
        }
        while(boatSensor.value == 1){
            wait(100);
        }
        if(secondBoatLightToOpen.getPriorityBL()){
            secondBoatLightToOpen.turnLightGreen();
        }
        while(secondBoatLightToOpen.status == 1){
            secondBoatLightToOpen.update();
        }
        while(boatSensor.value == 1){
            wait(100);
        }
        closeBridge();
    }

    private void closeBridge(){
        Publisher.instance.sendMessage(gateBridgeTopic1, Publisher.instance.createPayload(0));
        Publisher.instance.sendMessage(gateBridgeTopic2, Publisher.instance.createPayload(0));
        wait(waitingTimeGate);
        Publisher.instance.sendMessage(bridgeTopic,Publisher.instance.createPayload(0));
        wait(waitingTimeBridge);
        Publisher.instance.sendMessage(tlBridgeLightTopic, Publisher.instance.createPayload(0));
    }


    private void wait (int sleepingTime){
        try {
            Thread.sleep(sleepingTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stopThread(){
        stop = true;
    }





    //Is er een boot?
    //Is de cooldown tijd voorbij?
        //Ja:
            //Lichten rood
            //6 seconden wachten)
            //Eerste  2 slagbomen dicht (Duurt 4 seconden)
                //Wacht tot brug leeg is
                    //Andere 2 slagbomen dicht (Duurt 4 seconden)
                    //Brug open (Duurt 10 seconden)
                        //Lichten aan 1 kant op groen
                            //Boten erlangs
                        //Lihcten aan andere kant  op groen
                            //boten erlangs

                        //Brug dicht (Duurt 10 seconden)
                        //Alle slagbomen open (Duurt 4 seconden)
                        //Lichten groen

        //Nee: (Overnieuw)
}
