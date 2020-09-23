package sample;

import javafx.scene.control.TextArea;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ClientMessages implements Runnable{
    Socket s;
    TextArea messageLog;

    public ClientMessages(Socket s, TextArea messageLog){
        this.s = s;
        this.messageLog = messageLog;
    }

    @Override
    public void run() {
        String received;
        String toreturn;
        while (true)
        {
            try {
                // receive the answer from client
                DataInputStream dis = new DataInputStream(s.getInputStream());
                received = dis.readUTF();
                messageLog.setText(messageLog.getText()+"\n"+received);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
