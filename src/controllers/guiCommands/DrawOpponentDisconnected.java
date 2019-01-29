package controllers.guiCommands;

import controllers.GameScreenController;

public class DrawOpponentDisconnected implements Runnable{

    GameScreenController gameScreenControllerReference ;

    @Override
    public void run() {
        gameScreenControllerReference.setMessageLabelOpponentDisconected();
        gameScreenControllerReference.setEnableExitBUtton();
    }

    public DrawOpponentDisconnected(GameScreenController gameScreenControllerReference) {
        this.gameScreenControllerReference = gameScreenControllerReference;
    }
}
