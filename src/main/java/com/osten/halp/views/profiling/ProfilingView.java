package com.osten.halp.views.profiling;

import com.osten.halp.api.model.gui.PopulatableView;
import com.osten.halp.api.model.profiling.Profile;
import com.osten.halp.api.model.shared.PropertyModel;
import com.osten.halp.views.main.MainWindowView;
import com.osten.halp.utils.FXMLUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionModel;
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
public class ProfilingView extends HBox implements Initializable, PopulatableView<Long>
{

    MainWindowView parentView;

    @FXML private ComboBox profileSelector;

    @FXML private ListView statisticSelector;

    @FXML private ListView statisticTypeSelector;

    @FXML private VBox adaptiveFilterList;

    @FXML private VBox stopRuleList;

    @FXML private VBox changeDetectorList;

    private PropertyModel<Long> properties;

    public ProfilingView(MainWindowView parentView){
        this.parentView = parentView;
        FXMLUtils.load( this );
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
		System.out.println( "ProfilingView Repopulated using: " );
		properties.printModel();

        this.properties = properties;
	}

	@Override
	public PropertyModel<Long> getPropertyModel()
	{
		return parentView.getPropertyModel();
	}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        profileSelector.getItems().addAll( Profile.values() );
        profileSelector.getSelectionModel().selectFirst();
        profileSelector.getSelectionModel().selectedItemProperty().addListener( handleProfileSelected );



    }

    private ChangeListener handleProfileSelected = new ChangeListener(){

        @Override
        public void changed(ObservableValue observableValue, Object o, Object o2) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    };
}
