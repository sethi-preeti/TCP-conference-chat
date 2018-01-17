
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author dell
 */
public class server {
   static int counter=0;
   static ArrayList outputstreams = new ArrayList();
  //Socket client = null;
  //DataInputStream  input = null;
  DataInputStream in = null;
  DataOutputStream out = null;

public static void main(String args[]) throws IOException{
    ServerSocket server = new ServerSocket(5000);
    System.out.println("Server Started, Waiting for connections.......");
    while(true){
    Socket client = server.accept();
    counter++;
    System.out.println("Connected to client "+counter);
    cthread t = new cthread(client,counter); //send  the request to a separate thread
    t.start();
    }
}  
} 
class cthread extends Thread{
    Socket client;
    int client_no;
    
    cthread(Socket c, int n){
        client = c;
        client_no = n;
    }
    public void run(){
        try {
            DataInputStream in = new DataInputStream(client.getInputStream());
            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            server.outputstreams.add(out);
            out.writeInt(client_no);
            out.flush();
            String msg="";
            String tosend ="";
            while(!msg.equals("bye")){
                msg = in.readUTF();
                tosend = "Client "+client_no+": "+msg;
                for(int i=0;i<server.outputstreams.size();i++){
             if(i!=client_no-1){
                 out =(DataOutputStream)server.outputstreams.get(i);
                 out.writeUTF(tosend);
             }
                out.flush();
            }
            }
            in.close();
            out.close();
            client.close();
        } catch (IOException ex) {
            Logger.getLogger(server.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
      System.out.println("Client " + client_no + " disconnected.");
      server.outputstreams.remove(client_no-1);
      
    }
    }
}


