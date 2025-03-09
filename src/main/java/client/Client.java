package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {

         try (Socket socket = new Socket("localhost", 4000)) {
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

             Scanner scanner = new Scanner(System.in);
             String requestString;
             String responseString;
             do {
                 System.out.println("Enter string to be echoed (sent to server): ");
                 requestString = scanner.nextLine();

                 output.println(requestString);
                 if (!requestString.equals("exit")) {
                     responseString = input.readLine();
                     System.out.println("Server response: " + responseString);
                 }
             } while (!requestString.equals("exit"));
         } catch (IOException exception) {
             System.out.println("Client Error: " + exception.getMessage());
         } finally {
             System.out.println("Client Disconnected");
         }
    }

}
