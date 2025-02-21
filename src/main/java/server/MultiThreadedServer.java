package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadedServer {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        try (ServerSocket serverSocket = new ServerSocket(4000)) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected from IP: " + socket.getInetAddress() + ", Port: " + socket.getPort());
                socket.setSoTimeout(900_0000); // drop client connection after 15 minutes of inactivity
                executorService.submit(() -> handleClientRequest(socket));
            }
        } catch (IOException ioException) {
            System.out.println("Server exception: " + ioException.getMessage());
        }
    }

    private static void handleClientRequest(Socket socket) {
        try(socket;
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);) {
            while (true) {
                String echoString = input.readLine();
                System.out.println("Request from client: " + socket.getInetAddress() + ", Port: " + socket.getPort() + " - " + echoString);
                if (echoString == null || echoString.equals("exit")) {
                    break;
                }
                output.println("Server echo: " + echoString);
            }
        } catch (IOException ioException) {
            System.out.println("Client exception: " + ioException.getMessage());
        } finally {
            System.out.println("Client disconnected");
        }
    }
}