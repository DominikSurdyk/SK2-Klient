package controllers;

import connection.Connection;
import engine.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.io.IOException;


public class GameScreenController {
    private MainController mainController;
    private Game game;

    @FXML
    private Canvas canvas; //wymiary boiska to 600x700 jednostek
    @FXML
    private Button buttonUp;
    @FXML
    private Button buttonUpRight;
    @FXML
    private Button buttonRight;
    @FXML
    private Button buttonDownRight;
    @FXML
    private Button buttonDown;
    @FXML
    private Button buttonDownLeft;
    @FXML
    private Button buttonLeft;
    @FXML
    private Button buttonUpLeft;
    @FXML
    private Label labelMessage;
    @FXML
    private Label labelPlayerOneSeat;
    @FXML
    private Label labelPlayerTwoSeat;
    @FXML
    private Button buttonExit;
    //grafika i wymiary
    private GraphicsContext field;
    private final int fieldUnit = 50;
    private final int oldLineWidth = 1;
    private final int newLineWidth = 3;

    private final int dotFieldRadius = 2;
    private final int fieldBandWidth = 2;
    private final int currentPositionRadius = 10;

    //yZero xZero to poczatki ukladow wspolrzednych do rysowania
    private final int xZero = 2 * fieldUnit;
    private final int yZero = 1 * fieldUnit;


    public MainController getMainController() {
        return mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void initialize() {
        this.game = Model.getGame();
        field = canvas.getGraphicsContext2D();
        drawEmptyField();
        refreshBUttons();
        drawOldMoves();
        drawCurrentPosition();
        setPlayerSeatsLabels();
        if (!game.isMyTurn()) {
            setmessageLabelOpponentTurn();
            Connection.readMoves(this.game, this);
        } else {
            setmessageLabelMyTurn();
        }
    }

    //poczatek ukladu wspolrzednych w lewym gornym rogu. x rosnie w prawo, y rosnie w dol
    public void drawEmptyField() {
        field.setFill(Color.GREEN);
        field.fillRect(0, 0, 12 * fieldUnit, 14 * fieldUnit);
        field.setFill(Color.WHITE);
        field.setStroke(Color.WHITE);
        field.setLineWidth(fieldBandWidth);

        //rysowanie lini

        //gora lini
        field.strokeLine(2 * fieldUnit, 2 * fieldUnit, 5 * fieldUnit, 2 * fieldUnit);
        field.strokeLine(5 * fieldUnit, 2 * fieldUnit, 5 * fieldUnit, 1 * fieldUnit);
        field.strokeLine(5 * fieldUnit, 1 * fieldUnit, 7 * fieldUnit, 1 * fieldUnit);
        field.strokeLine(7 * fieldUnit, 1 * fieldUnit, 7 * fieldUnit, 2 * fieldUnit);
        field.strokeLine(7 * fieldUnit, 2 * fieldUnit, 10 * fieldUnit, 2 * fieldUnit);
        //pionowa linia prawa
        field.strokeLine(10 * fieldUnit, 2 * fieldUnit, 10 * fieldUnit, 12 * fieldUnit);
        //dolna linia
        field.strokeLine(10 * fieldUnit, 12 * fieldUnit, 7 * fieldUnit, 12 * fieldUnit);
        field.strokeLine(7 * fieldUnit, 12 * fieldUnit, 7 * fieldUnit, 13 * fieldUnit);
        field.strokeLine(7 * fieldUnit, 13 * fieldUnit, 5 * fieldUnit, 13 * fieldUnit);
        field.strokeLine(5 * fieldUnit, 13 * fieldUnit, 5 * fieldUnit, 12 * fieldUnit);
        field.strokeLine(5 * fieldUnit, 12 * fieldUnit, 2 * fieldUnit, 12 * fieldUnit);
        field.strokeLine(2 * fieldUnit, 12 * fieldUnit, 2 * fieldUnit, 2 * fieldUnit);


        for (int x = 0; x < 7; x++) {
            for (int y = 0; y < 9; y++) {
                field.fillOval(3 * fieldUnit + x * fieldUnit, 3 * fieldUnit + y * fieldUnit, dotFieldRadius, dotFieldRadius);
            }
        }
        field.fillOval(6 * fieldUnit, 2 * fieldUnit, dotFieldRadius, dotFieldRadius);
        field.fillOval(6 * fieldUnit, 12 * fieldUnit, dotFieldRadius, dotFieldRadius);
    }

    private void drawLine(int xStart, int yStart, int xEnd, int yEnd) {
        field.setStroke(Color.WHITE);
        field.strokeLine(xZero + xStart * fieldUnit, yZero + yStart * fieldUnit,
                xZero + xEnd * fieldUnit, yZero + yEnd * fieldUnit);
    }

    public void drawOldLine(int xStart, int yStart, int xEnd, int yEnd) {
        field.setLineWidth(oldLineWidth);
        drawLine(xStart, yStart, xEnd, yEnd);
    }

    public void drawNewLine(int xStart, int yStart, int xEnd, int yEnd) {
        field.setLineWidth(newLineWidth);
        drawLine(xStart, yStart, xEnd, yEnd);
    }

    public void drawCurrentPosition() {
        field.setFill(Color.WHITE);
        field.fillOval(game.getCurrentPosition().getX() * fieldUnit + xZero - currentPositionRadius / 2,
                game.getCurrentPosition().getY() * fieldUnit + yZero - currentPositionRadius / 2,
                currentPositionRadius,
                currentPositionRadius);
    }

    public void drawOldMoves() {
        if (game.oldMoves.size() >= 2) {
            for (int i = 1; i < game.oldMoves.size(); i++) {
                Point start = game.oldMoves.get(i);
                Point end = game.oldMoves.get((i - 1));
                drawOldLine(start.getX(), start.getY(), end.getX(), end.getY());
            }
        }
    }

    public void drawNewMoves() {
        if (!game.newMoves.isEmpty()) {
            Point start = game.oldMoves.get(game.oldMoves.size() - 1);
            Point end = game.newMoves.get(0);
            drawNewLine(start.getX(), start.getY(), end.getX(), end.getY());
            if (game.newMoves.size() > 1)
                for (int p = 1; p < game.newMoves.size(); p++) {
                    start = game.newMoves.get(p - 1);
                    end = game.newMoves.get(p);
                    drawNewLine(start.getX(), start.getY(), end.getX(), end.getY());
                }
        }
    }

    public void buttonAction(int direction) {
        game.executeMove(direction, true);
        drawEmptyField();
        drawOldMoves();
        drawNewMoves();
        drawCurrentPosition();
        refreshBUttons();

        boolean waitForServerResponse = true;
        if (!game.amIStuck() && !game.amILoose() && !game.amIWin() && game.isPossibleToBounce()) {
            System.out.println("Wykonaj następny ruch!");
        } else {
            if (game.amIWin()) {
                System.out.println("Gratulacje, wygrałes!");
                waitForServerResponse = false;
                setMessageLabelIWin();

            } else if (game.amILoose() || game.amIStuck()) {
                System.out.println("Przegrałes!");
                waitForServerResponse = false;
                setMessageLabelILoose();
            } else {
                setmessageLabelOpponentTurn();
                System.out.println("Wykonałeś swoje ruchy, czekaj na przeciwnika");
            }
            StringBuilder messageBuilder =
                    new StringBuilder("3-" +
                            Connection.getClientId() + "-" +
                            game.getGameNo() + "-" +
                            game.getGameSeatNo() + "-" +
                            game.getNewMovesAsDirections());
            Connection.writeMoves(this.game, this, messageBuilder.toString(),waitForServerResponse);
        }
    }

    public void refreshBUttons() {
        AllowedMovesDirections allowedMovesDirections = game.getAllowedMovesFromCurrentPosition();
        allowedMovesDirections.printAllowedDirections();
        buttonUp.setDisable(true);
        buttonUpRight.setDisable(true);
        buttonRight.setDisable(true);
        buttonDownRight.setDisable(true);
        buttonDown.setDisable(true);
        buttonDownLeft.setDisable(true);
        buttonLeft.setDisable(true);
        buttonUpLeft.setDisable(true);

        if (game.isMyTurn()) {
            setmessageLabelMyTurn();
            if (allowedMovesDirections.direction[Direction.UP]) {
                buttonUp.setDisable(false);
            }
            if (allowedMovesDirections.direction[Direction.UP_RIGHT]) {
                buttonUpRight.setDisable(false);
            }
            if (allowedMovesDirections.direction[Direction.RIGHT]) {
                buttonRight.setDisable(false);
            }
            if (allowedMovesDirections.direction[Direction.DOWN_RIGHT]) {
                buttonDownRight.setDisable(false);
            }
            if (allowedMovesDirections.direction[Direction.DOWN]) {
                buttonDown.setDisable(false);
            }
            if (allowedMovesDirections.direction[Direction.DOWN_LEFT]) {
                buttonDownLeft.setDisable(false);
            }
            if (allowedMovesDirections.direction[Direction.LEFT]) {
                buttonLeft.setDisable(false);
            }
            if (allowedMovesDirections.direction[Direction.UP_LEFT]) {
                buttonUpLeft.setDisable(false);
            }
        } else {
            if (game.amIWin() || (game.amIStuck() && !game.getItWasMyMove())) {
                setMessageLabelIWin();
                setEnableExitBUtton();
            } else if (game.amILoose() || (game.amIStuck() && game.getItWasMyMove())) {//sprawdzam czy amIStuck() także ponieważ mogl sie przeciwnik zaklinować
                setMessageLabelILoose();
                setEnableExitBUtton();
            } else {
                setmessageLabelMyTurn();
            }

        }

    }

    public void drawUp() {
        buttonAction(Direction.UP);
    }

    public void drawUpRight() {
        buttonAction(Direction.UP_RIGHT);
    }
    @FXML
    public void drawRight() {
        buttonAction(Direction.RIGHT);
    }
    @FXML
    public void drawDownRight() {
        buttonAction(Direction.DOWN_RIGHT);
    }
    @FXML
    public void drawDown() {
        buttonAction(Direction.DOWN);
    }
    @FXML
    public void drawDownLeft() {
        buttonAction(Direction.DOWN_LEFT);
    }
    @FXML
    public void drawLeft() {
        buttonAction(Direction.LEFT);
    }
    @FXML
    public void drawUpLeft() {
        buttonAction(Direction.UP_LEFT);
    }
    @FXML
    public void goToChooseSeatScreen(){
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
    public void setmessageLabelMyTurn() {
        labelMessage.setTextFill(Color.BLACK);
        labelMessage.setText("Twój ruch!");
    }

    public void setmessageLabelOpponentTurn() {
        labelMessage.setTextFill(Color.ORANGE);
        labelMessage.setText("Czekaj na ruch przeciwnika!");
    }

    public void setMessageLabelIWin() {
        labelMessage.setTextFill(Color.GREEN);
        labelMessage.setText("Wygrałeś!");
    }

    public void setMessageLabelILoose() {
        labelMessage.setTextFill(Color.RED);
        labelMessage.setText("Przegrałeś!");
    }

    public void setMessageLabelOpponentDisconected(){
        labelMessage.setTextFill(Color.BLUE);
        labelMessage.setText("Przeciwnik się rozłączył! Koniec gry");
    }

    public void setPlayerSeatsLabels() {
        String myGate = "Twoja bramka";
        String opponentGate = "Bramka przeciwnika";
        if (game.getGameSeatNo() == 0) {
            labelPlayerOneSeat.setText(myGate);
            labelPlayerTwoSeat.setText(opponentGate);
        } else {
            labelPlayerOneSeat.setText(opponentGate);
            labelPlayerTwoSeat.setText(myGate);
        }
    }

    public void setEnableExitBUtton(){
        buttonExit.setDisable(false);
    }
}
