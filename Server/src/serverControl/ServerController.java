package serverControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class ServerController {
    private HashMap<Integer,ObjectOutputStream> outputs;

    private Thread s;
    @FXML
    protected TextArea serverLog;
    @FXML
    protected TextField serverPort;
    @FXML
    protected Button start;

    public  ServerController(){
        outputs = new HashMap<Integer,ObjectOutputStream>();
    }

    @FXML
    public void startBtn(ActionEvent event) {
        int port = Integer.parseInt(serverPort.getText());
        s = new Thread(new Server(outputs,this,port));
        s.start();
        start.setDisable(true);
        serverPort.setDisable(true);
    }
    public void sendObjectToClient(Object obj, String statusPhrase, String clientnumber, int clientID){
        ObjectOutputStream out = outputs.get(clientID);
        this.displayLog(statusPhrase +" "+clientnumber);
        System.out.println(statusPhrase +" "+clientnumber );
        try {
            out.writeObject(obj);
            out.flush();
            System.out.println("Object Send");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }public void sendObjectToAllClient(Object obj, String statusPhrase){
        for (int clientID : outputs.keySet()){
            this.sendObjectToClient(obj,statusPhrase,"to clientID : "+clientID,clientID);
        }

    }

    public HashMap<Integer, ObjectOutputStream> getOutputs() {
        return outputs;
    }

    public void removeOutputs(int clientID){
        outputs.remove(clientID);
    }
    public void displayLog(String log){
        log += "\n";
        this.serverLog.appendText(log);
    }
    public void displayPortInUse(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Port already in use");
        alert.setHeaderText("Port already in use");
        alert.setContentText("Program will be terminate");
        alert.showAndWait();
        System.exit(1);
    }

}
