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
     * <p>
     * mozliwosc ruchu - true / false
     */

    private boolean[][][] allowedMoves = new boolean[9][13][8];
    public List<Point> oldMoves = new LinkedList<>();
    public List<Point> newMoves = new LinkedList<>();
    private Point currentPosition = new Point(4, 6);
    private int gameSeatNo;
    private int gameNo;
    private boolean myTurn;

    public Game(int gameNo,int gameSeatNo, boolean makeFirstMove) {
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
            allowedMoves[0][m][1] = true;
            allowedMoves[0][m][2] = true;
            allowedMoves[0][m][3] = true;

            //set rigt border
            allowedMoves[8][m][5] = true;
            allowedMoves[8][m][6] = true;
            allowedMoves[8][m][7] = true;
        }

        //horizontal borders
        for (int n = 1; n <= 2; ++n) {
            //left-up
            allowedMoves[n][1][3] = true;
            allowedMoves[n][1][4] = true;
            allowedMoves[n][1][5] = true;
            //right-up
            allowedMoves[n + 5][1][3] = true;
            allowedMoves[n + 5][1][4] = true;
            allowedMoves[n + 5][1][5] = true;
            //left-down
            allowedMoves[n][11][0] = true;
            allowedMoves[n][11][1] = true;
            allowedMoves[n][11][7] = true;

            //right-down
            allowedMoves[n + 5][11][0] = true;
            allowedMoves[n + 5][11][1] = true;
            allowedMoves[n + 5][11][7] = true;
        }

        //gates-line
        for (int i1 = 3; i1 <= 5; ++i1) {
            for (int i = 0; i < 8; ++i) {
                allowedMoves[i1][1][i] = true;
                allowedMoves[i1][11][i] = true;
            }

        }
        //gates-line-up-left
        allowedMoves[3][1][0] = false;
        allowedMoves[3][1][7] = false;
        allowedMoves[3][1][6] = false;

        //gates-line-up-right
        allowedMoves[5][1][0] = false;
        allowedMoves[5][1][1] = false;
        allowedMoves[5][1][2] = false;

        //gates-line-down-left
        allowedMoves[3][11][4] = false;
        allowedMoves[3][11][5] = false;
        allowedMoves[3][11][6] = false;

        //gates-line-down-right
        allowedMoves[5][11][2] = false;
        allowedMoves[5][11][3] = false;
        allowedMoves[5][11][4] = false;

        //corners
        allowedMoves[0][1][3] = true;
        allowedMoves[8][1][5] = true;
        allowedMoves[0][11][1] = true;
        allowedMoves[8][11][7] = true;

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
                result = 0;
            } else if (deltaX == 1 && deltaY == -1) {
                result = 1;
            } else if (deltaX == 1 && deltaY == 0) {
                result = 2;
            } else if (deltaX == 1 && deltaY == 1) {
                result = 3;
            } else if (deltaX == 0 && deltaY == 1) {
                result = 4;
            } else if (deltaX == -1 && deltaY == 1) {
                result = 5;
            } else if (deltaX == -1 && deltaY == 0) {
                result = 6;
            } else if (deltaX == -1 && deltaY == -1) {
                result = 7;
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


    public void addNewMove(Point point,boolean isThisMyMove) {
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
        if (isThisMyMove){
            if ((!amILoose() || !amIWin() || !amIStuck()) && isPossibleToBounce()){
                myTurn = true;
            }else{
                myTurn = false;
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

    public void executeMove(int direction,boolean isThisMyMove) {
        if (direction == 0) {
            addNewMove(new Point(currentPosition.getX(), currentPosition.getY() - 1),isThisMyMove);
        }
        if (direction == 1) {
            addNewMove(new Point(currentPosition.getX() + 1, currentPosition.getY() - 1),isThisMyMove);
        }
        if (direction == 2) {
            addNewMove(new Point(currentPosition.getX() + 1, currentPosition.getY()),isThisMyMove);
        }
        if (direction == 3) {
            addNewMove(new Point(currentPosition.getX() + 1, currentPosition.getY() + 1),isThisMyMove);
        }
        if (direction == 4) {
            addNewMove(new Point(currentPosition.getX(), currentPosition.getY() + 1),isThisMyMove);
        }
        if (direction == 5) {
            addNewMove(new Point(currentPosition.getX() - 1, currentPosition.getY() + 1),isThisMyMove);
        }
        if (direction == 6) {
            addNewMove(new Point(currentPosition.getX() - 1, currentPosition.getY()),isThisMyMove);
        }
        if (direction == 7) {
            addNewMove(new Point(currentPosition.getX() - 1, currentPosition.getY() - 1),isThisMyMove);
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


    //0 - wygral gracz z miejsca 0. 1 - wygral gracz z miejsca 1. 2  - nikt nie wygral
    private int isSomeoneWin() {
        int result = 2;
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

    public String getNewMovesAsDirections(){
        StringBuilder responseBuilder = new StringBuilder();
        Point lastPoint = oldMoves.get(oldMoves.size()-1);
        Point newPoint = newMoves.get(0);

        int direction = getMoveDirectionBetweenTwoPoints(lastPoint,newPoint);

        responseBuilder.append(direction);

        if (newMoves.size() > 1){
            for (int i = 1; i < newMoves.size(); i++){
                lastPoint = newMoves.get(i-1);
                newPoint = newMoves.get(i);
                direction = getMoveDirectionBetweenTwoPoints(lastPoint,newPoint);
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

    public void transferNewMovesToOldMoves(){
        for (Point point: newMoves) {
            addOldMove(point);
        }
        newMoves.clear();
    }
}
