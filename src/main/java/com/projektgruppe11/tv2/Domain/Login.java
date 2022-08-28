package com.projektgruppe11.tv2.Domain;

import com.projektgruppe11.tv2.Persistence.UserFileRepository;
import com.projektgruppe11.tv2.Presentation.MainApp;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Deprecated
public class Login implements Initializable {

    private List<User> users;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    void login(ActionEvent event) {
        for (User user : users) {
            if (user.getUsername().equals(username.getText()) && user.getPassword().equals(password.getText())) {
                showPopUp("Logged in as " + user.getUsername() + " with role " + user.getRole().toString().toLowerCase(), Color.GREEN, 3);
                //new MainApp(user);
                try {
                    MainApp.setRoot("ApplicationLayer");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
        }
        showPopUp("No such login info!", Color.RED, 3);
    }

    @FXML
    void continueWithoutLogin(MouseEvent event) {
        showPopUp("Logged in as user", Color.GREEN, 3);
        try {
            MainApp.setRoot("ApplicationLayer");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showPopUp(String string, Color color, int duration) {
        final Stage popup = new Stage();
        popup.setAlwaysOnTop(true);

        Point p = MouseInfo.getPointerInfo().getLocation();
        popup.setX(p.x + 5);
        popup.setY(p.y - 18);
        Text text = new Text(string);
        text.setFill(color);
        text.setFont(new Font("Arial", 16));

        StackPane pane = new StackPane(text);
        pane.layout();
        double width = text.getLayoutBounds().getWidth();
        double padding = 10;

        Scene dialogScene = new Scene(pane, width + padding, 20);

        popup.setScene(dialogScene);
        popup.initStyle(StageStyle.TRANSPARENT);
        popup.setOpacity(0.5);

        PauseTransition delay = new PauseTransition(Duration.seconds(duration));
        delay.setOnFinished(e -> popup.hide());

        popup.show();
        delay.play();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UserFileRepository userFileRepository = UserFileRepository.getInstance();
        User admin = new User("admin", "password", User.Role.ADMIN);
        User producer = new User("producer", "password", User.Role.PRODUCER);
        User user = new User("user", "password", User.Role.USER);

        //userFileRepository.saveUsers(admin, producer, user);

    }
}
