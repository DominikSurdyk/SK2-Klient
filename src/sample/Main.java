package sample;

import connection.Connection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    Connection connection;

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("../fxml/MainScreen.fxml"));
        primaryStage.setTitle("Pilka nozna");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();


    }

    public static void main(String[] args) {
        launch(args);
//        Connection connection = new Connection();
//        connection.writeData("tescik\ntescik2");
//        try {
//            TimeUnit.SECONDS.sleep(5);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }







        // connection.closeConnection();
//        System.out.println(connection.readData());
//        System.out.println("koniec");
    }
}
