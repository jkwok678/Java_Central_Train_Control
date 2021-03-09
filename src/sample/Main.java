package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.xml.crypto.Data;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        DataCentre data = new DataCentre();
        System.out.println(data.convertStringMessageToStringArray("001,005,040,0,100,100;"));
        data.saveLastDCCCommand("<t 1 01 20 1>");
        //data.sendNFCInstruction();
        GUI gui = new GUI(data);

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(gui.getGUI(), 720, 480));
        primaryStage.show();

    }


    public static void main(String[] args)
    {
        launch(args);
    }
}
