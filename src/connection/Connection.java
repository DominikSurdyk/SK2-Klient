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

        try {
            InputStream inputStream = clientSocket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String message = reader.readLine();
            System.out.println("Odebrano dane [" + message + "]");
            return message;
        } catch (IOException e) {
            System.out.println("Nie udało się odczyta danych");
            return "fail";
        }

    }

    public static int writeData(String message) {
        try {
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
            writer.println(message.toCharArray());
            System.out.println("wysłano dane [" + message + "]");
            return 0;
        } catch (IOException e) {
            System.out.println("Nie udało się wysłać danych");
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
