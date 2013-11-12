package com.osten.halp.views.profiling;

import com.osten.halp.api.model.gui.PopulatableView;
import com.osten.halp.api.model.shared.DataModel;
import com.osten.halp.api.model.shared.FilterModel;
import com.osten.halp.api.model.shared.ProfileModel;
import com.osten.halp.api.model.statistics.Statistic;
import com.osten.halp.utils.FXMLUtils;
import com.osten.halp.views.main.MainWindowView;
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

	@FXML
	private ComboBox<ProfileModel.Profile> profileSelector;

	@FXML
	private ListView<String> statisticSelector;

	@FXML
	private ListView<Statistic.DataType> statisticTypeSelector;

	@FXML
	private VBox adaptiveFilterList;

	@FXML
	private VBox stopRuleList;

	@FXML
	private VBox changeDetectorList;

	public ProfilingView( MainWindowView parentView )
	{
		this.parentView = parentView;
		FXMLUtils.load( this );
	}

	@FXML
	public void handleAnalyze( ActionEvent event )
	{
		getTabsSelectionModel().selectNext();

		parentView.rePopulateViews();
		System.out.println( "(Not really) Applying filters" );
	}

	public SelectionModel getTabsSelectionModel()
	{
		return parentView.getSelectionModel();
	}

	@Override
	public void populate( DataModel<Long> dataModel, FilterModel<Long> filterModel, ProfileModel<Long> profileModel )
	{

		System.out.println( "ProfilingView Repopulated using: " );
		dataModel.printModel();

		statisticSelector.getItems().clear();
		statisticTypeSelector.getItems().clear();

		statisticSelector.getItems().addAll( FXCollections.observableList( getDataModel().getStatisticNames() ) );
		statisticSelector.getSelectionModel().selectFirst();
		statisticTypeSelector.getItems().addAll( Statistic.DataType.values() );
	}

	@Override
	public DataModel<Long> getDataModel()
	{
		return parentView.getDataModel();
	}

	@Override
	public FilterModel<Long> getFilterModel()
	{
		return parentView.getFilterModel();
	}

	@Override
	public ProfileModel<Long> getProfileModel()
	{
		return parentView.getProfileModel();
	}

	@Override
	public void initialize( URL url, ResourceBundle resourceBundle )
	{

		profileSelector.getItems().addAll( ProfileModel.Profile.values() );
		profileSelector.getSelectionModel().selectFirst();

		profileSelector.getSelectionModel().selectedItemProperty().addListener( handleProfileSelected );
		statisticSelector.getSelectionModel().selectedItemProperty().addListener( handleStatisticSelected );
		statisticTypeSelector.getSelectionModel().selectedItemProperty().addListener( handleStatisticTypeSelected );
	}

	public String getSelectedStatistic()
	{
		return statisticSelector.getSelectionModel().getSelectedItem();
	}

	public Statistic.DataType getSelectedStatisticType()
	{
		return statisticTypeSelector.getSelectionModel().getSelectedItem();
	}

	public ProfileModel.Profile getSelectedProfile()
	{
		return profileSelector.getSelectionModel().getSelectedItem();
	}

	private ChangeListener handleStatisticSelected = new ChangeListener()
	{

		@Override
		public void changed( ObservableValue observableValue, Object o, Object o2 )
		{
			if( !statisticSelector.getItems().isEmpty() )
			{
				String selectedItem = getSelectedStatistic();
				adaptiveFilterList.getChildren().clear();
				Statistic.DataType type = getDataModel().getDataByName( selectedItem ).getType();
				statisticTypeSelector.getSelectionModel().select( type );
				System.out.println( getSelectedStatistic().toString() + " of type " + type + " selected." );
			}
		}
	};

	private ChangeListener<Statistic.DataType> handleStatisticTypeSelected = new ChangeListener<Statistic.DataType>()
	{

		@Override
		public void changed( ObservableValue<? extends Statistic.DataType> observableValue, Statistic.DataType oldType, Statistic.DataType newType )
		{
			if( !statisticTypeSelector.getItems().isEmpty() )
			{
				String name = getSelectedStatistic();
				System.out.println( "Defining " + name + " to the statistic-type " + newType );
				getDataModel().getDataByName( name ).setType( newType );

				if( adaptiveFilterList.getChildren().isEmpty() )
				{
					adaptiveFilterList.getChildren().add( new SelectableFilterView( ProfilingView.this ) );
				}
			}
		}
	};

	private ChangeListener handleProfileSelected = new ChangeListener()
	{

		@Override
		public void changed( ObservableValue observableValue, Object o, Object o2 )
		{
			System.out.println( getSelectedProfile().toString() + "-profile selected" );
		}
	};
}
