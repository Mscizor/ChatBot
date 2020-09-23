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
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServerGUIController{

    private ChatServerModel chatServerModel;
    private ArrayList<String> log = new ArrayList<String>();
    private Boolean connected = true;
    private ServerSocket serverSocket;
    private Socket serverEndpointOne;
    private Socket serverEndpointTwo;

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

    public String getLog() {
        String s = "";
        for (int i=0;i<this.log.size();i++) {
            s = s + this.log.get(i);
            s = s + "\n";
        }
        return s;
    }

    public void addLog(String newLog) {
        this.log.add(newLog);
    }

    public void initialize(ChatServerModel chatServerModel) {
        this.chatServerModel = chatServerModel;
        labelPort.setText(chatServerModel.getPortNumber());

        int nPort = Integer.parseInt(chatServerModel.getPortNumber());
        //Connect to server socket
        try {
            serverSocket = new ServerSocket(nPort);
            System.out.println("Server: Listening on port " + nPort + "...");
            serverLog.setText("Server: Listening on port " + nPort + "...");
        }
        catch(IOException e) {
            e.printStackTrace();
        }

            serverEndpointOne = null;

            try {
                //accepts first client
                serverEndpointOne = serverSocket.accept();
                System.out.println("A new client is connected : " + serverEndpointOne);
                serverLog.setText(serverLog.getText() + "\nA new client is connected : " + serverEndpointOne.getRemoteSocketAddress());

                //accepts second client
                serverEndpointTwo = serverSocket.accept();
                System.out.println("A new client is connected : " + serverEndpointTwo);
                serverLog.setText(serverLog.getText() + "\nA new client is connected : " + serverEndpointTwo.getRemoteSocketAddress());

                System.out.println("Assigning new thread for this client");

                // create a new thread object
                ClientHandler c1 = new ClientHandler(serverEndpointOne, serverEndpointTwo, serverLog);
                Thread t1 = new Thread(c1);

                ClientHandler c2 = new ClientHandler(serverEndpointTwo, serverEndpointOne, serverLog);
                Thread t2 = new Thread(c2);

                // Invoking the start() method
                t1.start();
                t2.start();

            } catch (IOException e) {
                try {
                    serverEndpointOne.close();
                    serverEndpointTwo.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }


        disconnect.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    connected = false;
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