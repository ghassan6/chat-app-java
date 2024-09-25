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

public class Controller {
    public static final int SERVICE_PORT = 12345;
    Socket client;
    private Stage stage;

    private Parent root;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button login;
    @FXML 
    private Label error;
    @FXML
    private VBox mainPane;
    

    public void initialize(Socket client) {
        this.client = client;
    }

    public void getInfo(ActionEvent event)  {
      
         try {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(client.getOutputStream()) , true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
    
            // send log operation to the server 
            writer.println("log");
    
            writer.println(username.getText().trim());
            writer.println(password.getText());
            
            if (error == null) {
                error = new Label();
                error.setStyle("-fx-text-fill: red;");
                mainPane.getChildren().add( 3, error);
            }

            error.setText("");

            if(reader.readLine().equals("valid")) {
                Stage stage = (Stage) login.getScene().getWindow();
                switchToChatRoom(stage , username.getText());
            }
               
            // alert the user for wrong crendentials
            else error.setText("Incorrect username or password!");
            
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void switchToSignUp(ActionEvent event) throws IOException {
        // Load the sign up scene FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/signUp.fxml"));
        root = loader.load();

        // Initialize the controller of the sign up scene and then pass the client socket 
        SignUpController signUpController = loader.getController();
        signUpController.setUp(client);

        // get the current stage
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // create a "sign up" scene and apply CSS and set title to it
        Scene signUpScene = new Scene(root);
        signUpScene.getStylesheets().add(getClass().getResource("/resources/css/main.css").toExternalForm());
        stage.setTitle("Sign up");
        
        stage.setScene(signUpScene);
        stage.show(); 
    }
    
    public void switchToChatRoom(Stage stage , String username) throws IOException {
        
        // create a new fxml loader and load the chat room 
        FXMLLoader chatLoader = new FXMLLoader(getClass().getResource("/resources/fxml/chat_room.fxml"));
        Parent root = chatLoader.load();

        // create an instance of chat room controller
        ChatRoomController chatRoomController = chatLoader.getController();
        chatRoomController.setup(client, username);

        // creae a new scene and assign the root
        Scene chatRoom = new Scene(root);

        // apply CSS
        chatRoom.getStylesheets().add(getClass().getResource("/resources/css/chatroom.css").toExternalForm());

        stage.setScene(chatRoom);
        stage.show();
        
    }
}
