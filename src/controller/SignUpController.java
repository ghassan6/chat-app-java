package controller;

import java.io.*;
import java.net.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SignUpController {
    Socket client;
    private Parent root;
    private Stage stage;

    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField displayname;
    @FXML
    private PasswordField password;
    @FXML
    private Button signup;
    @FXML
    private VBox userPane;
    @FXML
    private Label error;

    public void setUp(Socket client) {
        this.client = client;
    }

    public void switchToMain(ActionEvent event) throws IOException {
         // Load the main scene FXML file
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/resources/fxml/mainScene.fxml"));
        root = mainLoader.load();

        // Initialize the controller of the main scene and then pass the client socket 
        Controller controller = mainLoader.getController();
        controller.initialize(client);

        // get the current stage
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        
        // create a "main" scene and apply CSS and set title to it
        Scene main = new Scene(root);
        stage.setTitle("JTalk");
        main.getStylesheets().add(getClass().getResource("/resources/css/main.css").toExternalForm());
        
        stage.setScene(main);
        stage.show();
    }

    public void getSignUpInfo(ActionEvent event) {
        
        try {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(client.getOutputStream()) , true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));

            // send sign operation to the server
            writer.println("sign");

            // send credentials to the server
            String username = usernameTextField.getText().trim();
            String displayName = displayname.getText().trim();
            String pass = password.getText().trim();

            if (error == null) {
                error = new Label();
                error.setStyle("-fx-text-fill: red;");
                userPane.getChildren().add( 2, error);
            }

            error.setText("");

            if (username.isEmpty() || pass.isEmpty()) {
                error.setText("All fields requierd");
                return;
            }

            writer.println(username);
            writer.println(displayName);
            writer.println(pass);

            String response = reader.readLine();

            if(response.equals("taken")) {
                error.setText("Username is taken, Please pick another one");
                return;
            }

            else {
                Stage stage = (Stage) signup.getScene().getWindow();
                switchToChatRoom(stage , username);
            }
            
        } catch (IOException IOEException) {
            IOEException.printStackTrace();
        }
    }

    public void switchToChatRoom(Stage stage ,String username) throws IOException {
        // create a new fxml loader and load the chat room 
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/chat_room.fxml"));
        Parent root = loader.load();

        // Initialize the controller of the main scene and then pass the client socket 
        ChatRoomController chatController = loader.getController();
        chatController.setup(client , username);

        Scene chatRoom = new Scene(root);

        // apply CSS
        chatRoom.getStylesheets().add(getClass().getResource("/resources/css/chatroom.css").toExternalForm());

        stage.setTitle("JTalk");
        stage.setScene(chatRoom);
        stage.show();
    }
}
