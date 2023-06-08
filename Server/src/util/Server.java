package util;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private static List<MultiClientHandler> clients = new ArrayList<>();
    public static void main(String[] args) {
        final int port = 3000;

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server started. Listening on port " + port);

            for (int i = 0; i < 5; i++) {
                Socket clientSocket = serverSocket.accept();

                DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
                String clientName = dataInputStream.readUTF();
                System.out.println("Client connected: " + clientName);

                MultiClientHandler clientHandler = new MultiClientHandler(clientSocket,clientName);
                clients.add(clientHandler);
                clientHandler.start();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void broadcastMessage(String name,String message) {
        for (MultiClientHandler client : clients) {
            if (!client.userName.equals(name)){
                client.sendMessage(message);
            }
        }
    }

    public static void broadcastImage(String name, File file) {
        for (MultiClientHandler client : clients) {
            if (!client.userName.equals(name)){
                client.sendImages(file);
            }
        }
    }


}
