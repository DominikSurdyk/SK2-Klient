package connection;

import engine.Game;

import java.io.*;
import java.net.Socket;

public class Connection {

    private Game game;

    private static String SERVER_IP_ADDRESS_DEFAULT = "localhost";
    private static int SERVER_PORT_DEFAULT = 1234;
    private static int CLIENT_ID;

    private static Socket clientSocket;


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

    public static String readData() {
        StringBuilder stringBuilder = new StringBuilder();
        Thread thread = new Thread(new ConnectionReadCommand(stringBuilder,clientSocket));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("Błąd w wątku");
        }
        return stringBuilder.toString();
    }

    public static int writeData(String message) {
        Thread thread = new Thread(new ConnectionWriteCommand(clientSocket,message));
        thread.start();
        try {
            thread.join();
            return 0;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return -1;
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
