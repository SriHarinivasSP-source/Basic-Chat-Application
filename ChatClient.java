import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             Scanner scanner = new Scanner(System.in)) {

            System.out.print("Enter your name: ");
            String name = scanner.nextLine();

            new Thread(() -> {
                try {
                    while (true) {
                        Message message = (Message) in.readObject();
                        System.out.println(message);
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }).start();

            while (true) {
                String content = scanner.nextLine();
                out.writeObject(new Message(name, content));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
