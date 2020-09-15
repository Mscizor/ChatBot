package sample;
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

    public void initialize() {

        enter.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Enter clicked");
                try {
                    switchSceneToChatClientGUI("ChatClientGUI.fxml");

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
        controller.initialize();

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