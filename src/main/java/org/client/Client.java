package org.client;

import java.net.Socket;
import java.util.Scanner;

public class Client {
    public Client(){

    }
    public static void home() throws Exception {
        Scanner sc = new Scanner(System.in);
        do{
            System.out.println("Welcome to chat app by viral");
            System.out.println(" To Chat with server press - 1");
            System.out.println(" To  join Group Chat  press - 2");
            int choice = sc.nextInt();
            switch(choice){
                case 1 :

                    new Client1();
                    break;

                case 2:
                    System.out.println("enter your user name for the groupchat:");
                    String username = sc.nextLine();

                    Socket socket = new Socket("localhost",1234);
                    Client2 client = new Client2(socket,username);
                    client.listenMessage();
                    client.sendMessage();
            }
        }while (true);

    }
}
