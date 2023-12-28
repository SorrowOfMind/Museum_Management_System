package com.museum.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 5000;
    public static void main(String[] args) {
      try {
          ServerSocket serverSocket = new ServerSocket(PORT);
          System.out.println("Server is running...");

          while(true) {
              Socket clientSocket = serverSocket.accept();

              Thread clientThread = new Thread(new ClientHandler(clientSocket));
              clientThread.start();
          }
      } catch (IOException e) {
          e.printStackTrace();
          throw new RuntimeException(e);
      }
    }
}
