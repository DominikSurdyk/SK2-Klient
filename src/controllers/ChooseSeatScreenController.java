package controllers;

import connection.Connection;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ChooseSeatScreenController {
    private MainController mainController;

    @FXML
    private Button button1_1;
    @FXML
    private Button button1_2;
    @FXML
    private Button button2_1;
    @FXML
    private Button button2_2;
    @FXML
    private Button button3_1;
    @FXML
    private Button button3_2;

    @FXML
    void initialize(){
        Connection.writeData("0");


    }

    public MainController getMainController() {
        return mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
