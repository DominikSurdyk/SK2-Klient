package engine;

import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.abs;

public class Game {
    /**
     * kierunki ruchów
     * 0 - gora
     * 1 - gora prawa
     * 2 - prawo
     * 3 - dol prawo
     * 4 - dol
     * 5 - dol lewo
     * 6 - lewo
     * 7 - gora lewa
     * mozliwosc ruchu - true / false
     */

    private boolean[][][] allowedMoves = new boolean[9][13][8];
    public List<Point> oldMoves = new LinkedList<>();
    public List<Point> newMoves = new LinkedList<>();
    private Point currentPosition = new Point(4, 6);
    private int gameSeatNo;
    private int gameNo;
    private boolean myTurn;

    public Game(int gameNo, int gameSeatNo, boolean makeFirstMove) {
        this.setGameNo(gameNo);
        this.setGameSeatNo(gameSeatNo);
        prepareAllowedMovesToGame(makeFirstMove);
        System.out.println("Stworzono model gry!");
    }

    public void prepareAllowedMovesToGame(boolean makeFirstMove) {
        //clear all moves on field
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 13; ++j) {
                for (int k = 0; k < 8; ++k) {
                    allowedMoves[i][j][k] = false;
                }
            }
        }

        //set middle part of field
        for (int l = 1; l <= 7; ++l) {
            for (int i = 2; i <= 10; ++i) {
                for (int j = 0; j < 8; ++j) {
                    allowedMoves[l][i][j] = true;
                }
            }
        }

        //vertical borders
        for (int m = 2; m <= 10; ++m) {
            //set left border
            allowedMoves[0][m][Direction.UP_RIGHT] = true;
            allowedMoves[0][m][Direction.RIGHT] = true;
            allowedMoves[0][m][Direction.DOWN_RIGHT] = true;

            //set rigt border
            allowedMoves[8][m][Direction.DOWN_LEFT] = true;
            allowedMoves[8][m][Direction.LEFT] = true;
            allowedMoves[8][m][Direction.UP_LEFT] = true;
        }

        //horizontal borders
        for (int n = 1; n <= 2; ++n) {
            //left-up
            allowedMoves[n][1][Direction.DOWN_RIGHT] = true;
            allowedMoves[n][1][Direction.DOWN] = true;
            allowedMoves[n][1][Direction.DOWN_LEFT] = true;
            //right-up
            allowedMoves[n + 5][1][Direction.DOWN_RIGHT] = true;
            allowedMoves[n + 5][1][Direction.DOWN] = true;
            allowedMoves[n + 5][1][Direction.DOWN_LEFT] = true;
            //left-down
            allowedMoves[n][11][Direction.UP] = true;
            allowedMoves[n][11][Direction.UP_LEFT] = true;
            allowedMoves[n][11][Direction.UP_RIGHT] = true;

            //right-down
            allowedMoves[n + 5][11][Direction.UP] = true;
            allowedMoves[n + 5][11][Direction.UP_RIGHT] = true;
            allowedMoves[n + 5][11][Direction.UP_LEFT] = true;
        }

        //gates-line
        for (int i1 = 3; i1 <= 5; ++i1) {
            for (int i = 0; i < 8; ++i) {
                allowedMoves[i1][1][i] = true;
                allowedMoves[i1][11][i] = true;
            }

        }
        //gates-line-up-left
        allowedMoves[3][1][Direction.UP] = false;
        allowedMoves[3][1][Direction.UP_LEFT] = false;
        allowedMoves[3][1][Direction.LEFT] = false;

        //gates-line-up-right
        allowedMoves[5][1][Direction.UP] = false;
        allowedMoves[5][1][Direction.UP_RIGHT] = false;
        allowedMoves[5][1][Direction.RIGHT] = false;

        //gates-line-down-left
        allowedMoves[3][11][Direction.DOWN] = false;
        allowedMoves[3][11][Direction.DOWN_LEFT] = false;
        allowedMoves[3][11][Direction.LEFT] = false;

        //gates-line-down-right
        allowedMoves[5][11][Direction.RIGHT] = false;
        allowedMoves[5][11][Direction.DOWN_RIGHT] = false;
        allowedMoves[5][11][Direction.DOWN] = false;

        //corners
        allowedMoves[0][1][Direction.DOWN_RIGHT] = true;
        allowedMoves[8][1][Direction.DOWN_LEFT] = true;
        allowedMoves[0][11][Direction.UP_RIGHT] = true;
        allowedMoves[8][11][Direction.UP_LEFT] = true;

        //ustawienie pierwszego punktu oraz sprawienie ze sie zaczyna
        myTurn = makeFirstMove;
        oldMoves.add(new Point(4, 6));
    }

    public static int getMoveDirectionBetweenTwoPoints(Point start, Point end) {
        int deltaX = end.getX() - start.getX();
        int deltaY = end.getY() - start.getY();

        int result = -1;
        if ((abs(deltaX) > 1) || (abs(deltaY)) > 1 || (deltaX == 0 && deltaY == 0)) {
            result = -1;
        } else {
            if (deltaX == 0 && deltaY == -1) {
                result = Direction.UP;
            } else if (deltaX == 1 && deltaY == -1) {
                result = Direction.UP_RIGHT;
            } else if (deltaX == 1 && deltaY == 0) {
                result = Direction.RIGHT;
            } else if (deltaX == 1 && deltaY == 1) {
                result = Direction.DOWN_RIGHT;
            } else if (deltaX == 0 && deltaY == 1) {
                result = Direction.DOWN;
            } else if (deltaX == -1 && deltaY == 1) {
                result = Direction.DOWN_LEFT;
            } else if (deltaX == -1 && deltaY == 0) {
                result = Direction.LEFT;
            } else if (deltaX == -1 && deltaY == -1) {
                result = Direction.UP_LEFT;
            }
        }
        return result;
    }

    public boolean isMyTurn() {
        return myTurn;
    }

    public void setMyTurn(boolean makeNextMove) {
        this.myTurn = makeNextMove;
    }

    public void setMovePermission(Point point, int direction, boolean value) {
        this.allowedMoves[point.getX()][point.getY()][direction] = value;
    }

    public AllowedMovesDirections getAllowedMovesFromCurrentPosition() {
        AllowedMovesDirections allowedMovesDirections = new AllowedMovesDirections();
        int x = getCurrentPosition().getX();
        int y = getCurrentPosition().getY();
        for (int p = 0; p < 8; p++) {
            allowedMovesDirections.direction[p] = allowedMoves[x][y][p];
            if (allowedMoves[x][y][p]) {

                //System.out.println("mozliwy kierunek ruchu: " + p);
            }
        }
        return allowedMovesDirections;
    }

    //sprawdzam czy mozna sie odbic od punktu w korym akutalnie sie znajduje
    public boolean isPossibleToBounce() {
        AllowedMovesDirections allowedMovesDirections = getAllowedMovesFromCurrentPosition();
        return allowedMovesDirections.isPossibleToBounce();
    }

    //sprawdzam czy utknalem
    public boolean amIStuck() {
        AllowedMovesDirections allowedMovesDirections = getAllowedMovesFromCurrentPosition();
        return allowedMovesDirections.amIStuck();
    }

    public void addOldMove(Point point) {
        oldMoves.add(point);
        if (oldMoves.size() > 1) {
            Point lastPoint = oldMoves.get(oldMoves.size() - 2);
            int direction = getMoveDirectionBetweenTwoPoints(lastPoint, point);
            int oppositeDirection = (direction + 4) % 8;

            setMovePermission(lastPoint, direction, false);
            setMovePermission(point, oppositeDirection, false);
        }
        setCurrentPosition(point);
    }


    public void addNewMove(Point point, boolean isThisMyMove) {
        int direction;
        int oppositeDirection;
        Point lastPoint;
        if (newMoves.size() < 1) {
            lastPoint = oldMoves.get(oldMoves.size() - 1);
        } else {
            lastPoint = newMoves.get(newMoves.size() - 1);
        }
        newMoves.add(point);
        refreshCurrentPosition();
        direction = getMoveDirectionBetweenTwoPoints(lastPoint, point);
        oppositeDirection = (direction + 4) % 8;

        setMovePermission(lastPoint, direction, false);
        setMovePermission(point, oppositeDirection, false);
        if (isThisMyMove) {
            if (amILoose() || amIWin() || amIStuck() || !isPossibleToBounce()) {
                myTurn = false;
            } else {
                myTurn = true;
            }

            System.out.print("Poprzedni Punkt: " + lastPoint.getX() + "," + lastPoint.getY() + ". Możliwe kierunki po wykonaniu ruchu: ");
            for (int p = 0; p < 8; p++) {
                if (allowedMoves[lastPoint.getX()][lastPoint.getY()][p]) {
                    System.out.print(p + " ");
                }
            }
            System.out.print("\n");
            System.out.print("Nowy Punkt: " + point.getX() + "," + point.getY() + ". Możliwe kierunki po wykonaniu ruchu: ");
            for (int p = 0; p < 8; p++) {
                if (allowedMoves[point.getX()][point.getY()][p]) {
                    System.out.print(p + " ");
                }
            }
        }

    }

    public void executeMove(int direction, boolean isThisMyMove) {
        if (direction == Direction.UP) {
            addNewMove(new Point(currentPosition.getX(), currentPosition.getY() - 1), isThisMyMove);
        }
        if (direction == Direction.UP_RIGHT) {
            addNewMove(new Point(currentPosition.getX() + 1, currentPosition.getY() - 1), isThisMyMove);
        }
        if (direction == Direction.RIGHT) {
            addNewMove(new Point(currentPosition.getX() + 1, currentPosition.getY()), isThisMyMove);
        }
        if (direction == Direction.DOWN_RIGHT) {
            addNewMove(new Point(currentPosition.getX() + 1, currentPosition.getY() + 1), isThisMyMove);
        }
        if (direction == Direction.DOWN) {
            addNewMove(new Point(currentPosition.getX(), currentPosition.getY() + 1), isThisMyMove);
        }
        if (direction == Direction.DOWN_LEFT) {
            addNewMove(new Point(currentPosition.getX() - 1, currentPosition.getY() + 1), isThisMyMove);
        }
        if (direction == Direction.LEFT) {
            addNewMove(new Point(currentPosition.getX() - 1, currentPosition.getY()), isThisMyMove);
        }
        if (direction == Direction.UP_LEFT) {
            addNewMove(new Point(currentPosition.getX() - 1, currentPosition.getY() - 1), isThisMyMove);
        }
    }

    public Point getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Point currentPosition) {
        this.currentPosition = currentPosition;
    }

    public void refreshCurrentPosition() {
        if (newMoves.isEmpty()) {
            currentPosition = oldMoves.get(oldMoves.size() - 1);
        } else {
            currentPosition = newMoves.get(newMoves.size() - 1);
        }
    }

    public void deleteNewMoves() {
        newMoves.clear();
    }

    //0 - wygral gracz z miejsca 0. 1 - wygral gracz z miejsca 1. -1  - nikt nie wygral
    private int isSomeoneWin() {
        int result = -1;
        if (currentPosition.getY() == 12 && (
                currentPosition.getX() == 3 ||
                        currentPosition.getX() == 4 ||
                        currentPosition.getX() == 5)) {
            result = 0;
        }

        if (currentPosition.getY() == 0 && (
                currentPosition.getX() == 3 ||
                        currentPosition.getX() == 4 ||
                        currentPosition.getX() == 5)) {
            result = 1;
        }
        return result;

    }

    public boolean amIWin() {
        boolean result = false;
        if (getGameSeatNo() == 0) {
            result = (0 == isSomeoneWin());
        }

        if (getGameSeatNo() == 1) {
            result = (1 == isSomeoneWin());
        }
        return result;
    }

    public boolean amILoose() {
        boolean result = false;
        if (getGameSeatNo() == 0) {
            result = 1 == isSomeoneWin();
        }

        if (getGameSeatNo() == 1) {
            result = 0 == isSomeoneWin();
        }
        return result;
    }

    public String getNewMovesAsDirections() {
        StringBuilder responseBuilder = new StringBuilder();
        Point lastPoint = oldMoves.get(oldMoves.size() - 1);
        Point newPoint = newMoves.get(0);

        int direction = getMoveDirectionBetweenTwoPoints(lastPoint, newPoint);

        responseBuilder.append(direction);

        if (newMoves.size() > 1) {
            for (int i = 1; i < newMoves.size(); i++) {
                lastPoint = newMoves.get(i - 1);
                newPoint = newMoves.get(i);
                direction = getMoveDirectionBetweenTwoPoints(lastPoint, newPoint);
                responseBuilder.append(direction);
            }
        }
        return responseBuilder.toString();

    }

    public int getGameSeatNo() {
        return gameSeatNo;
    }

    public void setGameSeatNo(int gameSeatNo) {
        this.gameSeatNo = gameSeatNo;
    }

    public int getGameNo() {
        return gameNo;
    }

    public void setGameNo(int gameNo) {
        this.gameNo = gameNo;
    }

    public void transferNewMovesToOldMoves() {
        for (Point point : newMoves) {
            addOldMove(point);
        }
        newMoves.clear();
    }
}
