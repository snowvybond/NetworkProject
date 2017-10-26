package clientControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import model.ServerHandle;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;


public class ClientController {
    private Socket serverSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    @FXML
    protected Button connectBtn;
    @FXML
    protected Button viewBtn;

    private void connect(String ip,int port) {
        try {
            serverSocket = new Socket(ip, port);
            System.out.println("Connect to server");
            out = new ObjectOutputStream(serverSocket.getOutputStream());
            in = new ObjectInputStream(serverSocket.getInputStream());
            ArrayList<Object> sentObj = new ArrayList<Object>();
            String status = "100";
            sentObj.add(status);
            Object sent = sentObj;
            this.sentObjectToServer(sent,"Connect");

            Thread c = new Thread(new ServerHandle(in,this));
            c.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sentObjectToServer(Object obj,String statusPhrase){
        try {
            out.writeObject(obj);
            out.flush();
            System.out.println(statusPhrase);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connectBtnAction (ActionEvent event){
        this.connect("127.0.0.1",5555);
    }

}
