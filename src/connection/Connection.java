package connection;

import connection.commands.CommandRead;
import connection.commands.CommandReadResponseFromServer;
import connection.commands.CommandWrite;
import controllers.GameScreenController;
import engine.Game;

import java.io.*;
import java.net.Socket;

public class Connection {

    private Game game;

    private static String SERVER_IP_ADDRESS_DEFAULT = "localhost";
    private static int SERVER_PORT_DEFAULT = 1234;
    private static int CLIENT_ID;

    private static Socket clientSocket;
    public static Thread myThread;
    public static StringBuilder stringBuilderOut;

    public Connection() {
    }

    private static void connect() throws IOException {
        clientSocket = new Socket(SERVER_IP_ADDRESS_DEFAULT, SERVER_PORT_DEFAULT);
    }

    public static void setConfiguration(String serverIpAddress, int serverPort) {
        SERVER_IP_ADDRESS_DEFAULT = serverIpAddress;
        SERVER_PORT_DEFAULT = serverPort;
    }

    public static void setClientId(int id) {
        CLIENT_ID = id;
    }

    public static int getClientId() {
        return CLIENT_ID;
    }

    public static int connect(String serverIpAddress, int serverPort) {
        try {
            setConfiguration(serverIpAddress, serverPort);
            connect();
            System.out.println("Nawiązano połączenia s serwerem o IP: " + serverIpAddress + ", port: " + serverPort);
            return 0;
        } catch (IOException e) {
            System.out.println("Nie udało się nawiązać połączenia z serwerem");
            return -1;
        }
    }

    public static void  readData() {
        stringBuilderOut= new StringBuilder();
        myThread= new Thread(new CommandRead(stringBuilderOut,clientSocket));
        myThread.start();
    }

    public static void writeData(String message) {
        myThread= new Thread(new CommandWrite(clientSocket,message));
        myThread.start();
    }

    public static void readMoves(Game gameReference ,GameScreenController gameScreenControllerReference){
        myThread = new Thread(new CommandReadResponseFromServer(clientSocket,gameReference,gameScreenControllerReference));
        myThread.start();
    }

    public static void writeMoves(Game gameReference ,GameScreenController gameScreenControllerReference, String message){
        try {
            writeData(message);
            myThread.join();
            readMoves( gameReference , gameScreenControllerReference);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static int closeConnection() {
        try {
            clientSocket.close();
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
