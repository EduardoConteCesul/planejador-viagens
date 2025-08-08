package com.conteduu.planejadorviagens;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application
{
    @Override
    public void start(Stage stage) throws Exception {
        var loader = new FXMLLoader(getClass().getResource("/com.conteduu.planejadorviagens/trip_view.fxml"));

        stage.setTitle("Trip Planner - MVC");
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }


    public static void main( String[] args ){launch(args);}
}
