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

    @Override
    public void run() {
        try {
            System.out.println("In of Client Messages");
            while (connected) {
                String data = dis.readUTF();
                st = new StringTokenizer(data);
                String type = st.nextToken();

                if (type.equalsIgnoreCase("message")) {
                    String message = "";
                    while (st.hasMoreTokens()) {
                        if (message.equals(""))
                            message = st.nextToken();
                        else
                            message = message + " " + st.nextToken();
                    }
                    messageLog.setText(messageLog.getText() + message + "\n");
                }
                else {
                    String directory = "C:\\Users\\User\\Pictures\\downloaded.png"; // ask client to select directory
                    String name = st.nextToken();
                    int size = Integer.parseInt(st.nextToken());

                    FileOutputStream fos = new FileOutputStream(directory);
                    InputStream input = s.getInputStream();
                    BufferedInputStream bis = new BufferedInputStream(input);

                    byte [] byteArray = new byte[100];
                    int current = 0;
                    while ((current = bis.read(byteArray)) != -1) {
                        for (int i = 0; i < byteArray.length; i++) {
                            System.out.printf("%d: %c", i, (char) byteArray[i]);
                        }
                        System.out.println();
                        fos.write(byteArray, 0, current);
                    }
                    fos.flush();
                    fos.close();
                    messageLog.setText(messageLog.getText() + "File was received.\n");
                }
            }
        } catch (IOException e) {
            connected = false;
            //Close the socket
            try {
                s.close();
            } catch (IOException ex) {
                connected = false;
                ex.printStackTrace();
            }
            e.printStackTrace();
            System.out.println("out of Client Messages");
        }}
    }

