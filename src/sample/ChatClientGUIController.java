package sample;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.Socket;

public class ChatClientGUIController{

    private ChatClientModel chatClientModel;

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

        System.out.println(chatClientModel.getIpAddress() + " " + chatClientModel.getPortNumber() + " " + chatClientModel.getUserName());
        String sServerAddress = chatClientModel.getIpAddress();
        int nPort = Integer.parseInt(chatClientModel.getPortNumber());
        try {
            Socket clientEndpoint = new Socket(sServerAddress, nPort);
            System.out.printf("%s: Connecting to server at %s\n", chatClientModel.getUserName(), clientEndpoint.getRemoteSocketAddress());
            System.out.printf("%s: Connected to server at %s\n", chatClientModel.getUserName(), clientEndpoint.getRemoteSocketAddress());
        }
        catch(Exception e) {
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
                System.out.println("Send clicked");
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