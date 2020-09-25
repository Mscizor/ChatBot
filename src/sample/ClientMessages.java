package sample;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Optional;
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
        try {
            while(true){
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
                    String sender = st.nextToken();
                    ObjectInputStream input = new ObjectInputStream(s.getInputStream());
                    File fileReceived = (File) input.readObject();
                    String name = fileReceived.getName();
                    Platform.runLater(() -> {
                        Stage stage = (Stage) messageLog.getScene().getWindow();
                        DirectoryChooser dc = new DirectoryChooser();
                        File path = dc.showDialog(stage);
                        Optional<String> filename;
                        if (path != null) {
                            TextInputDialog tid = new TextInputDialog(name);
                            tid.setTitle("File Name Chooser");
                            tid.setHeaderText("File Name");
                            tid.setContentText("Enter file name:");
                            filename = tid.showAndWait();
                            if (filename.isPresent()) {
                                String directory = path.getPath() + "\\" + filename.get();
                                System.out.println("Directory: " + directory);
                                try {
                                    FileOutputStream fos = new FileOutputStream(directory);
                                    System.out.println("after");
                                    fos.write(Files.readAllBytes(fileReceived.toPath()));
                                    System.out.println("Before flush");
                                    fos.flush();
                                    messageLog.setText(messageLog.getText() + "File from " + sender + ": Received successfully.\n");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                messageLog.setText(messageLog.getText() + "File from " + sender + ": Receiving canceled, no filename.\n");
                            }
                        } else {
                            messageLog.setText(messageLog.getText() + "File from " + sender + ": Receiving canceled, no directory.\n");
                        }
                    });
                }
            }

        }
        catch(Exception e) {
            System.out.println("Client messages READUTF " + s.getRemoteSocketAddress());
            e.printStackTrace();
        }

        try {
            s.close();
            System.out.println(s.getRemoteSocketAddress() + " Closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

