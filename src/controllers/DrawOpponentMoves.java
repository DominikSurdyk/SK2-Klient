package controllers;

public class DrawOpponentMoves implements Runnable {

    GameScreenController gameScreenControllerReference;

    @Override
    public void run() {
        gameScreenControllerReference.drawEmptyField();
        gameScreenControllerReference.drawOldMoves();
        gameScreenControllerReference.drawCurrentPosition();
        gameScreenControllerReference.refreshBUttons();

    }

    public DrawOpponentMoves(GameScreenController gameScreenControllerReference) {
        this.gameScreenControllerReference = gameScreenControllerReference;
    }
}
