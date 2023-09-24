package org.chatapp;
import org.servers.*;

import java.net.ServerSocket;

public class Main {
    public static void main(String[] args) throws Exception{

        ServerSocket server = new ServerSocket(7777);
        new Server(server);
        ServerSocket serverSocket = new ServerSocket(1234);
        grpchat_Server server1 = new grpchat_Server(serverSocket );
        server1.startServer();

}


    }
