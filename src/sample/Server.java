package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Server extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {

        ChatServerModel chatServerModel = new ChatServerModel();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ChatServer.fxml"));

        Parent root = loader.load();

        Scene scene = new Scene(root);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Chat Server");
        primaryStage.setScene(scene);
        primaryStage.show();

        loader.<ChatServerController>getController().initialize(chatServerModel);
        loader.<ChatServerController>getController().setStage(primaryStage);

    }

    public static void main(String[] args) {
        launch(args);
    }
}