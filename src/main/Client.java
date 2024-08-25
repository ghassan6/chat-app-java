package main;
import java.net.*;

import controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Client extends Application{

    public static final int SERVICE_PORT = 12345;
    private Socket client;

    private Parent root;
    @Override
    public void start(Stage stage) throws Exception {

        client = new Socket(InetAddress.getLoopbackAddress(), SERVICE_PORT);

        // select the initial scene to display
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/mainScene.fxml"));
        root = loader.load();

        // pass the reader/writer to the controller
        Controller controller = loader.getController();
        controller.initialize(client);

        Scene main = new Scene(root);

        // apply the css file
        main.getStylesheets().add(getClass().getResource("/resources/css/main.css").toExternalForm());
        // add icon and title to the main scene
        stage.getIcons().add(new Image("/resources/images/app icon.jpg"));
        stage.setTitle("JTalk");
        stage.setScene(main);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}