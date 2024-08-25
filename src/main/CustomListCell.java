package main;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

    public class CustomListCell extends ListCell<User> {
        private Button button = new Button("Add");
        private User currentUser;

        public void setCurrentUSer(User currentUser) {
            this.currentUser = currentUser;
        }
        @Override
        protected void updateItem(User user , boolean empty) {
            super.updateItem(user, empty);

            if (empty || user == null) {
                setText(null);
                setGraphic(null);
            }

            else {
                button.setId("addButton");
                button.setOnAction(event -> {
                    System.out.println(user.getUsername() + " by " + currentUser.getUsername());
                });
                
                Region region = new Region();
                Label userLabel = new Label(user.toString());
                HBox hbox = new HBox();

                userLabel.setId("userLabel");

                hbox.getChildren().addAll(userLabel , region , button);

                HBox.setHgrow(region, Priority.ALWAYS);
                setGraphic(hbox);
            }
        }

    }
