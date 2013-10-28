package com.osten.halp.gui.analysis;

import com.osten.halp.gui.main.MainWindowView;
import com.osten.halp.utils.FXMLUtils;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-10-15
 * Time: 15:18
 * To change this template use File | Settings | File Templates.
 */
public class AnalysisView extends HBox implements Initializable{

    MainWindowView parentView;

    public AnalysisView( MainWindowView parentView){
        this.parentView = parentView;
        FXMLUtils.load( this );
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
