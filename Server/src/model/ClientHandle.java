package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientHandle implements Runnable{

    private Socket clientSocket;
    private BufferedReader in = null;
    public ClientHandle(Socket client) {
        this.clientSocket = client;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String clientRequest;
            while ((clientRequest = in.readLine()) != null){
                if (clientRequest == "receive"){
                    //re
                }
                else if (clientRequest == "send"){

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void reciveFile(){

    }
    public void sendFileList(){

    }
}
