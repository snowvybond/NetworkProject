package model;

import java.io.BufferedReader;
import java.net.Socket;

public class ClientHandle implements Runnable{

    private Socket clientSocket;
    private BufferedReader in = null;
    public ClientHandle(Socket client) {
        this.clientSocket = client;
    }

    @Override
    public void run() {

    }
}
