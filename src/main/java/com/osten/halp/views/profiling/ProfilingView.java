package com.osten.halp.views.profiling;

import com.osten.halp.api.model.gui.PopulatableView;
import com.osten.halp.api.model.profiling.Profile;
import com.osten.halp.api.model.shared.DataModel;
import com.osten.halp.api.model.statistics.Statistic;
import com.osten.halp.views.main.MainWindowView;
import com.osten.halp.utils.FXMLUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.cell.ComboBoxListCell;
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

    @FXML private ListView<String> statisticSelector;

    @FXML private ListView<Statistic.DataType> statisticTypeSelector;

    @FXML private VBox adaptiveFilterList;

    @FXML private VBox stopRuleList;

    @FXML private VBox changeDetectorList;

    private DataModel<Long> dataModel;

    public ProfilingView(MainWindowView parentView){
        this.parentView = parentView;
        FXMLUtils.load( this );
    }

   	@FXML
	public void handleAnalyze(ActionEvent event){
		getTabsSelectionModel().selectNext();
		System.out.println( "(Not really) Applying filters" );
	}

	public SelectionModel getTabsSelectionModel(){
		return parentView.getSelectionModel();
	}

	@Override
	public void populate( DataModel<Long> properties )
	{
		System.out.println( "ProfilingView Repopulated using: " );
		properties.printModel();

        this.dataModel = properties;

        statisticSelector.getItems().clear();
        statisticSelector.getItems().addAll(FXCollections.observableList( dataModel.getStatisticNames() ));
        statisticSelector.getSelectionModel().selectFirst();

        statisticTypeSelector.getItems().clear();
        statisticTypeSelector.getItems().addAll( Statistic.DataType.values() );

    }

	@Override
	public DataModel<Long> getPropertyModel()
	{
		return parentView.getDataModel();
	}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        profileSelector.getItems().addAll( Profile.values() );
        profileSelector.getSelectionModel().selectFirst();
        profileSelector.getSelectionModel().selectedItemProperty().addListener( handleProfileSelected );

        statisticSelector.getSelectionModel().selectedItemProperty().addListener( handleStatisticSelected );
        statisticTypeSelector.getSelectionModel().selectedItemProperty().addListener( handleStatisticTypeSelected );
    }

    private ChangeListener handleStatisticSelected = new ChangeListener(){

        @Override
        public void changed(ObservableValue observableValue, Object o, Object o2) {

            String selectedItem = statisticSelector.getSelectionModel().getSelectedItem();
            Statistic.DataType type = dataModel.getDataByName( selectedItem ).getType();
            statisticTypeSelector.getSelectionModel().select( type );
            System.out.println( statisticSelector.getSelectionModel().getSelectedItem().toString() + " selected, populating filter-view with " + statisticSelector.getSelectionModel().getSelectedItem().toString() );
        }
    };

    private ChangeListener<Statistic.DataType> handleStatisticTypeSelected = new ChangeListener<Statistic.DataType>(){


        @Override
        public void changed(ObservableValue<? extends Statistic.DataType> observableValue, Statistic.DataType oldType, Statistic.DataType newType) {
            String name = statisticSelector.getSelectionModel().getSelectedItem();
            dataModel.getDataByName( name ).setType( newType );
        }
    };

    private ChangeListener handleProfileSelected = new ChangeListener(){

        @Override
        public void changed(ObservableValue observableValue, Object o, Object o2) {
            System.out.println( profileSelector.getSelectionModel().getSelectedItem().toString() + "-profile selected");
        }
    };
}
