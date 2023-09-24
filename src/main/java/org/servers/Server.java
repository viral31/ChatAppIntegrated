package org.servers;

import org.jetbrains.annotations.NotNull;
import java.util.concurrent.TimeUnit;
import java.net.*;
import java.io.*;
public class Server
{
    ServerSocket server ;
    Socket socket ;
    BufferedReader br;
    PrintWriter out ;
    public Server(ServerSocket server){
        try {
            this.server = server;
            System.out.println("server is ready to accept connections");
            System.out.println("waiting.......");

            socket = server.accept();
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String msg =br.readLine();
            String result = chatGPT(msg);

            out = new PrintWriter(socket.getOutputStream());
            out.println(result);
                out.flush();
//            startReading();
//            startWriting();




        } catch(Exception e){

            e.printStackTrace();
            try{
            socket.close();}catch (Exception ex){
                e.printStackTrace();
            }
        }


    }
//    public void startReading(){
//        Runnable r1 =()->{
//            System.out.println("reader started......");
//            try{
//                while(true){
//
//                    String msg =br.readLine();
//
//                    if (msg.equals("stop")){
//                        System.out.println("client terminated the chat ");
//
//                        break;
//
//                    }
//                    System.out.println("user :"+ msg);
//                }
//            } catch(Exception e){
//                //e.printStackTrace();
//                System.out.println("connection closed");
//            }
//
//        };
//        new Thread(r1).start();
//    }
//    public void startWriting(){
//        Runnable r2 =()->{
//            System.out.println("writer started......");
//            try{
//                while(!socket.isClosed()){
//
//                    BufferedReader br1 = new BufferedReader( new InputStreamReader(System.in));
//                    String content = br1.readLine();
//                    out.println(content);
//                    out.flush();
//                    if(content.equals("stop")){
//                        socket.close();
//                        break;
//                    }
//
//                }
//                System.out.println("connection closed");
//            } catch(Exception e){
//                e.printStackTrace();
//
//            }
//        };
//        new Thread(r2).start();
//    }

    public static @NotNull String chatGPT(String message) {
        String url = "https://api.openai.com/v1/chat/completions";
        String apiKey ="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";
        String model = "gpt-3.5-turbo";

        try {

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
          con.setRequestProperty("Authorization", "Bearer " + apiKey);
            con.setRequestProperty("Content-Type", "application/json");


            String body = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + message + "\"}]}";
           con.setDoOutput(true);
          PrintWriter writer = new PrintWriter(con.getOutputStream());
            writer.write(body);

            writer.close();
            int responseCode = con.getResponseCode();
            if (responseCode == 429)  {
                // Rate limit exceeded, wait for a while and retry
                int retryAfter = con.getHeaderFieldInt("Retry-After", 10); // Default to 10 seconds if no Retry-After header
               try{ TimeUnit.SECONDS.sleep(retryAfter);} catch(Exception e){e.printStackTrace();}
                return chatGPT(message); // Retry the request
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine())!= null) {
                response.append(inputLine);
            }
            in.close();

            // returns the extracted contents of the response.
            return extractContentFromResponse(response.toString());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static String extractContentFromResponse(String response) {
        int startMarker = response.indexOf("content")+11;
        int endMarker = response.indexOf("\"", startMarker);
        return response.substring(startMarker, endMarker);
    }
    public static void main(String[] args) throws Exception{

        ServerSocket server = new ServerSocket(7777);
        new Server(server);
//        ServerSocket serverSocket = new ServerSocket(1234);
//        grpchat_Server server1 = new grpchat_Server(serverSocket );
//        server1.startServer();

    }
}

