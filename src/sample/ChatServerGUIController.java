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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ChatServerGUIController{

    private ChatServerModel chatServerModel;
    private String log;
    private Boolean connected = true;
    private ServerSocket serverSocket;
    private Socket serverEndpointOne = null;
    private Socket serverEndpointTwo = null;

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
    @FXML
    private Button saveLog;

    /*
    This method sends the user to the ChatGUI window.
    */
    @FXML
    public void disconnect(){

    }

    /*
    This method save the server log
    */
    @FXML
    public void saveLog(){

    }

    public String getLog() {
        return log;
    }

    public void addLog(String newLog) {
        this.log = log;
    }

    /*
    Returns a string of Date and Time
    */
    public String getDateAndTime() {
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDate = myDateObj.format(myFormatObj);
        return formattedDate;
    }

    public void initialize(ChatServerModel chatServerModel) {
        this.chatServerModel = chatServerModel;
        labelPort.setText(chatServerModel.getPortNumber());

        int nPort = Integer.parseInt(chatServerModel.getPortNumber());
        //Connect to server socket
        try {
            serverSocket = new ServerSocket(nPort);
            //get date and time for timestamp
            String formattedDate = getDateAndTime();
            serverLog.setText(formattedDate + "\t\t\t" +"Server: Listening on port " + nPort + "...");
        }
        catch(IOException e) {
            e.printStackTrace();
        }

            //Thread for accepting clients
            Runnable runnable = () -> {
                try {
                    //accepts first client
                    serverEndpointOne = serverSocket.accept();
                    //get date and time for timestamp
                    String formattedDate = getDateAndTime();
                    serverLog.setText(serverLog.getText() + "\n" + formattedDate + "\t\t\t" + "A new client is connected : " + serverEndpointOne.getRemoteSocketAddress());

                    //accepts second client
                    serverEndpointTwo = serverSocket.accept();
                    //get date and time for timestamp
                    formattedDate = getDateAndTime();
                    serverLog.setText(serverLog.getText() + "\n" + formattedDate + "\t\t\t" + "A new client is connected : " + serverEndpointTwo.getRemoteSocketAddress() + "\n");
                    // create a new thread object
                    ClientHandler c1 = new ClientHandler(serverEndpointOne, serverEndpointTwo, serverLog);
                    ClientHandler c2 = new ClientHandler(serverEndpointTwo, serverEndpointOne, serverLog);
                    Thread t1 = new Thread(c1);
                    Thread t2 = new Thread(c2);
                    t1.start();
                    t2.start();
                    Boolean connected = true;
                    while (true) {
                        //if both endpoints are null
                        if (serverEndpointOne == null && serverEndpointTwo == null) {
                            System.out.println("BOTH");

                            //accepts first client
                            System.out.println("both accept");
                            serverEndpointOne = serverSocket.accept();
                            //get date and time for timestamp
                            formattedDate = getDateAndTime();
                            serverLog.setText(serverLog.getText() + "\n" + formattedDate + "\t\t\t" + "A new client is connected : " + serverEndpointOne.getRemoteSocketAddress());

                            //accepts second client
                            serverEndpointTwo = serverSocket.accept();
                            //get date and time for timestamp
                            formattedDate = getDateAndTime();
                            serverLog.setText(serverLog.getText() + formattedDate + "\t\t\t" + "A new client is connected : " + serverEndpointTwo.getRemoteSocketAddress() + "\n");

                            //set socket
                            c1.setSocket(serverEndpointOne,serverEndpointTwo);
                            c2.setSocket(serverEndpointTwo,serverEndpointOne);
                        }
                        else if (serverEndpointOne == null) { //if endpoint one is null
                            System.out.println("FIRST");
                            //accepts first client
                            System.out.println("first accept");
                            serverEndpointOne = serverSocket.accept();
                            //get date and time for timestamp
                            formattedDate = getDateAndTime();
                            serverLog.setText(serverLog.getText() + formattedDate + "\t\t\t" + "A new client is connected : " + serverEndpointOne.getRemoteSocketAddress() + "\n");

                            c1.setSocket(serverEndpointOne,serverEndpointTwo);
                            c2.setSocket(serverEndpointTwo,serverEndpointOne);
                        }
                        else if (serverEndpointTwo == null) { //if endpoint two is null\
                            System.out.println("SECOND");
                            //accepts second client
                            System.out.println("second accept");
                            serverEndpointTwo = serverSocket.accept();
                            //get date and time for timestamp
                            formattedDate = getDateAndTime();
                            serverLog.setText(serverLog.getText() + formattedDate + "\t\t\t" + "A new client is connected : " + serverEndpointTwo.getRemoteSocketAddress() + "\n");
                            // create a new thread object
                            //c2 = new ClientHandler(serverEndpointTwo, serverEndpointOne, serverLog);
                            c1.setSocket(serverEndpointOne,serverEndpointTwo);
                            c2.setSocket(serverEndpointTwo,serverEndpointOne);
                            //t2 = new Thread(c2);
                            //t2.start();
                        }
                        if (c1.getConnected() == false) {
                            System.out.println("c1 false");
                            //c1.setConnected(true);
                            serverEndpointOne = null;
                        }
                        if (c2.getConnected() == false) {
                            System.out.println("c2 false");
                            //c2.setConnected(true);
                            serverEndpointTwo = null;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Error");
                }
            };
            Thread t = new Thread(runnable);
            t.start();


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

        saveLog.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("save clicked");
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