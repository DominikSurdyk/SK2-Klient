package connection.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class CommandRead implements Runnable {
    StringBuilder message;
    Socket clientSocket;

    @Override
    public void run() {
        try {
            InputStream inputStream = clientSocket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String response = reader.readLine();
            System.out.println("Odebrano dane [" + response + "]");
            message.append(response);
        } catch (IOException e) {
            System.out.println("Nie udało się odczytac danych");
        }
    }

    public CommandRead(StringBuilder message, Socket clientSocket) {
        this.message = message;
        this.clientSocket = clientSocket;
    }
}
