package com.osten.halp.views.analysis;

import com.osten.halp.api.model.gui.PopulatableView;
import com.osten.halp.api.model.profiling.AdaptiveFilter;
import com.osten.halp.api.model.shared.DataModel;
import com.osten.halp.api.model.shared.FilterModel;
import com.osten.halp.api.model.shared.ProfileModel;
import com.osten.halp.api.model.statistics.DataPoint;
import com.osten.halp.api.model.statistics.Statistic;
import com.osten.halp.impl.statistics.LongStatistic;
import com.osten.halp.utils.FXMLUtils;
import com.osten.halp.views.main.MainWindowView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;

import java.net.URL;
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
    ToggleButton filterButton;

	@FXML
	LineChart<Number,Number> lineChart;

	public AnalysisView( MainWindowView parentView )
	{
		this.parentView = parentView;

		FXMLUtils.load( this );
	}

    @FXML
    public void handleFilterButton( ActionEvent actionEvent ){
        if(filterButton.selectedProperty().get()){
            for( Statistic<Long> data : getDataModel().getData() ){
                for( AdaptiveFilter<Long> filter : getFilterModel().getFiltersByStatisticName( data.getName() )){
                    lineChart.getData().add( toSeries( filter.getEstimates() ) );
                }
            }
        }else{
            rebuildLineChart();
        }
    }

	@Override
	public void initialize( URL url, ResourceBundle resourceBundle )
	{
         filterButton.selectedProperty().addListener(new ChangeListener<Boolean>(){
             @Override
             public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
                 if(newValue){
                     filterButton.setText( "Hide Filters" );
                 }else{
                     filterButton.setText( "Show Filters" );
                 }
             }
         });
    }


	private XYChart.Series toSeries( Statistic<Long> statistic )
	{
		XYChart.Series<Number, Number> series = new XYChart.Series<Number,Number>();
		series.setName( statistic.getName() );

		List<DataPoint<Long>> data = statistic.getData();

		for ( int i = 0; i < data.size(); i++ ){
			 series.getData().add( new XYChart.Data<Number, Number>( i+0.05, data.get( i ).getData() ) );
		}

		return series;
	}

    @Override
    public void populate(DataModel<Long> properties, FilterModel<Long> filterModel, ProfileModel<Long> profileModel) {

        rebuildLineChart();
        properties.printModel();
    }

    private void rebuildLineChart(){
        lineChart.getData().clear();
        for ( Statistic<Long> statistic : getDataModel().getData() ){
            lineChart.getData().add( toSeries( statistic ) );
        }
    }

    @Override
	public DataModel<Long> getDataModel()
	{
		return parentView.getDataModel();
	}

    @Override
    public FilterModel<Long> getFilterModel() {
        return parentView.getFilterModel();
    }

    @Override
    public ProfileModel<Long> getProfileModel() {
        return parentView.getProfileModel();
    }
}
