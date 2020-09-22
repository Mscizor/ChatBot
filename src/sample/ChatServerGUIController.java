package sample;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServerGUIController{

    private ChatServerModel chatServerModel;

    @FXML
    private Stage stage;

    @FXML
    public TextArea serverLog;
    @FXML
    public Label labelPort;
    @FXML
    public Label labelIPAddress;
    @FXML
    private Button disconnect;

    /*
    This method sends the user to the ChatGUI window.
    */
    @FXML
    public void disconnect(){

    }

    public void initialize(ChatServerModel chatServerModel) {
        this.chatServerModel = chatServerModel;
        labelPort.setText(chatServerModel.getPortNumber());

        int nPort = Integer.parseInt(chatServerModel.getPortNumber());
        ServerSocket serverSocket;
        Socket serverEndpointOne;
        Socket serverEndpointTwo;
        String messageOne;
        String messageTwo;
        try
        {
            serverSocket = new ServerSocket(nPort);
            serverLog.setText("Server: Listening on port " + chatServerModel.getPortNumber() + "...");
            serverEndpointOne = serverSocket.accept();
            serverLog.setText(serverLog.getText() + "\nServer: New client connected: " + serverEndpointOne.getRemoteSocketAddress());

            serverEndpointTwo = serverSocket.accept();
            serverLog.setText(serverLog.getText() + "\nServer: New client connected: " + serverEndpointTwo.getRemoteSocketAddress());
            /*
            DataInputStream disReaderOne = new DataInputStream(serverEndpointOne.getInputStream());
            chatServerModel.setUserNameOne(disReaderOne.readUTF());
            messageOne = disReaderOne.readUTF();

            DataInputStream disReaderTwo = new DataInputStream(serverEndpointTwo.getInputStream());
            usernameTwo = disReaderTwo.readUTF();
            messageTwo = disReaderTwo.readUTF();

            DataOutputStream dosWriterOne = new DataOutputStream(serverEndpointOne.getOutputStream());
            dosWriterOne.writeUTF("Message from " + usernameTwo + ": " + messageTwo);
            DataOutputStream dosWriterTwo = new DataOutputStream(serverEndpointTwo.getOutputStream());
            dosWriterTwo.writeUTF("Message from " + usernameOne + ": " + messageOne);
            */
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        disconnect.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    switchSceneToChatServer("ChatServer.fxml");
                    /*
                    serverEndpointOne.close();
                    serverEndpointTwo.close();
                    */
                    System.out.println("Server: Connection terminated");

                } catch (IOException s) {
                    s.printStackTrace();
                }
            }
        });

    }

    public void switchSceneToChatServer(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxml));
        Parent ChatServer = loader.load();
        Scene scene = new Scene(ChatServer);
        Stage stage = (Stage) disconnect.getScene().getWindow();

        ChatServerController controller = loader.getController();
        controller.initialize(chatServerModel);

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