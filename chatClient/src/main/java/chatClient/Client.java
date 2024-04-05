package chatClient;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private  final Socket socket;

    private  final  String name;

    private boolean privateMessaging;

    private  BufferedWriter bufferedWriter;

    private BufferedReader bufferedReader;

    public Client(Socket socket, String userName)  {
        this.socket = socket;
        name =userName;
        try {

            //потоки запустили  главные  на чтение и запись

        bufferedWriter= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            }

        catch (IOException e ){
            closeEverything(socket, bufferedReader, bufferedWriter);
            }



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

       public  void  listenForMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String message;
                privateMessaging = false;


                while (socket.isConnected()){
                    try {
                        message= bufferedReader.readLine();
                        if(!message.contains("@")){
                            System.out.println(message);
                        }
                        else{
                            String[] result = message.split(" ");
                            for (int i = 0; i <result.length ; i++) {
                                if(result[i].contains("@")){

                                    String nameForCheck = result[i];
                                     String nameForCheckClear = nameForCheck.substring(1);

                                    if (nameForCheckClear.equals(name)){

                                        privateMessaging = true;
                                         System.out.println(message );
                                     }

                                }
                            }


                        }

                    }
                    catch (IOException e){
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }

                }

            }
        }).start();

       }

    public  void  sendForMessage(){
        try {


            bufferedWriter.write(name);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()) {
                String message = scanner.nextLine();
                bufferedWriter.write(name + ":  " + message);
                bufferedWriter.newLine();
                bufferedWriter.flush();


            }
        }
catch(IOException e){
    closeEverything(socket, bufferedReader, bufferedWriter);

}
    }




}
