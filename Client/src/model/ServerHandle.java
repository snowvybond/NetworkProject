package model;

import clientControl.ClientController;

import java.io.IOException;
import java.io.ObjectInputStream;
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
                    ArrayList<String> filename = (ArrayList<String>) serverArray.get(1);
                    if (staus.equals("201")){
                        for (int i=0;i<filename.size();i++){
                            System.out.println(filename.get(i));
                        }

                    }

                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}