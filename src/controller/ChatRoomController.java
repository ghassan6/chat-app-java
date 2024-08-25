package controller;

import java.net.Socket;
import java.util.ArrayList;

// import org.controlsfx.control.textfield.TextFields;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import main.CustomListCell;
import main.SQLHelper;
import main.User;

public class ChatRoomController {
    private String username;
    Socket client;
    @FXML
    private Button sendButton;
    @FXML
    private Pane displayChat;
    @FXML
    private TextField sendField;
    @FXML
    private TextField search;
    @FXML
    private AnchorPane root;
    @FXML
    private ListView<User> userListView;

    public void setup(Socket client , String username) {
        this.client = client;
        this.username = username;
        greeting(username);
    }

    public void greeting(String username) {
        Label welcomeMessage = new Label();
        welcomeMessage.setText("Welcome back: " + username);
        welcomeMessage.setId("welcomeLabel");
        welcomeMessage.setLayoutX(140);
        welcomeMessage.setLayoutY(67);

        displayChat.getChildren().add(welcomeMessage);
    }

    public void add(ActionEvent event ) {
        sendMessage();
    }

    public void pressEnter(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER && !sendField.getText().isEmpty()) {
            sendMessage();
            event.consume();
        }
    }

    public void sendMessage() {
        String message = sendField.getText();

        if(!message.isEmpty()) {
            Text text = new Text();
            text.setText(message.trim());

            HBox hBox = new HBox();
            hBox.getChildren().add(text);
            displayChat.getChildren().add(hBox);

            sendField.clear();
        }
    }

      @FXML
      public void initialize() {
        
        search.textProperty().addListener((observable , old , newValue) -> {

            if(!newValue.isEmpty()) updateSuggestions(newValue , username);
            
            if(newValue.isEmpty()) {
                userListView.setVisible(false);
                userListView.setManaged(false);
            }
           
        });

        userListView.setCellFactory(param -> {
            
            CustomListCell cell = new CustomListCell();
            cell.setCurrentUSer(SQLHelper.getUser(username));
            return cell;
        });
      }

    //   do it in thr background - runlater - thread or task
    public void updateSuggestions(String lookForName , String currentUser) {

        new Thread(() -> {

            ArrayList<User> users = SQLHelper.lookUp(lookForName , currentUser);

            ObservableList<User> suggestions = FXCollections.observableArrayList(users);

            Platform.runLater(() -> {
                if(suggestions.isEmpty()) {
                    userListView.setVisible(false);
                    userListView.setManaged(false);
                } else {
                    userListView.setVisible(true);
                    userListView.setManaged(true);
                    userListView.setItems(suggestions);
                }
            });
        }).start();
       
    }
}
