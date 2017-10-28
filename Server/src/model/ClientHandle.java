package model;

import serverControl.ServerController;

import java.io.*;
import java.net.SocketException;
import java.util.ArrayList;

public class ClientHandle implements Runnable{

    private ObjectInputStream in ;
    private ServerController controller;
    private int clientID;





    public ClientHandle(ObjectInputStream in, ServerController controller,int clientID) {
        this.controller = controller;
        this.in = in;
        this.clientID = clientID;
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
                        String s = "101";
                        aObj.add(s);
                        aObj.add("greating");
                        Object send = aObj;
                        controller.sendObjectToClient(send,"greating","to clientID : "+clientID,clientID);
                    }
                    if (staus.equals("101")){
                        ArrayList<Object> aObj = new ArrayList<Object>();
                        ArrayList<String> filename = DatabaseHandle.browserName();
                        String s = "201";
                        aObj.add(s);
                        aObj.add(filename);
                        Object send = aObj;
                        controller.sendObjectToClient(send,"sendfilelist","to clientID : "+clientID,clientID);
                    }
                    if (staus.equals("300")){
                        boolean isDup = false;
                        String filename = (String)clientArray.get(1);
                        ArrayList<String> oldname = DatabaseHandle.browserName();
                        for (String n:oldname){
                            if (filename.equals(n)){
                                isDup = true;
                                break;
                            }
                        }
                        if (isDup){
                            String s = "600";
                            ArrayList<Object> aObj = new ArrayList<Object>();
                            aObj.add(s);
                            aObj.add("data duplicate");
                            Object send = aObj;
                            controller.sendObjectToClient(send,"data duplicate","to clientID : "+clientID,clientID);
                        }
                        else{
                            String data = (String) clientArray.get(2);
                            DatabaseHandle.updateData(filename,data);
                            ArrayList<Object> aObj = new ArrayList<Object>();
                            ArrayList<String> name = DatabaseHandle.browserName();
                            String s = "201";
                            aObj.add(s);
                            aObj.add(name);
                            Object send = aObj;
                            controller.displayLog("Recieve Data from clientID : "+clientID);
                            controller.sendObjectToAllClient(send,"sendfilelist");
                        }

                    }
                    if (staus.equals("400")){
                        String filename = (String)clientArray.get(1);
                        String data = DatabaseHandle.browseData(filename);
                        ArrayList<Object> aObj = new ArrayList<Object>();
                        String s = "401";
                        aObj.add(s);
                        aObj.add(data);
                        Object send = aObj;
                        controller.sendObjectToClient(send,"send data file","to clientID : "+clientID,clientID);

                    }
                    if (staus.equals("500")){
                        String filename = (String)clientArray.get(1);
                        DatabaseHandle.deleteData(filename);
                        ArrayList<Object> aObj = new ArrayList<Object>();
                        ArrayList<String> name = DatabaseHandle.browserName();
                        String s = "201";
                        aObj.add(s);
                        aObj.add(name);
                        Object send = aObj;
                        controller.displayLog("Delete data by clientID : "+clientID);
                        controller.sendObjectToAllClient(send,"sendfilelist");
                    }
                    if (staus.equals("700")){
                        String filename = (String)clientArray.get(1);
                        String data = DatabaseHandle.browseData(filename);
                        ArrayList<Object> aObj = new ArrayList<Object>();
                        String s = "701";
                        aObj.add(s);
                        aObj.add(data);
                        Object send = aObj;
                        controller.sendObjectToClient(send,"send data file for download","to clientID : "+clientID,clientID);
                    }
                    if (staus.equals("800")){
                        String data = (String) clientArray.get(1);
                        ArrayList<Object> aObj = new ArrayList<Object>();
                        String s = "801";
                        aObj.add(s);
                        aObj.add(data);
                        Object send = aObj;
                        controller.displayLog("Chat data recieve by clientID : "+clientID);
                        controller.sendObjectToAllClient(send,"update chat");
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SocketException e){
                System.out.println("Connection reset in clientID : "+clientID);
                controller.displayLog("Connection reset in clientID : "+clientID);
                System.out.println(Thread.activeCount());
                controller.removeOutputs(clientID);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
