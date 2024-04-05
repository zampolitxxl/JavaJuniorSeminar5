package chatServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server

{
    private  final ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public  void  runServer(){
        while (!serverSocket.isClosed()){
            try {
                //не понял
                Socket socket= serverSocket.accept();
                //не понял
                ClientManager clientManager = new ClientManager(socket);

                System.out.println("подключен новый клиент!");
                Thread thread = new Thread(clientManager);
                thread.start();
            }
            catch (IOException e){

closeSocket();            }


        }

}

private void closeSocket(){
        try {
            if (serverSocket != null) serverSocket.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
}
}
