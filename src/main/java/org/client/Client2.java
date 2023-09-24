package org.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client2 {
    private Socket socket ;


    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username ;

    public Client2(Socket socket ,String username) {
        try{
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;}catch(Exception e){
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }
    public void sendMessage(){
        try{
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            Scanner sc = new Scanner(System.in);
            while(socket.isConnected()){
                String messageToSend = sc.nextLine();
                bufferedWriter.write(username+":"+messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }catch(Exception e){
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }
    public void listenMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String messageFromChat;
                while (socket.isConnected()){

                    try{
                        messageFromChat = bufferedReader.readLine();
                        System.out.println(messageFromChat);
                    }catch (Exception e){
                        closeEverything(socket,bufferedReader,bufferedWriter);
                    }

                }}
        }).start();
    }
    public void closeEverything(Socket socket,BufferedReader bufferedReader, BufferedWriter bufferedWriter){

        try {
            if(bufferedReader!= null){
                bufferedReader.close();
            }
            if(bufferedWriter != null){
                bufferedWriter.close();
            }
            if (socket !=null){
                socket.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
