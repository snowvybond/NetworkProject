package serverControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ServerController {
    private ArrayList<ObjectOutputStream> outputs;
    private Thread s;
    @FXML
    protected TextArea serverLog;
    @FXML
    protected TextField serverPort;
    @FXML
    protected Button start;

    public  ServerController(){
        outputs = new ArrayList<>();
    }

    @FXML
    public void startBtn(ActionEvent event) {
        int port = Integer.parseInt(serverPort.getText());
        s = new Thread(new Server(outputs,this,port));
        s.start();
        start.setDisable(true);
        serverPort.setDisable(true);
    }
    public void sendObjectToClient(Object obj, String statusPhrase, String clientnumber, int i){
        ObjectOutputStream out = outputs.get(i);
        System.out.println(statusPhrase +" "+clientnumber );
        try {
            out.writeObject(obj);
            out.flush();
            System.out.println("Object Send");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public ArrayList<ObjectOutputStream> getOutputs() {
        return outputs;
    }
    public void removeOutputs(){
        outputs.remove(outputs.size()-1);
    }
    public void displayLog(String log){
        this.serverLog.appendText(log);
    }

}
