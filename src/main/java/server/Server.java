package server;

import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(4000)) {

            try (Socket socket = serverSocket.accept()) {
                System.out.println("Client connected from IP: " + socket.getInetAddress() + ", Port: " + socket.getPort());
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

                while (true) {
                    String echoString = input.readLine();
                    System.out.println("Client request: " + echoString);
                    if (echoString == null || echoString.equals("exit")) {
                        break;
                    }
                    output.println("Server echo: " + echoString);
                }
            }
        } catch (IOException ioException) {
            System.out.println("Server exception: " + ioException.getMessage());
        }
    }
}