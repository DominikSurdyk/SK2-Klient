package controllers.guiCommands;

import controllers.GameScreenController;

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
