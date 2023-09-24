package org.servers;

import java.net.ServerSocket;
import java.net.Socket;

public class grpchat_Server {
    private ServerSocket serverSocket;

    public grpchat_Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        System.out.println("grpchat server is started");
    }

    public void startServer() {
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("new client is connected");
                ClientHandler clienthandler = new ClientHandler(socket);
                Thread thread = new Thread(clienthandler);
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    public static void main(String[] args) throws Exception{

//        ServerSocket server = new ServerSocket(7777);
//        new Server(server);
        ServerSocket serverSocket = new ServerSocket(1234);
        grpchat_Server server1 = new grpchat_Server(serverSocket );
        server1.startServer();

    }
}


