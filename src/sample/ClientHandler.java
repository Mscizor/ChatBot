package sample;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringTokenizer;

import static java.lang.Thread.sleep;

public class ClientHandler implements Runnable {
    public Socket sock;
    public String name;
    public DataInputStream input;
    public DataOutputStream output;
    TextArea serverLog;

    StringTokenizer st;

    //public ClientHandler(Socket s1, Socket s2, TextArea serverLog){
    public ClientHandler(Socket s, String n, DataInputStream i, DataOutputStream o, TextArea serverLog) {
        this.sock = s;
        this.name = n;
        this.input = i;
        this.output = o;
        this.serverLog = serverLog;
    }

    /**
     * Alerts the user with the given string
     */
    public void alert(String s) {
        Alert fail = new Alert(Alert.AlertType.INFORMATION);
        fail.setHeaderText("Error");
        fail.setContentText(s);
        fail.showAndWait();
    }

    /**
     * returns a formatted string of date dd-mm-yyyy hh:mm:ss
     */
    public String getDateAndTime() {
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDate = myDateObj.format(myFormatObj);
        return formattedDate;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // receive message from socket 1
                String data = null;
                try {
                    data = input.readUTF();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //this block is for the first client 0 means first client
                if (name.equals("0")) {
                    if (ChatServerGUIController.clist.size() == 2) {
                        String formattedDate = getDateAndTime();
                        try {
                            ChatServerGUIController.clist.get(1).output.writeUTF(data);
                            //1 represents the second client
                            serverLog.setText(serverLog.getText() + formattedDate + "\t\t\t" + sock.getRemoteSocketAddress() + " sent a message to " + ChatServerGUIController.clist.get(1).sock.getRemoteSocketAddress() + "\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        //this.output.writeUTF("SERVER: Unable to send message. Other client has disconnected.\n");
                        alert("Cannot send to disconnected client");
                        String formattedDate = getDateAndTime();
                        serverLog.setText(serverLog.getText() + formattedDate + "\t\t\tClient tried to send message to disconnected client\n");
                    }
                }
                else { //this block is for the second client 1 means second client
                    if (ChatServerGUIController.clist.size() == 2) {
                        String formattedDate = getDateAndTime();
                        try {
                            ChatServerGUIController.clist.get(0).output.writeUTF(data);
                            serverLog.setText(serverLog.getText() + formattedDate + "\t\t\t" + sock.getRemoteSocketAddress() + " sent a message to " + ChatServerGUIController.clist.get(0).sock.getRemoteSocketAddress() + "\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        alert("Cannot send to disconnected client");
                        //this.output.writeUTF("SERVER: Unable to send message. Other client has disconnected.\n");
                    }
                }
            } catch (Exception e) {
                //Remove a list if 2
                if (ChatServerGUIController.clist.size() == 2)
                    ChatServerGUIController.clist.removeElementAt(Integer.parseInt(name));
                else
                    ChatServerGUIController.clist.removeElementAt(0);
                ChatServerGUIController.i = Integer.parseInt(name);
                break;
            }
        }
        try {
            //Close streams
            this.input.close();
            this.output.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            String formattedDate = getDateAndTime();
            serverLog.setText(serverLog.getText()+ formattedDate + "\t\t\tServer: Client " + sock.getRemoteSocketAddress() + " disconnected\n");
        }
    }
}