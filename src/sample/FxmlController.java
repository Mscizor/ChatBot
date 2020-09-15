package sample;
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

public class FxmlController{
    @FXML
    private Stage stage;

    @FXML
    public TextField ipAddress = null;
    @FXML
    public TextField portNumber = null;
    @FXML
    private Button submit;

    /*
    This method sends the user to the ChatGUI window.
    */
    @FXML
    public void submit(){

    }

    public void initialize() {

        submit.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    switchSceneToChatGUI("ChatGUI.fxml");
                    System.out.println("Logged in");

                } catch (IOException s) {
                    s.printStackTrace();
                }
            }
        });

    }

    public void switchSceneToChatGUI(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxml));
        Parent login = loader.load();
        Scene scene = new Scene(login);
        Stage stage = (Stage) submit.getScene().getWindow();

        ChatController controller = loader.getController();
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