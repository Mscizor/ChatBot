package sample;

import javafx.scene.control.TextArea;

import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;

public class ClientMessages implements Runnable{
    Socket s;
    TextArea messageLog;
    boolean connected;

    DataInputStream dis;
    DataOutputStream dos;
    StringTokenizer st;

    public void setConnected(Boolean b){
        connected = b;
        System.out.println("Set connected to " + connected + " successfully");
    }

    public boolean getConnected(){
        return connected;
    }

    public ClientMessages(Socket s, TextArea messageLog){
        this.s = s;
        this.messageLog = messageLog;
        connected = true;

        try {
            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * This method receives a message from the server and appends the message to the text area
     */
    @Override
    public void run() {
            try{
                while(true){
                    String data = dis.readUTF();
                    messageLog.setText(messageLog.getText() + data + "\n");
                }
            }
            catch(IOException ex) {
                System.out.println("Client messages READUTF " + s.getRemoteSocketAddress());
                //ex.printStackTrace();
            }
        try {
            s.close();
            System.out.println(s.getRemoteSocketAddress() + " Closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

