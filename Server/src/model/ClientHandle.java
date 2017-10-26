package model;

import serverControl.ServerController;

import java.io.*;
import java.util.ArrayList;

public class ClientHandle implements Runnable{


    private ObjectInputStream in ;
    private ServerController controller;





    public ClientHandle(ObjectInputStream in, ServerController controller) {
        this.controller = controller;
        this.in = in;
    }

    @Override
    public void run() {
        Object clientObject;
        try {
            try {
                while ((clientObject = in.readObject()) != null){
                    ArrayList<Object> clientArray = (ArrayList<Object>) clientObject;
                    String staus = (String)clientArray.get(0);
                    if (staus.equals("100")){
                        ArrayList<Object> aObj = new ArrayList<Object>();
                        ArrayList<String> filename = DatabaseHandle.browserData();
                        String s = "201";
                        aObj.add(s);
                        aObj.add(filename);
                        Object send = aObj;
                        controller.sendObjectToClient(send,"sendfilelist",controller.getOutputs().size()+"",controller.getOutputs().size()-1);
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
