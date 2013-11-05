package com.osten.halp.views.home;

import com.osten.halp.views.main.MainWindowView;
import com.osten.halp.utils.FXMLUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-10-15
 * Time: 15:43
 * To change this template use File | Settings | File Templates.
 */
public class HomeView extends HBox implements Initializable{

    private static Logger logger = LoggerFactory.getLogger(HomeView.class);

    @FXML
    WebView webView;

    WebEngine webEngine;
    MainWindowView parentView;

    public HomeView(MainWindowView parentView){

        this.parentView = parentView;
        FXMLUtils.load( this );
    }

    @FXML
    public void handleWhat(ActionEvent event) {

        webEngine.load( getClass().getResource("/html/what.html").toString() );
    }

    @FXML
       public void handleWhy(ActionEvent event) {

        webEngine.load( getClass().getResource("/html/why.html").toString() );

    }

    @FXML
    public void handleHow(ActionEvent event) {

        webEngine.load( getClass().getResource("/html/how.html").toString() );

    }

    @FXML
    public void handleBehaviour(ActionEvent event){
       webEngine.load( getClass().getResource("/html/behaviour.html").toString() );
    }

    @FXML
    public void handleGo(ActionEvent event ){
         parentView.getSelectionModel().select( 1 );
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
         webEngine = webView.getEngine();
         webEngine.load( getClass().getResource("/html/what.html").toString() );
    }
}
