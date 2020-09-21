package sample;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;

public class ChatServerController{

    private ChatServerModel chatServerModel;

    @FXML
    private Stage stage;
    @FXML
    public TextField ipAddress;
    @FXML
    public TextField portNumber;
    @FXML
    private Button submit;

    /*
    This method sends the user to the ChatGUI window.
    */
    @FXML
    public void submit(){

    }

    public void initialize(ChatServerModel chatServerModel) {
        this.chatServerModel = chatServerModel;

        submit.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    boolean empty=false;
                    boolean numeric= true;
                    ChatServerController c = new ChatServerController();
                    //Check if both inputs are empty
                    if ((ipAddress.getText() == null || ipAddress.getText().trim().isEmpty()) ||
                            portNumber.getText() == null || portNumber.getText().trim().isEmpty()) {
                        empty = true;
                    }

                    if (!empty) {
                        //Check if port number is number
                        try {
                            int x = Integer.parseInt(portNumber.getText());
                        }
                        catch (NumberFormatException e){
                            numeric = false;
                            Alert fail = new Alert(Alert.AlertType.INFORMATION);
                            fail.setHeaderText("Error");
                            fail.setContentText("Port number should be a number");
                            fail.showAndWait();
                        }
                        if (numeric) {
                            chatServerModel.setIpAddress(ipAddress.getText());
                            chatServerModel.setPortNumber(portNumber.getText());
                            System.out.println(chatServerModel.getIpAddress());
                            System.out.println(chatServerModel.getPortNumber());
                            switchSceneToServerGUI("ChatServerGUI.fxml");
                            System.out.println("Server connected");
                        }
                    }
                    else{
                        Alert fail = new Alert(Alert.AlertType.INFORMATION);
                        fail.setHeaderText("Error");
                        fail.setContentText("Type in the missing input");
                        fail.showAndWait();
                    }
                } catch (IOException s) {
                    s.printStackTrace();
                }
            }
        });

    }

    public void switchSceneToServerGUI(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxml));
        Parent ChatServer = loader.load();
        Scene scene = new Scene(ChatServer);
        Stage stage = (Stage) submit.getScene().getWindow();

        ChatServerGUIController controller = loader.getController();
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