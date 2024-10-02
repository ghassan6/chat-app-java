package controller;

import java.net.Socket;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import main.CustomListCell;
import main.SQLHelper;
import main.User;

public class ChatRoomController {
    private String currentUser;
    Socket client;
    @FXML
    private Button sendButton;
    @FXML
    private Pane dashBoard;
    @FXML
    private TextField sendField;
    @FXML
    private TextField search;
    @FXML
    private AnchorPane root;
    @FXML
    private ListView<User> userListView;

    public void setup(Socket client , String currentUser) {
        this.client = client;
        this.currentUser = currentUser;
        greeting(currentUser);
    }

    public void greeting(String currentUser) {
        Label welcomeMessage = new Label();
        welcomeMessage.setText("Welcome back: " + currentUser);
        welcomeMessage.setId("welcomeLabel");
        welcomeMessage.setLayoutX(140);
        welcomeMessage.setLayoutY(67);

        dashBoard.getChildren().add(welcomeMessage);
    }

      @FXML
      public void initialize() {
        
        search.textProperty().addListener((observable , old , newValue) -> {

            if(!newValue.isEmpty()) updateSuggestions(newValue , currentUser);
            
            if(newValue.isEmpty()) {
                userListView.setVisible(false);
                userListView.setManaged(false);
            }
        });

        userListView.setCellFactory(param -> {
            
            CustomListCell cell = new CustomListCell();
            cell.setCurrentUSer(SQLHelper.getUser(currentUser), client);
            return cell;
        });
      }

    //   do it in the background 
    public void updateSuggestions(String targetUser , String currentUser) {

        new Thread(() -> {
            // look for the user in the daabase excluding the current user
            ArrayList<User> users = SQLHelper.lookUp(targetUser , currentUser);

            // convert the user arraylist to an abservableList and show it in the ListView
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
