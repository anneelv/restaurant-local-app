package org.anneelv.burritokingv2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.anneelv.burritokingv2.Database.DatabaseInit;
import java.io.IOException;
/*
* The entry point of BurritoKing application, it sets the first scene
* to be displayed when the application first run
* */
public class Main extends Application {

    /*
    * The method to set the first stage and scene to be displayed
    * */
    @Override
    public void start(Stage primaryStage) throws IOException {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            primaryStage.setTitle("Burrito King");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /*
    * The method to actually run the application and run the database
    * to be prepared during application active session
    * */
    public static void main(String[] args) {
        DatabaseInit.init();
        launch(args);
    }
}