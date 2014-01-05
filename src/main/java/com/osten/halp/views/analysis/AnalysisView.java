package com.osten.halp.views.analysis;

import com.osten.halp.model.gui.PopulatableView;
import com.osten.halp.model.profiling.AdaptiveFilter;
import com.osten.halp.model.profiling.PointsOfInterest;
import com.osten.halp.model.profiling.Range;
import com.osten.halp.model.shared.DataModel;
import com.osten.halp.model.shared.DetectorModel;
import com.osten.halp.model.shared.FilterModel;
import com.osten.halp.model.shared.ProfileModel;
import com.osten.halp.model.statistics.DataPoint;
import com.osten.halp.model.statistics.Statistic;
import com.osten.halp.utils.FXMLUtils;
import com.osten.halp.views.main.MainWindowView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-10-15
 * Time: 15:18
 * To change this template use File | Settings | File Templates.
 */
public class AnalysisView extends HBox implements Initializable, PopulatableView<Long>
{

	MainWindowView parentView;

	@FXML
	private ToggleButton filterButton;

	@FXML
	private LineChart<Number, Number> lineChart;

	@FXML
	private VBox statisticSelector;

	@FXML
	private TextArea pointsOfInterest;

	@FXML
	private HBox poiVisualizer;

	public AnalysisView( MainWindowView parentView )
	{
		this.parentView = parentView;
		FXMLUtils.load( this );
	}

	@FXML
	public void handleFilterButton( ActionEvent actionEvent )
	{
		rebuildLineChart();
		if( filterButton.selectedProperty().get() )
		{
			for( Statistic<Long> data : getDataModel().getData() )
			{
				if( !getSelectedData().contains( data ) )
				{
					addFiltersByStatistic( data );
				}
			}
		}
	}

	private void addFiltersByStatistic( Statistic<Long> statistic )
	{
		for( AdaptiveFilter<Long> filter : getFilterModel().getFiltersByStatisticName( statistic.getName() ) )
		{
			if( !getSelectedData().contains( statistic.getName() ) )
			{
				filter.getEstimates().setName( statistic.getName() + "->" + filter.getType() + "->" + filter.getEstimates().getAggregatedType() );
				lineChart.getData().add( asSeries( filter.getEstimates() ) );
			}
		}
	}

	private List<String> getSelectedData()
	{
		List<String> selectedList = new ArrayList<String>();
		for( Node child : statisticSelector.getChildren() )
		{
			if( child.getId().equals( "hideDataButton" ) )
			{
				ToggleButton button = ( ToggleButton )child;
				if( button.selectedProperty().get() )
				{
					selectedList.add( button.getText() );
				}
			}
		}
		return selectedList;
	}

	@Override
	public void initialize( URL url, ResourceBundle resourceBundle )
	{
		filterButton.selectedProperty().addListener( new ChangeListener<Boolean>()
		{
			@Override
			public void changed( ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue )
			{
				if( newValue )
				{
					filterButton.setText( "Hide Filters" );
				}
				else
				{
					filterButton.setText( "Display Filters" );
				}
			}
		} );
	}

	private XYChart.Series asSeries( Statistic<Long> statistic )
	{
		XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
		series.setName( statistic.getName() );

		List<DataPoint<Long>> data = statistic.getDataAsList();

		for( int i = 0; i < data.size(); i++ )
		{
			series.getData().add( new XYChart.Data<Number, Number>( i + 0.05, data.get( i ).getValue() ) );
		}

		return series;
	}

	@Override
	public void populate( DataModel<Long> dataModel, FilterModel<Long> filterModel, DetectorModel<Long> detectorModel, ProfileModel<Long> profileModel )
	{

		rebuildStatisticSelector();
		rebuildLineChart();
		rebuildPointsOfInterest();
		dataModel.printModel();
	}

	private void rebuildPointsOfInterest()
	{

		pointsOfInterest.clear();

		PointsOfInterest poi = getProfileModel().getPointsOfInterests();

		if( poi.getPointOfInterest().size() > 1 || poi.getPointOfInterest().get( 0 ).getStop() > 0)
		{
			pointsOfInterest.clear();
			pointsOfInterest.appendText( " Based on " + poi.getProfile().toString() + "-profiling\n==================================\nPoI severity-level --> # { Time started --> Time Stopped }\n==================================\n" );
			poiVisualizer.getChildren().clear();
			int i = 0;
			for( Range range : poi.getPointOfInterest() )
			{
				   if( range.getStart() <= range.getStop()){
					pointsOfInterest.appendText( poi.getRelevance( range ) + " --> #" + i + " { " + range.getStart() + " --> " + range.getStop() + " }\n" );
				}
				i++;
			}
			pointsOfInterest.appendText( "==================================" );

			poiVisualizer.getChildren().clear();
			long lastDetection = 0;
			int accumulator = 0;
			for( int j = 0; j < poi.getPointOfInterest().size(); j++) {
				Range range = poi.getPointOfInterest().get( j );
				String color = "green";
				switch( poi.getRelevance( range ) ){
					case Irrelevant:
						color = "yellow";
						break;
					case Moderate:
						color = "red";
						break;
					case Substantial:
						color = "black";
					default:
				}
				Region region = RegionBuilder.create().style( "-fx-background-color: green;" ).maxHeight( 15 ).minWidth( Math.abs( range.getStart() - lastDetection ) ).maxWidth( 50 ).build();
				poiVisualizer.getChildren().add( region );

				if(j == 100){
					poiVisualizer.getChildren().add( RegionBuilder.create().style( "-fx-background-color: black;" ).minHeight( 25 ).maxHeight( 25 ).minWidth( 5 ).build() );
				}

				Region region2 = RegionBuilder.create().style( "-fx-background-color: " + color + ";" ).maxHeight( 15 ).minWidth( range.getStop() - range.getStart() ).maxWidth( 50 ).build();
				poiVisualizer.getChildren().add( region2 );
				lastDetection = range.getStop();
				if( poi.getRelevance( range ) != PointsOfInterest.Relevance.Irrelevant){
					accumulator += range.getStop() - range.getStart();
				}


			}
			double d = Math.round( ( accumulator * 100 ) / poi.getInvolvedStatistics().get( 0 ).size() );
			pointsOfInterest.appendText( "\nBottleneck likelihood ==> { " + d + "% }" );
		}
		if( poi.getPointOfInterest().size() == 1 && poi.getPointOfInterest().get( 0 ).getStop() == 0){
	      pointsOfInterest.clear();
			pointsOfInterest.setText( "No points of interest detected " );
		}
	}

	private void rebuildLineChart()
	{
		lineChart.getData().clear();
		List<String> selectedData = getSelectedData();
		for( Statistic<Long> statistic : getDataModel().getData() )
		{
			if( !selectedData.contains( statistic.getName() ) )
			{
				lineChart.getData().add( asSeries( statistic ) );
			}
		}
	}

	private void rebuildStatisticSelector()
	{
		statisticSelector.getChildren().clear();
		for( Statistic<Long> statistic : getDataModel().getData() )
		{
			final ToggleButton statisticButton = ToggleButtonBuilder.create().prefWidth( 250 ).id( "hideDataButton" ).text( statistic.getName() ).build();
			statisticButton.selectedProperty().addListener( new ChangeListener<Boolean>()
			{
				@Override
				public void changed( ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean selected )
				{
					if( selected )
					{
						removeSeriesByName( statisticButton.getText() );
						statisticButton.setStyle( "-fx-opacity: 0.2;" );
					}
					else
					{
						Statistic<Long> data = getDataModel().getDataByName( statisticButton.getText() );
						lineChart.getData().add( asSeries( data ) );
						statisticButton.setStyle( "-fx-opacity: 1;" );

						//If the filterButton is selected then we should add the filters for that statistic.
						if( filterButton.selectedProperty().get() )
						{
							addFiltersByStatistic( data );
						}
					}
				}
			} );
			statisticSelector.getChildren().add( statisticButton );
		}
	}

	private void removeSeriesByName( String name )
	{
		List<XYChart.Series> candidatesForRemoval = new ArrayList<XYChart.Series>();
		for( XYChart.Series<Number, Number> currentSeries : lineChart.getData() )
		{
			//The statistic
			if( currentSeries.getName().equals( name ) )
			{
				candidatesForRemoval.add( currentSeries );
				continue;
			}
			//Any filters applied to the chart.
			if( filterButton.selectedProperty().get() && currentSeries.getName().startsWith( name ) )
			{
				candidatesForRemoval.add( currentSeries );
				continue;
			}
		}
		for( XYChart.Series series : candidatesForRemoval )
		{
			lineChart.getData().remove( series );
		}


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
}
