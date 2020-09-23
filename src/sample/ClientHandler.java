package sample;

import javafx.scene.control.TextArea;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringTokenizer;

public class ClientHandler implements Runnable{
    Socket s1;
    Socket s2;
    TextArea serverLog;
    
    StringTokenizer st;
    DataInputStream dis1;
    public ClientHandler(Socket s1, Socket s2, TextArea serverLog){
        this.s1 = s1;
        this.s2 = s2;
        this.serverLog = serverLog;

        try {
            dis1 = new DataInputStream(s1.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                // receive message from socket 1
                String data = dis1.readUTF();
                System.out.println("ClientHandler: " + data);
                st = new StringTokenizer(data);
                String type = st.nextToken();

                //get date and time for timestamp
                LocalDateTime myDateObj = LocalDateTime.now();
                DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                String formattedDate = myDateObj.format(myFormatObj);

                if (type.equalsIgnoreCase("message")) {
                    String message = "";
                    while (st.hasMoreTokens()) {
                        if (message.equals(""))
                            message = st.nextToken();
                        else
                            message = message + " " + st.nextToken();
                    }
                    try {
                        DataOutputStream dos2 = new DataOutputStream(s2.getOutputStream());
                        dos2.writeUTF("message " + message);
                        serverLog.setText(serverLog.getText() + formattedDate + "\t\t\t" + s1.getRemoteSocketAddress() + " sent a message to " + s2.getRemoteSocketAddress() + "\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    String name = st.nextToken();
                    String size = st.nextToken();
                    try {
                        DataOutputStream dos2 = new DataOutputStream(s2.getOutputStream());
                        dos2.writeUTF("file " + name + " " + size);
                        InputStream inputFile = s1.getInputStream();
                        OutputStream sendFile = s2.getOutputStream();
                        byte[] byteArray = new byte[100];
                        int current;
                        while ((current = inputFile.read(byteArray)) > 0) {
                            sendFile.write(byteArray, 0, current);
                        }
                        sendFile.flush();
                        sendFile.close();
                        serverLog.setText(serverLog.getText() + formattedDate + "\t\t\t" + s1.getRemoteSocketAddress() + " sent a file to " + s2.getRemoteSocketAddress() + "\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
