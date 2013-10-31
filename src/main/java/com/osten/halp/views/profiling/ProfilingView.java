package com.osten.halp.views.profiling;

import com.osten.halp.api.model.gui.PopulatableView;
import com.osten.halp.api.model.shared.PropertyModel;
import com.osten.halp.views.main.MainWindowView;
import com.osten.halp.utils.FXMLUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionModel;
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
public class ProfilingView extends HBox implements Initializable, PopulatableView<Long>
{

    MainWindowView parentView;

    public ProfilingView(MainWindowView parentView){
        this.parentView = parentView;
        FXMLUtils.load( this );
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

	@FXML
	public void handleAnalyze(ActionEvent event){
		getTabsSelectionModel().selectNext();
		System.out.println( "Applying filters" );
	}

	public SelectionModel getTabsSelectionModel(){
		return parentView.getSelectionModel();
	}

	@Override
	public void populate( PropertyModel<Long> properties )
	{
		System.out.println( "ProfilingView Repopulating using: " );
		properties.printModel();
	}

	@Override
	public PropertyModel<Long> getPropertyModel()
	{
		return parentView.getPropertyModel();
	}
}
