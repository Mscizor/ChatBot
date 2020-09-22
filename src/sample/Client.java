package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Client extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {

        ChatClientModel chatClientModel = new ChatClientModel();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ChatClient.fxml"));

        Parent root = loader.load();

        Scene scene = new Scene(root);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Chat Client");
        primaryStage.setScene(scene);
        primaryStage.show();

        loader.<ChatClientController>getController().initialize(chatClientModel);
        loader.<ChatClientController>getController().setStage(primaryStage);

    }

    public static void main(String[] args) {
        launch(args);
    }
}