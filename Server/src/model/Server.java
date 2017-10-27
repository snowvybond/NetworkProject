package model;

import serverControl.ServerController;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable {

    private int port ;
    private ServerSocket serverSocket;
    private ArrayList<ObjectOutputStream> outputs;
    private ServerController controller;


    public Server(ArrayList<ObjectOutputStream> outputs, ServerController controller, int port){
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
                //PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                //BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                outputs.add(out);
                Thread t = new Thread(new ClientHandle(in, controller));
                t.start();

            }
        } catch (Exception e) {
            System.exit(1);
        }

    }

}
