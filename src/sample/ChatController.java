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
    This method sends the user to the Login window.
    */
    @FXML
    public void exit(){

    }

    /*
    This method sends the user to the Login window. (temporary just to check if its working)
    */
    @FXML
    public void save(){

    }

    public void initialize() {

        exit.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Exit");
                try {
                    switchSceneToLoginExit("Login.fxml");

                } catch (IOException s) {
                    s.printStackTrace();
                }
            }
        });

        save.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("saved");
                try {
                    switchSceneToLoginSave("Login.fxml");

                } catch (IOException s) {
                    s.printStackTrace();
                }
            }
        });
        
    }

    public void switchSceneToLoginExit(String fxml) throws IOException {
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

    public void switchSceneToLoginSave(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxml));
        Parent login = loader.load();
        Scene scene = new Scene(login);
        Stage stage = (Stage) save.getScene().getWindow();

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