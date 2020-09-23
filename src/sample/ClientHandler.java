package sample;

import javafx.scene.control.TextArea;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

                //get date and time for timestamp
                LocalDateTime myDateObj = LocalDateTime.now();
                DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                String formattedDate = myDateObj.format(myFormatObj);

                //set serverLog text area
                serverLog.setText(serverLog.getText() + formattedDate + "\t\t\t" + s1.getRemoteSocketAddress() + " sent a message to " + s2.getRemoteSocketAddress() + "\n");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
