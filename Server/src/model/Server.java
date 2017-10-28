package model;

import javafx.application.Platform;
import serverControl.ServerController;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Random;

public class Server implements Runnable {

    private int port ;
    private ServerSocket serverSocket;
    private HashMap<Integer,ObjectOutputStream> outputs;
    private ServerController controller;


    public Server(HashMap<Integer,ObjectOutputStream> outputs, ServerController controller, int port){
        this.outputs = outputs;
        this.controller = controller;
        this.port = port;
    }


    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            controller.displayLog("Server started");
            System.out.println("Server started.");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                int clientID = this.randomClientID();
                outputs.put(clientID,out);
                Thread t = new Thread(new ClientHandle(in, controller,clientID));
                t.start();

            }
        } catch (Exception e) {
            Platform.runLater(() -> { controller.displayPortInUse();});
        }

    }

    private int randomClientID(){
        Random rand = new Random();
        int randomNum = rand.nextInt((9999 - 1) + 1) + 1;
        for (int clientID : outputs.keySet()){
            if (clientID == randomNum){
                return this.randomClientID();
            }
        }
        return randomNum;
    }

}
