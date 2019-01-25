package controllers;

import connection.Connection;
import engine.AllowedMovesDirections;
import engine.Game;
import engine.Model;
import engine.Point;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;


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

    public void BoiskoScreenController() {
    }

    public void initialize() {
        this.game = Model.getGame();
        field = canvas.getGraphicsContext2D();
        drawEmptyField();
        if (!game.isMyTurn()) {
            System.out.println("czekaj aż drugi gracz dolaczy!");
        } else {
            refreshBUttons();
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
        game.executeMove(direction);
        drawEmptyField();
        drawOldMoves();
        drawNewMoves();
        drawCurrentPosition();
        refreshBUttons();
        if (game.amIWin()) {
            System.out.println("Gratulacje, wygrales!");
        } else if (game.amILoose() || game.amIStuck()) {
            System.out.println("Przegrales!");
        } else if(game.isPossibleToBounce()){
            System.out.println("Wykonaj nastepny ruch!");
        } else {
            System.out.println("Wykonałeś swoje ruchy, czekaj na przeciwnika");
            StringBuilder messageBuilder =
                    new StringBuilder("3-" +
                            Connection.getClientId() + "-" +
                            game.getGameNo() + "-" +
                            game.getGameSeatNo() + "-" +
                            game.getNewMovesAsDirections());
            Connection.writeData(messageBuilder.toString());

            //System.out.println(Connection.readData());
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
            if (allowedMovesDirections.direction[0]) {
                buttonUp.setDisable(false);
            }
            if (allowedMovesDirections.direction[1]) {
                buttonUpRight.setDisable(false);
            }
            if (allowedMovesDirections.direction[2]) {
                buttonRight.setDisable(false);
            }
            if (allowedMovesDirections.direction[3]) {
                buttonDownRight.setDisable(false);
            }
            if (allowedMovesDirections.direction[4]) {
                buttonDown.setDisable(false);
            }
            if (allowedMovesDirections.direction[5]) {
                buttonDownLeft.setDisable(false);
            }
            if (allowedMovesDirections.direction[6]) {
                buttonLeft.setDisable(false);
            }
            if (allowedMovesDirections.direction[7]) {
                buttonUpLeft.setDisable(false);
            }
        }

    }

    public void drawUp() {
        buttonAction(0);
    }

    public void drawUpRight() {
        buttonAction(1);
    }

    public void drawRight() {
        buttonAction(2);
    }

    public void drawDownRight() {
        buttonAction(3);
    }

    public void drawDown() {
        buttonAction(4);
    }

    public void drawDownLeft() {
        buttonAction(5);
    }

    public void drawLeft() {
        buttonAction(6);
    }

    public void drawUpLeft() {
        buttonAction(7);
    }
}
