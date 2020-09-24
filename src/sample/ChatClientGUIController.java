package sample;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatClientGUIController{

    private ChatClientModel chatClientModel;
    Socket clientEndpoint=null;

    @FXML
    private Stage stage;
    @FXML
    private Button exit;
    @FXML
    private Button file;
    @FXML
    private Button send;
    @FXML
    private TextField textMessage;
    @FXML
    private TextArea messageLog;

    /*
    This method sends the user to the Login window.
    */
    @FXML
    public void exit(){

    }

    /*
    This method sends the file to the other user
    */
    @FXML
    public void file(){

    }

    /*
    This method sends the message to the other user
    */
    @FXML
    public void send(){

    }

    public void initialize(ChatClientModel chatClientModel) {
        this.chatClientModel = chatClientModel;

        String sServerAddress = chatClientModel.getIpAddress();
        int nPort = Integer.parseInt(chatClientModel.getPortNumber());
        try {
            clientEndpoint = new Socket(sServerAddress, nPort);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        ClientMessages c1 = new ClientMessages(clientEndpoint, messageLog);

        try {
            // create a new thread object
            Thread t1 = new Thread(c1);
            t1.start();
        }
        catch(Exception e){
            e.printStackTrace();
        }

        exit.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    //Close the socket of client (clientEndpoint) (.setConnected(false))
                    //DataOutputStream dos1 = new DataOutputStream(clientEndpoint.getOutputStream());
                    //dos1.writeUTF("exit");
                    //c1.setConnected(false);
                    clientEndpoint.close();
                    switchSceneToChatClient("ChatClient.fxml");

                } catch (IOException s) {
                    s.printStackTrace();
                }
            }
        });

        file.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

                int returnValue = jfc.showOpenDialog(null);
                // int returnValue = jfc.showSaveDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = jfc.getSelectedFile();
                    int len = (int) selectedFile.length();
                    int filesize = (int) Math.ceil(len / 100);
                    String name = selectedFile.getName();

                    try {
                        DataOutputStream dos1 = new DataOutputStream(clientEndpoint.getOutputStream());
                        dos1.writeUTF("file" + " " + name.replace(" ", "_") + " " + len);

                        InputStream input = new FileInputStream(selectedFile);
                        OutputStream output = clientEndpoint.getOutputStream();
                        BufferedInputStream bis = new BufferedInputStream(input);

                        byte [] byteArray = new byte[100];
                        int count;
                        while ((count = bis.read(byteArray)) > 0) {
                            output.write(byteArray, 0, count);
                        }
                        output.flush();
                        output.close();
                        messageLog.setText(messageLog.getText() + chatClientModel.getUserName() + ": Sent a file\n");
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("File clicked");
            }
        });

        send.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    //Send message to server
                    DataOutputStream dos1 = new DataOutputStream(clientEndpoint.getOutputStream());
                    dos1.writeUTF(chatClientModel.getUserName() + ": " + textMessage.getText());
                    //Appends message to messageLog
                    messageLog.setText(messageLog.getText() + chatClientModel.getUserName() + ": " + textMessage.getText() + "\n");
                }
                catch(IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void switchSceneToChatClient(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxml));
        Parent login = loader.load();
        Scene scene = new Scene(login);
        Stage stage = (Stage) exit.getScene().getWindow();

        ChatClientController controller = loader.getController();
        controller.initialize(chatClientModel);

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }
}