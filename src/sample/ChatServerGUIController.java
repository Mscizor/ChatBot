package sample;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

public class ChatServerGUIController{

    private ChatServerModel chatServerModel;
    private String log;
    private Boolean connected = true;
    private ServerSocket serverSocket;
    volatile public static Vector<ClientHandler> cList;
    public boolean serverStart;
    public static int i = 0;

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
            serverLog.setText(formattedDate + "\t\t\tListening on port " + nPort + "...\n");
        }
        catch(IOException e) {
            e.printStackTrace();
        }

            //Thread for accepting clients
            Runnable runnable = () -> {
            i=0;
                serverStart = true;
                this.cList = new Vector<ClientHandler>();
                while(true){
                    try {
                        //terminate server process where there are no clients left connected

                        if (serverStart && (ChatServerGUIController.cList.size()==1)) {
                            i++;
                            serverStart = false;
                            Runnable cListChecker = () -> {
                                Boolean serverConnected = true;
                                while (serverConnected) {
                                    if (ChatServerGUIController.cList.size() == 0) {
                                        try {
                                            serverSocket.close();
                                            serverConnected = false;
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            };
                            Thread t2 = new Thread(cListChecker);
                            t2.start();
                        }

                        //if no more client
                        if (ChatServerGUIController.cList.size() == 0 && !serverStart) {
                            throw new Exception("No more clients connected.");
                        }


                        if (ChatServerGUIController.cList.size() < 2) {
                            Socket client = serverSocket.accept();
                            String formattedDate = getDateAndTime();
                            serverLog.setText(serverLog.getText() + formattedDate + "\t\t\tNew client connected: " + client.getRemoteSocketAddress() + "\n");

                            //Initialize streams
                            DataInputStream input = new DataInputStream(client.getInputStream());
                            DataOutputStream output = new DataOutputStream(client.getOutputStream());

                            //Create thread
                            ClientHandler conn = new ClientHandler (client, ""+i, input, output, serverLog);
                            Thread t = new Thread (conn);
                            cList.add(i, conn);
                            t.start();
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                        break;
                    }
                }
                String formattedDate = getDateAndTime();
                serverLog.setText(serverLog.getText() + formattedDate + "\t\t\tBoth client disconnected. Server terminated\n"); //not working
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
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