package edu.sdsu.cs560.project.fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("WoodenPuzzle.fxml"));
        primaryStage.setTitle("Wooden Puzzle");
        primaryStage.setScene(new Scene(root, 600, 530));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
