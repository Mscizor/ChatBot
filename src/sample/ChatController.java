package sample;
//import javafx.scene.control.*;
//import javafx.fxml.FXML
import javafx.scene.control.TextField;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
public class ChatController{
    @FXML
    private Stage stage;
    @FXML
    private Button exit;
    @FXML
    private Button save;


    /*
    This method sends the user to the ChatGUI window.
    */

    @FXML
    public void exit(){
            //try-catch method to continue the loading of the title screen without interference due to an error.
            //IOException (Input / Output Exception)
        try {
            switchSceneToLogin("Login.fxml");
            System.out.println("Logged out");
        } catch (IOException s) {
            s.printStackTrace();
        }
    }

    /*
    This method sends the user to the ChatGUI window.
    */
    @FXML
    public void save(){
            //try-catch method to continue the loading of the title screen without interference due to an error.
            //IOException (Input / Output Exception)
        try {
            switchSceneToLogin("Login.fxml");
            System.out.println("Saved");
        } catch (IOException s) {
            s.printStackTrace();
        }
    }

    public void initialize() {
        /*
        exit.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Exit");
            }
        });
        */

        
    }

    public void switchSceneToLogin(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxml));
        Parent login = loader.load();
        Scene scene = new Scene(login);
        Stage stage = (Stage) exit.getScene().getWindow();

        FxmlController controller = loader.getController();
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