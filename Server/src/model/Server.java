package model;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private final int port = 5555;
    private static ServerSocket serverSocket;
    private static Socket clientSocket = null;

    public void setUp() {

        try {
            serverSocket = new ServerSocket(4444);
            System.out.println("Server started.");
        } catch (Exception e) {
            System.err.println("Port already in use.");
            System.exit(1);
        }

        while (true) {
            try {
                clientSocket = serverSocket.accept();
                System.out.println("Accepted connection : " + clientSocket);

                Thread t = new Thread(new ClientHandle(clientSocket));

                t.start();

            } catch (Exception e) {
                System.err.println("Error in connection attempt.");
            }
        }
    }

}
