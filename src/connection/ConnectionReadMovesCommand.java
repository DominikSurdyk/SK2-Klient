package connection;

import controllers.GameScreenController;
import controllers.DrawOpponentMoves;
import engine.Game;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ConnectionReadMovesCommand implements Runnable {
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

            for (int i = 0; i < tokens[2].length(); i++) {
                gameReference.executeMove(Character.getNumericValue(tokens[2].charAt(i)), false);
            }
            gameReference.transferNewMovesToOldMoves();

            //tutaj testwow w tym momencie!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! trzeba ustandaryzowac metode!

            int status = Integer.parseInt(tokens[1]);
            System.out.println("Status gry: "+ status + "[0]-graj dalej, [-1] - przegrales, [1]-wygrales");
            if (status == -1) {
                System.out.println("przegrales gre!");
                gameReference.setMyTurn(false);
            } else if (status == 1) {
                System.out.println("Wygrales gre!");
                gameReference.setMyTurn(false);
            } else {
                System.out.println("Gra toczy sie dalej");
                gameReference.setMyTurn(true);
            }


            //gameReference.setMyTurn(true);
            Platform.runLater(new DrawOpponentMoves(this.gameScreenControllerReference));


        } catch (IOException e) {
            System.out.println("Nie udało się odczyta danych");
        }
    }

    public ConnectionReadMovesCommand(Socket clientSocket, Game gameReference, GameScreenController gameScreenControllerReference) {
        this.gameReference = gameReference;
        this.clientSocket = clientSocket;
        this.gameScreenControllerReference = gameScreenControllerReference;
    }
}