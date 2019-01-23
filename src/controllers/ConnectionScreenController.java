package controllers;

import connection.Connection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class ConnectionScreenController {

    private MainController mainController;

    @FXML
    private Button connectionButton;
    @FXML
    private TextField ipAddressTextField;
    @FXML
    private TextField portTextField;
    @FXML
    private Label messageLabel;

    public MainController getMainController() {
        return mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void connectToServer(){
        int result = Connection.connect(ipAddressTextField.getText(), Integer.parseInt(portTextField.getText()));
        if (result == 0){
            System.out.println("Otwieram ekran wyboru drużyny");
            openChooseSeatScreen();
        }else {
            messageLabel.setText("Nie udało się nawiązać połączenia");
        }
    }

    public void openChooseSeatScreen(){
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../fxml/ChooseSeatScreen.fxml"));
        Pane pane = null;
        try {
            pane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ChooseSeatScreenController chooseSeatScreenController = loader.getController();
        chooseSeatScreenController.setMainController(this.getMainController());
        mainController.setPane(pane);
    }
}
