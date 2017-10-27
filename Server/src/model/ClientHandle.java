package model;

import serverControl.ServerController;

import java.io.*;
import java.net.SocketException;
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
                        ArrayList<String> filename = DatabaseHandle.browserName();
                        String s = "201";
                        aObj.add(s);
                        aObj.add(filename);
                        Object send = aObj;
                        controller.sendObjectToClient(send,"sendfilelist","to client : "+controller.getOutputs().size()+"",controller.getOutputs().size()-1);
                    }
                    if (staus.equals("300")){
                        String filename = (String)clientArray.get(1);
                        String data = (String) clientArray.get(2);
                        DatabaseHandle.updateData(filename,data);
                        ArrayList<Object> aObj = new ArrayList<Object>();
                        ArrayList<String> name = DatabaseHandle.browserName();
                        String s = "201";
                        aObj.add(s);
                        aObj.add(name);
                        Object send = aObj;
                        controller.sendObjectToClient(send,"sendfilelist","to client : "+controller.getOutputs().size()+"",controller.getOutputs().size()-1);

                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SocketException e){
                System.err.println("Connection reset");
                System.out.println(Thread.activeCount());
                //controller.removeOutputs();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
