package com.osten.halp.gui.profiling;

import com.osten.halp.gui.main.MainWindowView;
import com.osten.halp.utils.FXMLUtils;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-10-15
 * Time: 15:18
 * To change this template use File | Settings | File Templates.
 */
public class ProfilingView extends HBox implements Initializable{

    MainWindowView parentView;

    public ProfilingView(MainWindowView parentView){
        this.parentView = parentView;
        FXMLUtils.load( this );
    }

	 public void populateView(  ){

	 }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
