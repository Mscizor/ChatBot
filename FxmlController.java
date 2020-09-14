//import javafx.scene.control.*;
//import javafx.fxml.FXML;
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
        if (stage != null) {
            stage.hide();
            //try-catch method to continue the loading of the title screen without interference due to an error.
            //IOException (Input / Output Exception)
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ChatGUI.fxml"));
                Parent root = loader.load();

                loader.setLocation(FxmlController.class.getResource("ChatGUI.fxml"));
                loader.<ChatController>getController().initialize();

                Scene scene = new Scene(root);
                Stage stageg = new Stage();
                stageg.setScene(scene);
                stageg.setResizable(false);
                stageg.setTitle("Chat");
                stageg.initOwner(this.stage);
                stageg.show();

            } catch (IOException s) {
                s.printStackTrace();
            }
        }
    }

    public void initialize() {

        submit.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Logged in");
            }
        });

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }
}