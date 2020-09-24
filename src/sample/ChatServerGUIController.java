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

import java.io.*;
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
    volatile private Socket serverEndpointOne = null;
    volatile private Socket serverEndpointTwo = null;
    private Socket tempSocket = null;

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
            serverLog.setText(formattedDate + "\t\t\t" +"Server: Listening on port " + nPort + "...\n");
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
                    serverLog.setText(serverLog.getText() + formattedDate + "\t\t\t" + "A new client is connected : " + serverEndpointOne.getRemoteSocketAddress() + "\n");

                    //accepts second client
                    serverEndpointTwo = serverSocket.accept();
                    //get date and time for timestamp
                    formattedDate = getDateAndTime();
                    serverLog.setText(serverLog.getText() + formattedDate + "\t\t\t" + "A new client is connected : " + serverEndpointTwo.getRemoteSocketAddress() + "\n");
                    // create a new thread object
                    ClientHandler c1 = new ClientHandler(serverEndpointOne, serverEndpointTwo, serverLog);
                    ClientHandler c2 = new ClientHandler(serverEndpointTwo, serverEndpointOne, serverLog);
                    Thread t1 = new Thread(c1);
                    Thread t2 = new Thread(c2);
                    t1.start();
                    t2.start();
                    Boolean connected = true;
                    while (true) {
                        if (!c1.getConnected()) {
                            c1.setConnected(true);
                            serverEndpointOne = null;
                        }
                        if (!c2.getConnected()) {
                            c2.setConnected(true);
                            serverEndpointTwo = null;
                        }
                        if (serverEndpointOne == null) { //if endpoint one is null
                            //accepts first client
                            serverEndpointOne = serverSocket.accept();
                            //get date and time for timestamp
                            formattedDate = getDateAndTime();
                            serverLog.setText(serverLog.getText() + formattedDate + "\t\t\t" + "A new client is connected : " + serverEndpointOne.getRemoteSocketAddress() + "\n");
                            System.out.println("Endpoint one");
                            c1.setSocket1(serverEndpointOne);
                            c2.setSocket2(serverEndpointOne);
                            System.out.println("Endpoint two");
                            c1.setSocket2(serverEndpointTwo);
                            c2.setSocket1(serverEndpointTwo);
                            c1.setConnected(false);
                        }
                        if (serverEndpointTwo == null) { //if endpoint two is null
                            //accepts second client
                            serverEndpointTwo = serverSocket.accept();
                            //get date and time for timestamp
                            formattedDate = getDateAndTime();
                            serverLog.setText(serverLog.getText() + formattedDate + "\t\t\t" + "A new client is connected : " + serverEndpointTwo.getRemoteSocketAddress() + "\n");
                            // create a new thread object
                            //c2 = new ClientHandler(serverEndpointTwo, serverEndpointOne, serverLog);
                            System.out.println("Endpoint two");
                            c2.setSocket1(serverEndpointTwo);
                            c1.setSocket2(serverEndpointTwo);
                            System.out.println("Endpoint one");
                            c2.setSocket2(serverEndpointOne);
                            c1.setSocket1(serverEndpointOne);
                            c2.setConnected(false);
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
                try {
                    File file = new File("Server_log.txt");
                    FileWriter writeFile = new FileWriter("Server_log.txt");
                    file.createNewFile();
                    serverLog.setText(serverLog.getText() + getDateAndTime() + "\t\t\tServer log saved in Server_log.txt\n");
                    writeFile.write(serverLog.getText());
                    writeFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
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