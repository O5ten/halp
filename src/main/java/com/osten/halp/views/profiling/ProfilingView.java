package com.osten.halp.views.profiling;

import com.osten.halp.api.model.gui.PopulatableView;
import com.osten.halp.api.model.shared.DataModel;
import com.osten.halp.api.model.shared.FilterModel;
import com.osten.halp.api.model.shared.ProfileModel;
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
    private FilterModel<Long> filterModel;
    private ProfileModel<Long> profileModel;

    public ProfilingView(MainWindowView parentView){
        this.parentView = parentView;
        FXMLUtils.load( this );
    }

   	@FXML
	public void handleAnalyze(ActionEvent event){
		getTabsSelectionModel().selectNext();
		parentView.rePopulateViews();
        System.out.println( "(Not really) Applying filters" );
	}

	public SelectionModel getTabsSelectionModel(){
		return parentView.getSelectionModel();
	}

    @Override
    public void populate(DataModel<Long> dataModel, FilterModel<Long> filterModel, ProfileModel<Long> profileModel) {

        this.profileModel = profileModel;
        this.filterModel = filterModel;

        //TODO handle filterModel and profilemodel

        System.out.println( "ProfilingView Repopulated using: " );
        dataModel.printModel();
        statisticSelector.getItems().clear();

        statisticTypeSelector.getItems().clear();

        this.dataModel = dataModel;
        statisticSelector.getItems().addAll(FXCollections.observableList( this.dataModel.getStatisticNames() ));
        statisticSelector.getSelectionModel().selectFirst();
        statisticTypeSelector.getItems().addAll( Statistic.DataType.values() );
    }

    @Override
	public DataModel<Long> getPropertyModel()
	{
		return parentView.getDataModel();
	}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        profileSelector.getItems().addAll( ProfileModel.Profile.values() );
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
            System.out.println( statisticSelector.getSelectionModel().getSelectedItem().toString() + " of type " + type + " selected." );
        }
    };

    private ChangeListener<Statistic.DataType> handleStatisticTypeSelected = new ChangeListener<Statistic.DataType>(){


        @Override
        public void changed(ObservableValue<? extends Statistic.DataType> observableValue, Statistic.DataType oldType, Statistic.DataType newType) {
            String name = statisticSelector.getSelectionModel().getSelectedItem();
            dataModel.getDataByName( name ).setType( newType );
            System.out.println( "Defining " + name + " to the statistic-type " + newType );
        }
    };

    private ChangeListener handleProfileSelected = new ChangeListener(){

        @Override
        public void changed(ObservableValue observableValue, Object o, Object o2) {
            System.out.println( profileSelector.getSelectionModel().getSelectedItem().toString() + "-profile selected");
        }
    };
}
