import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final List<ObjectOutputStream> clients = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Chat Server started...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: " + socket.getInetAddress());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                clients.add(out);
                new Thread(() -> handleClient(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket socket) {
        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            while (true) {
                Message message = (Message) in.readObject();
                System.out.println(message);
                broadcast(message);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Client disconnected.");
        }
    }

    private static void broadcast(Message message) {
        for (ObjectOutputStream client : clients) {
            try {
                client.writeObject(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
