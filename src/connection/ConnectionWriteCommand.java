package connection;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionWriteCommand implements Runnable {
    Socket clientSocket;
    String messageIn;
    @Override
    public void run() {
        try {
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
            writer.println(messageIn.toCharArray());
            System.out.println("wysłano dane [" + messageIn + "]");

        } catch (IOException e) {
            System.out.println("Nie udało się wysłać danych");

        }
    }

    public ConnectionWriteCommand( Socket clientSocket, String messageIn){
        this.clientSocket = clientSocket;
        this.messageIn = messageIn;
    }
}
