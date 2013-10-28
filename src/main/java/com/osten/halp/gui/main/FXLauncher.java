package com.osten.halp.gui.main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-10-15
 * Time: 11:28
 * To change this template use File | Settings | File Templates.
 */
public class FXLauncher extends Application {

    private static Logger logger = LoggerFactory.getLogger(FXLauncher.class);

    public static void main(String[] args){
        launch( args );
    }

    @Override
    public void start(Stage stage) throws Exception{
        stage.setTitle("HALP - Heuristic Algorithms for Lightweight Performancetesting");
        stage.setScene( new Scene(new MainWindowView(), 950, 700) );
        stage.show();
    }
}
