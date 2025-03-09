package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadedServerUsingServerSocket {

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newCachedThreadPool();

        try (ServerSocket serverSocket = new ServerSocket(4000)) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Server accepts client connection");
                System.out.println("Client connected from IP: " + socket.getInetAddress() + ", Port: " + socket.getPort());
                socket.setSoTimeout(900_000);
                executorService.submit(() -> handleClientRequest(socket));
            }
        } catch (IOException ioException) {
            System.out.println("Server exception: " + ioException.getMessage());
        }
    }
    private static void handleClientRequest(Socket socket) {
        try (socket;
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
        ) {
            while (true) {
                String echoString = input.readLine();
                System.out.println("Server got request data: " + echoString);
                if (echoString == null || echoString.equals("exit")) {
                    break;
                }
                output.println("Echo from server: " + echoString);
            }
        } catch (IOException ioException) {
            System.out.println("Client socket shut down here");
        }
    }
}
