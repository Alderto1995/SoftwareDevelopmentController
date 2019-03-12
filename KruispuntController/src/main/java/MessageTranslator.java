import org.eclipse.paho.client.mqttv3.MqttMessage;
//<team_id>/<user_type>/<group_id>/<component_type>/<component_id>
public class MessageTranslator {
    Controller controller;
    MessageTranslator(Controller mController){
        controller = mController;
    }

    public void recieveMessage(String topic, MqttMessage mqttMessage){
        String[] topics = topic.split("/");
        Message message = new Message(topics, mqttMessage);
        switch(message.getComponentType()) {
            case "light":
                aLightMessage(message);
                break;
            case "sensor":
                aSensorMessage(message);
                break;
            default:
                System.out.println("Message not understand");
        }
    }

    private void aSensorMessage(Message message){
        switch(message.getUserType()) {
            case "foot":
                aPedestrianArrived(message);
                break;
            case "cycle":
                aCyclistArrived(message);
                break;
            case "motor_vehicle":
                aMotorVehicleArrived(message);
                break;
            case "public_service_vehicle":
                aPublicServiceVehicleArrived(message);
                break;
            case "vessel":
                aVesselArrived(message);
                break;
            default:
                System.out.println("Message not understand");
        }
    }

    private void aLightMessage(Message message){

    }

    private void aPedestrianArrived(Message message){

    }

    private void aCyclistArrived(Message message){

    }

    private void aMotorVehicleArrived(Message message){
        controller.aMotorVehicleArrived(message);
    }

    private void aPublicServiceVehicleArrived(Message message){

    }

    private void aVesselArrived(Message message){

    }
}
