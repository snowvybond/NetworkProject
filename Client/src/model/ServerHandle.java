package model;

import clientControl.ClientController;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketException;
import java.util.ArrayList;

public class ServerHandle implements Runnable {
    private ObjectInputStream in;
    private ClientController controller;
    public ServerHandle(ObjectInputStream in, ClientController clientController){
        this.in = in;
        this.controller = clientController;
    }
    @Override
    public void run() {
        Object clientObject;
        try {
            try {
                while ((clientObject = in.readObject()) != null){
                    ArrayList<Object> serverArray = (ArrayList<Object>) clientObject;
                    String staus = (String)serverArray.get(0);
                    if (staus.equals("101")){
                        Platform.runLater(() -> { controller.setTextConnect(); });
                        ArrayList<Object> aObj = new ArrayList<Object>();
                        String s = "101";
                        aObj.add(s);
                        aObj.add("greating");
                        Object send = aObj;
                        controller.sentObjectToServer(send,"greating");
                    }
                    if (staus.equals("201")){
                        ArrayList<String> filename = (ArrayList<String>) serverArray.get(1);
                        Platform.runLater(() -> { controller.updateFilenameView(filename); });
                    }
                    if (staus.equals("401")){
                        String data = (String)serverArray.get(1);
                        Platform.runLater(() -> { controller.updateTextViewer(data); });
                    }
                    if (staus.equals("600")){
                        System.out.println("Data duplicate");
                        Platform.runLater(() -> { controller.dataDuplicate(); });
                    }
                    if (staus.equals("701")){
                        String data = (String) serverArray.get(1);
                        System.out.println("Data download");
                        Platform.runLater(() -> { controller.setSaveData(data); });
                    }
                    if (staus.equals("801")){
                        String data = (String) serverArray.get(1);
                        System.out.println("Update chat");
                        Platform.runLater(() -> { controller.updateChat(data); });
                    }

                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SocketException e){
                Platform.runLater(() -> { controller.connectionLost(); });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
