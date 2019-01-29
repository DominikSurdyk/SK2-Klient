package connection.commands;

import controllers.GameScreenController;
import controllers.guiCommands.DrawOpponentDisconnected;
import controllers.guiCommands.DrawOpponentMoves;
import engine.Game;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class CommandReadResponseFromServer implements Runnable {
    Socket clientSocket;
    GameScreenController gameScreenControllerReference;
    Game gameReference;

    @Override
    public void run() {
        try {
            InputStream inputStream = clientSocket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String response = reader.readLine();
            System.out.println("Odebrano dane [" + response + "]");

            String[] tokens = response.split("-");
            int responseStatus = Integer.parseInt(tokens[0]);
            if (responseStatus == 0) {
                System.out.println("Przeciwnik się rozłączył. Wygrałeś");
                gameReference.setMyTurn(false);
                Platform.runLater(new DrawOpponentDisconnected(this.gameScreenControllerReference));
            } else {
                for (int i = 0; i < tokens[2].length(); i++) {
                    gameReference.executeMove(Character.getNumericValue(tokens[2].charAt(i)), false);
                }
                gameReference.transferNewMovesToOldMoves();
                int gameStatus = Integer.parseInt(tokens[1]);
                System.out.println("Status gry: " + gameStatus + ". [0] - graj dalej, [1] - wygrałeś, [2]-przegrałeś");
                if (gameStatus == 2) {
                    System.out.println("Przegrałeś!");
                    gameReference.setMyTurn(false);
                } else if (gameStatus == 1) {
                    System.out.println("Wygrałeś");
                    gameReference.setMyTurn(false);
                } else {
                    System.out.println("Gra toczy się dalej!");
                    gameReference.setMyTurn(true);
                }
                Platform.runLater(new DrawOpponentMoves(this.gameScreenControllerReference));
            }
        } catch (IOException e) {
            System.out.println("Nie udało się odczyta danych");
        }
    }

    public CommandReadResponseFromServer(Socket clientSocket, Game gameReference, GameScreenController gameScreenControllerReference) {
        this.gameReference = gameReference;
        this.clientSocket = clientSocket;
        this.gameScreenControllerReference = gameScreenControllerReference;
    }
}
