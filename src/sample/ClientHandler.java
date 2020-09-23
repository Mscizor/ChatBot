package sample;

import javafx.scene.control.TextArea;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable{
    Socket s1;
    Socket s2;
    TextArea serverLog;

    public ClientHandler(Socket s1, Socket s2, TextArea serverLog){
        this.s1 = s1;
        this.s2 = s2;
        this.serverLog = serverLog;
    }

    @Override
    public void run() {
        String received;
        String toreturn;
        while (true)
        {
            try {
                DataInputStream dis1 = new DataInputStream(s1.getInputStream());
                DataOutputStream dos2 = new DataOutputStream(s2.getOutputStream());
                // receive message from socket 1
                received = dis1.readUTF();
                //write message to socket 2
                dos2.writeUTF(received);
                serverLog.setText(serverLog.getText()+received);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
