package sample;

import javafx.scene.control.TextArea;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ClientMessages implements Runnable{
    Socket s;
    TextArea messageLog;
    boolean connected;

    private void setConnected(Boolean b){
        connected = b;
    }

    private boolean getConnected(){
        return connected;
    }

    public ClientMessages(Socket s, TextArea messageLog){
        this.s = s;
        this.messageLog = messageLog;
        connected = true;
    }

    @Override
    public void run() {
        String received;
        while (connected)
        {
            try {
                // receive the answer from client
                DataInputStream dis = new DataInputStream(s.getInputStream());
                received = dis.readUTF();
                messageLog.setText(messageLog.getText()+received);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
