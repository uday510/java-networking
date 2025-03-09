package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Server {

    private static final int PORT = 4000;
    private static final int PACKET_SIZE = 1024;

    public static void main(String[] args) {

        try (DatagramSocket serverSocket = new DatagramSocket(PORT)) {

            byte[] buffer = new byte[PACKET_SIZE];
            System.out.println("Waiting for client to connect...");
            DatagramPacket clientPacket = new DatagramPacket(buffer, buffer.length);
            serverSocket.receive(clientPacket);
            String audioFileName = new String(buffer, 0, clientPacket.getLength());
            System.out.println("Client requested to listen to: " + audioFileName);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
