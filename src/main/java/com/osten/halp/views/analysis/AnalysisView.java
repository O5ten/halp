package com.osten.halp.views.analysis;

import com.osten.halp.api.model.gui.PopulatableView;
import com.osten.halp.api.model.shared.PropertyModel;
import com.osten.halp.api.model.statistics.DataPoint;
import com.osten.halp.api.model.statistics.Statistic;
import com.osten.halp.utils.FXMLUtils;
import com.osten.halp.views.main.MainWindowView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
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
	LineChart<Number,Number> lineChart;

	public AnalysisView( MainWindowView parentView )
	{
		this.parentView = parentView;
		FXMLUtils.load( this );
	}

	@Override
	public void initialize( URL url, ResourceBundle resourceBundle )
	{

	}

	@Override
	public void populate( PropertyModel<Long> properties )
	{
		System.out.println( "AnalysisView populating view using: " );
		for ( Statistic<Long> statistic : properties.getData()){
			 lineChart.getData().add( toSeries( statistic ) );
		}

		properties.printModel();
	}

	public XYChart.Series toSeries( Statistic<Long> statistic )
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
	public PropertyModel<Long> getPropertyModel()
	{
		return parentView.getPropertyModel();
	}
}
