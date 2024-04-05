package chatServer;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientManager implements  Runnable
{

    private final Socket socket;

    private BufferedWriter bufferedWriter;

    private BufferedReader bufferedReader;

    private  String name;
    public final static ArrayList<ClientManager> clents = new ArrayList<>();


    //создается объект , передается сокет с адресом сервера, далее через

    public ClientManager(Socket socket) {
        this.socket = socket;

        try {

            bufferedWriter= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
          name= bufferedReader.readLine();
          clents.add(this);
            System.out.println(name + " подключился к чату");
        }

        catch (IOException e ){
closeEverything(socket, bufferedReader, bufferedWriter);
        }


    }

    @Override
    public void run() {

        String messageFromClient;

        while (socket.isConnected()){
            try {


                messageFromClient = bufferedReader.readLine();
                if(messageFromClient==null){
                    closeEverything(socket, bufferedReader, bufferedWriter);
                    break;
                }
                broadcastMessge(messageFromClient);
            }
            catch (IOException e){
                closeEverything(socket, bufferedReader, bufferedWriter);
            }

        }

    }

    private void broadcastMessge(String message){
        for (ClientManager client: clents){
            try {
                if (!client.name.equals(name)){
                    client.bufferedWriter.write(message);
                    client.bufferedWriter.newLine();
                    client.bufferedWriter.flush();

                }

            }
            catch (IOException e){
                closeEverything(socket,bufferedReader,bufferedWriter);
                break;
            }

        }
    }

    private  void  removeClient(){
        clents.remove(this);
        System.out.println(name + "покинул чат");
    }

    private  void  closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){

        try {
            if (bufferedReader != null) {
                bufferedReader.close();

            }
            if (bufferedWriter != null) {
                bufferedWriter.close();

            }
            if (socket != null){
                socket.close();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}

