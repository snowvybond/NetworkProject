package serverControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import model.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ServerController {
    private ArrayList<ObjectOutputStream> outputs;
    private Thread s;
    public  ServerController(){
        outputs = new ArrayList<>();
        s = new Thread(new Server(outputs,this));
    }

    @FXML
    protected Button start;
    @FXML
    public void startBtn(ActionEvent event) {
        s.start();
        start.setDisable(true);
    }
    public void sendObjectToClient(Object obj, String statusPhrase, String message, int i){
        ObjectOutputStream out = outputs.get(i);
        System.out.println(statusPhrase +" "+message );
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
}
