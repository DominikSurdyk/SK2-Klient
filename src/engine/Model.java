package engine;

public class Model {

    private static Game game;

    public static void newGame(int gameNo, int gameSeatNo, boolean makeFirstMove) {
        game = new Game(gameNo,gameSeatNo, makeFirstMove);
    }

    public static Game getGame() {
        return game;
    }
}
