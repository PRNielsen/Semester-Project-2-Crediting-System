package com.projektgruppe11.tv2.Presentation;


import com.projektgruppe11.tv2.Domain.Credit;
import com.projektgruppe11.tv2.Domain.CreditingSystem;
import com.projektgruppe11.tv2.Domain.Production;
import com.projektgruppe11.tv2.Domain.User;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainApp extends Application implements Initializable {

    private CreditingSystem creditingSystem;
    private static Scene scene;
    private User user;

    @FXML
    private TableView<Production> tableView;

    @FXML
    private TableColumn<Production, String> titleColumn;

    @FXML
    private TableColumn<Production, String> idColumn;

    @FXML
    private TextField searchBar;

    @FXML
    private Button searchButton;

    @FXML
    private Button addProductions;

    @FXML
    private Button saveToXML;

    @FXML
    private TableView<Credit> creditTableView;

    @FXML
    private TableColumn<Credit, String> creditNameColumn;

    @FXML
    private TableColumn<Credit, String> creditRoleColumn;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("ApplicationLayer"));
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getClassLoader().getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        creditingSystem = new CreditingSystem();

        titleColumn.setCellValueFactory(data -> data.getValue().nameProperty());
        idColumn.setCellValueFactory(data -> data.getValue().uuidProperty());
        //titleColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        //idColumn.setCellValueFactory(new PropertyValueFactory<>("uuid"));
        tableView.getItems().addAll(creditingSystem.getProductions().values());

        creditNameColumn.setCellValueFactory(data -> data.getValue().nameProperty());
        creditRoleColumn.setCellValueFactory(data -> data.getValue().roleProperty());
        //personsNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        //personsRoleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        creditTableView.setVisible(false);

        //Cell editing
        titleColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        CreateProductionCell(titleColumn);

        CreateProductionCell(idColumn);

        //FOR UUID
       /* idColumn.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<UUID>() {
            @Override
            public String toString(UUID object) {
                return object.toString();
            }

            @Override
            public UUID fromString(String string) {
                return UUID.fromString(string);
            }
        }));
        idColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Production, UUID>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Production, UUID> t) {
                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setUuid(t.getNewValue());
                    }
                }
        );*/

        creditNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        creditNameColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Credit, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Credit, String> t) {
                        t.getTableView().getItems().get(t.getTablePosition().getRow()).getPerson().setName(t.getNewValue());
                        //tableView.getItems().get(t.getTablePosition().getRow()).getCredits().get(t.getTablePosition().getRow()).getPerson().setName(t.getNewValue());
                        t.getTableView().getItems().get(t.getTablePosition().getRow()).setNameProperty(t.getNewValue());
                        creditingSystem.updateCredit(t.getTableView().getItems().get(t.getTablePosition().getRow()));
                    }
                }
        );

        creditRoleColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        creditRoleColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Credit, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Credit, String> t) {
                        t.getTableView().getItems().get(t.getTablePosition().getRow()).setRole(t.getNewValue());
                        //tableView.getItems().get(t.getTablePosition().getRow()).getCredits().get(t.getTablePosition().getRow()).setRole(t.getNewValue());
                        t.getTableView().getItems().get(t.getTablePosition().getRow()).setRoleProperty(t.getNewValue());
                        creditingSystem.updateCredit(t.getTableView().getItems().get(t.getTablePosition().getRow()));
                    }
                }
        );

        /*for(int i = 3; i < 150; i++){
            Production production = new Production("Test Production " + i);
            tableView.getItems().add(production);
        }*/
    }

    private void CreateProductionCell(TableColumn<Production, String> idColumn) {
        idColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Production, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Production, String> t) {
                        t.getTableView().getItems().get(t.getTablePosition().getRow()).setName(t.getNewValue());
                        t.getTableView().getItems().get(t.getTablePosition().getRow()).setNameProperty(t.getNewValue());
                        creditingSystem.updateProduction(t.getTableView().getItems().get(t.getTablePosition().getRow()));
                    }
                }
        );
    }

    @FXML
    void login(ActionEvent event) {
        User user = creditingSystem.authenticate(usernameField.getText(), passwordField.getText());
        if (user != null) {
            showPopUp("Logged in as " + user.getUsername() + " with role " + user.getRole().toString().toLowerCase(), Color.GREEN, 3);
            this.user = user;
            //If admin then you should be able to edit these cells
            if (user.getRole() == User.Role.ADMIN) {
                tableView.setEditable(true);
                creditTableView.setEditable(true);
            } else if (user.getRole() == User.Role.PRODUCER) {
                creditTableView.setEditable(true);
            }
        } else {
            showPopUp("No such login info!", Color.RED, 3);
        }
    }

    @FXML
    void onSearchButtonAction(ActionEvent event) {
        search(searchBar.getText(), true);
    }

    @FXML
    void onSearchTextChanged(KeyEvent event) {
        if (searchBar.getText().equalsIgnoreCase("")) {
            tableView.getSelectionModel().clearSelection();
            tableView.getItems().clear();
            tableView.getItems().addAll(creditingSystem.getProductions().values());
            return;
        }
        search(searchBar.getText(), true);
    }

    @FXML
    void onProductionTableClick(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            findCredits();
        }
    }

    public void search(String string, boolean highlight) {
        if (string.equalsIgnoreCase("")) return;

        for (int i = 0; i < tableView.getItems().size(); i++) {
            Production production = tableView.getItems().get(i);
            if (simpleTextSearch(string.toCharArray(), production.getName().toCharArray()) != -1 || string.equalsIgnoreCase(production.getName())) {
                tableView.getItems().add(0, tableView.getItems().remove(i));
                if (highlight) {
                    tableView.getSelectionModel().clearAndSelect(0);
                    findCredits();
                }
            }
        }
    }

    private void findCredits() {
        Production production = tableView.getSelectionModel().getSelectedItem();
        creditTableView.getItems().clear();
        if(production.hasCredits()) {
            creditTableView.getItems().addAll(production.getCredits());
            creditTableView.setVisible(true);
        }else{
            creditTableView.setVisible(false);
        }
    }

    //O(m(n-m + 1))
    //https://www.baeldung.com/java-full-text-search-algorithms
    private int simpleTextSearch(char[] pattern, char[] text) {
        int patternSize = pattern.length;
        int textSize = text.length;

        int i = 0;

        while ((i + patternSize) <= textSize) {
            int j = 0;
            while (text[i + j] == pattern[j]) {
                j += 1;
                if (j >= patternSize)
                    return i;
            }
            i += 1;
        }
        return -1;
    }


    @FXML
    private void addProductionsFromXML(ActionEvent event) {
        try {
            creditingSystem.saveXMLtoDatabase();
            updateTableView();
        } catch (NullPointerException e) {
            System.out.println("Cannot add same productions");
        }
    }

    @FXML
    private void setSaveToXML(ActionEvent event) {
        try {
            creditingSystem.saveToXML(new File("ProductionsProgramlist.xml"));
        } catch (NullPointerException e) {
            System.out.println("Creditingsystem import to xml went wrong");
        }
    }

    @FXML
    private void updateTableView() {
        tableView.getItems().clear();
        tableView.getItems().addAll(creditingSystem.getProductions().values());
        tableView.refresh();
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
}
