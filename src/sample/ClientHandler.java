package sample;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringTokenizer;
import java.util.Vector;

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

    public void updateServerLog(String update) {
        String formattedDate = getDateAndTime();
        String serverText = serverLog.getText() + formattedDate + "\t\t\t";
        serverLog.setText(serverText + update);
    }
    @Override
    public void run() {

        String formattedDate = getDateAndTime();
        String serverText = serverLog.getText() + formattedDate + "\t\t\t" + this.sock.getRemoteSocketAddress();

        while (true) {
            try {
                // receive message from socket 1
                String data = input.readUTF();
                st = new StringTokenizer(data);
                String type = st.nextToken();

                Vector<ClientHandler> cList = ChatServerGUIController.cList;

                int sendingTo;
                if (name.equals("0"))
                    sendingTo = 1;
                else
                    sendingTo = 0;

                ClientHandler other = cList.get(sendingTo);

                //this block is for the first client 0 means first client\
                if (ChatServerGUIController.cList.size() == 2) {
                    if (type.equals("message")) {
                        String message = "";
                        while (st.hasMoreTokens()) {
                            if (message.equals(""))
                                message = st.nextToken();
                            else
                                message = message + " " + st.nextToken();
                        }

                        try {
                            if (cList.size() == 2) {
                                other.output.writeUTF("message " + message + "\n");
                                updateServerLog(this.sock.getRemoteSocketAddress() + " sent a message to " + other.sock.getRemoteSocketAddress() + "\n");
                            } else {
                                alert("Cannot send to disconnected client");
                                this.output.writeUTF("SERVER: Unable to send message. Other client has disconnected.\n");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        String username = st.nextToken();
                        try {
                            if (cList.size() == 2) {
                                other.output.writeUTF("file " + username + "\n");
                                ObjectInputStream inputFile = new ObjectInputStream(this.sock.getInputStream());
                                ObjectOutputStream outputFile = new ObjectOutputStream(other.sock.getOutputStream());

                                File fileSent = (File) inputFile.readObject();
                                outputFile.writeObject(fileSent);
                                outputFile.flush();

                                updateServerLog(this.sock.getRemoteSocketAddress() + " sent a file to " + other.sock.getRemoteSocketAddress() + "\n");
                            } else {
                                alert("Cannot send to disconnected client");
                                this.output.writeUTF("SERVER: Unable to send message. Other client has disconnected.\n");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    alert("Cannot send to disconnected client");
                    this.output.writeUTF("SERVER: Unable to send message. Other client has disconnected.\n");
                    updateServerLog("Client tried to send message to disconnected client\n");
                }
            } catch (Exception e) {
                //Remove a list if 2
                if (ChatServerGUIController.cList.size() == 2)
                    ChatServerGUIController.cList.removeElementAt(Integer.parseInt(name));
                else
                    ChatServerGUIController.cList.removeElementAt(0);
                ChatServerGUIController.i = Integer.parseInt(name);
                System.out.println("Client handler break " + ChatServerGUIController.cList.size());
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
            updateServerLog("Server: Client " + sock.getRemoteSocketAddress() + " disconnected\n");
        }
    }
}