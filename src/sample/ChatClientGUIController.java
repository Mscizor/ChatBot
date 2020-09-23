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

import java.io.DataOutputStream;
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
    private Button save;
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
    This method saves the log
    */
    @FXML
    public void save(){

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

        try {
            // create a new thread object
            ClientMessages c1 = new ClientMessages(clientEndpoint, messageLog);
            Thread t1 = new Thread(c1);
            t1.start();
        }
        catch(Exception e){
            e.printStackTrace();
        }

        exit.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Exit clicked");
                try {
                    switchSceneToChatClient("ChatClient.fxml");

                } catch (IOException s) {
                    s.printStackTrace();
                }
            }
        });

        save.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Save clicked");
            }
        });

        file.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("File clicked");
            }
        });

        send.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    DataOutputStream dos1 = new DataOutputStream(clientEndpoint.getOutputStream());
                    dos1.writeUTF( "\n" + chatClientModel.getUserName() + ": " + textMessage.getText());
                    messageLog.setText(messageLog.getText() + "\n" + chatClientModel.getUserName() + ": " + textMessage.getText());
                    System.out.println("Send clicked");
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