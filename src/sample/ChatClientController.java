package sample;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
public class ChatClientController{

    private ChatClientModel chatClientModel;

    @FXML
    private Stage stage;

    @FXML
    public TextField ipAddress = null;
    @FXML
    public TextField portNumber = null;
    @FXML
    public TextField userName = null;
    @FXML
    private Button enter;

    /*
    This method sends the user to the ChatClientGUI window.
    */
    @FXML
    public void enter(){

    }

    public void alert(String s) {
        Alert fail = new Alert(Alert.AlertType.INFORMATION);
        fail.setHeaderText("Error");
        fail.setContentText(s);
        fail.showAndWait();
    }

    public void initialize(ChatClientModel chatClientModel) {
        this.chatClientModel = chatClientModel;

        enter.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Enter clicked");
                try {
                    boolean empty=false;
                    boolean numeric= true;
                    //Check if all inputs are empty
                    if ((ipAddress.getText() == null || ipAddress.getText().trim().isEmpty()) ||
                            (portNumber.getText() == null || portNumber.getText().trim().isEmpty()) ||
                                (userName.getText() == null || userName.getText().trim().isEmpty())) {
                        empty = true;
                    }

                    if (!empty) {
                        //Check if port number is number
                        try {
                            int x = Integer.parseInt(portNumber.getText());
                        }
                        catch (NumberFormatException e){
                            numeric = false;
                            alert("Port number should be a number");
                        }
                        if (numeric) {
                            if ((Integer.parseInt(portNumber.getText()) > 0) &&
                                    (Integer.parseInt(portNumber.getText()) <= 65535)) {
                                chatClientModel.setPortNumber(portNumber.getText());
                                chatClientModel.setIpAddress(ipAddress.getText());
                                chatClientModel.setUserName(userName.getText());
                                switchSceneToChatClientGUI("ChatClientGUI.fxml");
                                System.out.println("Server connected");
                            }
                            else {
                                alert("Invalid port Number");
                            }
                        }
                    }
                    else{
                        alert("Type in the missing input");
                    }
                } catch (IOException s) {
                    s.printStackTrace();
                }
            }
        });

    }

    public void switchSceneToChatClientGUI(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxml));
        Parent login = loader.load();
        Scene scene = new Scene(login);
        Stage stage = (Stage) enter.getScene().getWindow();

        ChatClientGUIController controller = loader.getController();
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