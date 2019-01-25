package controllers;

import engine.Game;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MainController {
    @FXML
    private StackPane mainStackPane;


    public MainController() {
        System.out.println("Odpalam program");
    }

    public void initialize() {
        setConnectionScreen();

    }

    //ustawianei ekranu menu
    public void setConnectionScreen() {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../fxml/ConnectionScreen.fxml"));
        Pane pane = null;
        try {
            pane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ConnectionScreenController connectionScreenController = loader.getController();
        connectionScreenController.setMainController(this);
        mainStackPane.getChildren().clear();
        mainStackPane.getChildren().add(pane);
    }

    public void setPane(Pane pane) {
        mainStackPane.getChildren().clear();
        mainStackPane.getChildren().add(pane);
    }

}
