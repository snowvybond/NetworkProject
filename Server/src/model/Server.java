package model;

import serverControl.ServerController;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable {

    private final int port = 5555;
    private ServerSocket serverSocket;
    ArrayList<PrintWriter> writers;
    private ArrayList<ObjectOutputStream> outputs;
    private ServerController controller;

    public Server(ArrayList<ObjectOutputStream> outputs, ServerController controller){
        this.outputs = outputs;
        this.controller = controller;
    }

//    public void start() {
//
//
//        while (true) {
//            try {
//                clientSocket = serverSocket.accept();
//                System.out.println("Accepted connection : " + clientSocket);
//                Thread t = new Thread(new ClientHandle(clientSocket));
//                t.start();
//            } catch (Exception e) {
//                System.err.println("Error in connection attempt.");
//            }
//        }
//    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
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
