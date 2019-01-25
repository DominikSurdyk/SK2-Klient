package engine;

public class Model {

    private static Game game;

    public static void newGame(int gameSeatNo, boolean makeFirstMove) {
        game = new Game(gameSeatNo, makeFirstMove);
    }

    public static Game getGame() {
        return game;
    }
}
