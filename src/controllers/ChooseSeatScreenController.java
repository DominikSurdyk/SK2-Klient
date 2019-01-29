package controllers;

import connection.Connection;
import engine.Game;
import engine.Model;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class ChooseSeatScreenController {

    private static final int PLAYER_SEATS = 6;
    private MainController mainController;
    private int[] seatStatusArray = new int[PLAYER_SEATS];

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
    private Label messageLabel;

    @FXML
    void initialize() {
        if (Connection.getClientId() < 0 ){
            Connection.writeData("1");

            try {
                Connection.myThread.join();
            } catch (InterruptedException e) {
                System.out.println("Błąd podczas inicjalizowania ChooseSeatController!");
            }


            Connection.readData();
            String response = "";
            try {
                Connection.myThread.join();
                response = Connection.stringBuilderOut.toString();
            } catch (InterruptedException e) {
                System.out.println("Błąd podczas inicjalizowania ChooseSeatController!");
            }
            //sprawdzanie ktore przyciski sa aktywne
            if (!"fail".equals(response)) {

                System.out.println("status-ID-xxxxxx(0-Wolne, 1-Zajete): " + response);
                String[] tokens = response.split("-");
                int myId = Integer.parseInt(tokens[1]);
                Connection.setClientId(myId);
                char seatStatus;
                for (int i = 0; i < PLAYER_SEATS; i++) {
                    seatStatus = tokens[2].charAt(i);
                    seatStatusArray[i] = Character.getNumericValue(seatStatus);
                }
                updateSeatStatusArray();
            } else {
                System.out.println("Nie można pobrać ID, oraz wolnych stołów!");
            }
        }else {
            Connection.writeData("4");
            try {
                Connection.myThread.join();
            } catch (InterruptedException e) {
                System.out.println("Błąd podczas inicjalizowania ChooseSeatController! Wysylanie");
            }


            Connection.readData();
            String response = "";
            try {
                Connection.myThread.join();
                response = Connection.stringBuilderOut.toString();
            } catch (InterruptedException e) {
                System.out.println("Błąd podczas inicjalizowania ChooseSeatController!");
            }
            //sprawdzanie ktore przyciski sa aktywne
            if (!"fail".equals(response)) {

                System.out.println("status-xxxxxx(0-Wolne, 1-Zajete): " + response);
                String[] tokens = response.split("-");
                char seatStatus;
                for (int i = 0; i < PLAYER_SEATS; i++) {
                    seatStatus = tokens[1].charAt(i);
                    seatStatusArray[i] = Character.getNumericValue(seatStatus);
                }
                updateSeatStatusArray();
            } else {
                System.out.println("Błąd podczas inicjalizowania ChooseSeatController!");
            }
        }

    }

    private void buttonAction(int seatNumber) {
        Connection.writeData("2-"
                + Connection.getClientId() + "-"
                + (seatNumber));

        try {
            Connection.myThread.join();
        } catch (InterruptedException e) {
            System.out.println("Blad podczas wysylania danych");
        }


        Connection.readData();
        String response = "";
        try {
            Connection.myThread.join();
            response = Connection.stringBuilderOut.toString();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!"fail".equals(response)) {
            System.out.println("status-x(x: 1 - zaczynaj, 0 - czekaj): " + response);
            String[] tokens = response.split("-");
            if("3".equals(tokens[0])){
                char seatStatus;
                for (int i = 0; i < PLAYER_SEATS; i++) {
                    seatStatus = tokens[1].charAt(i);
                    seatStatusArray[i] = Character.getNumericValue(seatStatus);
                }
                messageLabel.setText("NIESTETY KTOŚ WŁAŚNIE ZAJĄŁ TO MIEJSCE. WYBIERZ INNE!");
                updateSeatStatusArray();
            }else{
                boolean makeFirstMove = (1 == Integer.parseInt(tokens[1]));
                openGameScreen(seatNumber/2,seatNumber % 2, makeFirstMove);
            }

        } else {
            System.out.println("Nie udało się wybrać miejsca. Problem z połączeniem!");
        }

    }

    public void openGameScreen(int gameNo, int gameSeatNo, boolean makeFirstMove) {
        Model.newGame(gameNo,gameSeatNo, makeFirstMove);

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../fxml/GameScreen.fxml"));
        Pane pane = null;
        try {
            pane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        GameScreenController gameScreenController = loader.getController();
        gameScreenController.setMainController(this.getMainController());
        mainController.setPane(pane);
    }

    public MainController getMainController() {
        return mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void updateSeatStatusArray() {
        for (int i = 0; i < PLAYER_SEATS; i++) {
            if (i == 0) {
                button1_1.setDisable(seatStatusArray[i] == 1);
            }
            if (i == 1) {
                button1_2.setDisable(seatStatusArray[i] == 1);
            }
            if (i == 2) {
                button2_1.setDisable(seatStatusArray[i] == 1);
            }
            if (i == 3) {
                button2_2.setDisable(seatStatusArray[i] == 1);
            }
            if (i == 4) {
                button3_1.setDisable(seatStatusArray[i] == 1);
            }
            if (i == 5) {
                button3_2.setDisable(seatStatusArray[i] == 1);
            }

        }
    }

    public void button1_1Action() {
        buttonAction(0);
    }

    public void button1_2Action() {
        buttonAction(1);

    }

    public void button2_1Action() {
        buttonAction(2);
    }

    public void button2_2Action() {
        buttonAction(3);
    }

    public void button3_1Action() {
        buttonAction(4);
    }

    public void button3_2Action() {
        buttonAction(5);
    }
}
