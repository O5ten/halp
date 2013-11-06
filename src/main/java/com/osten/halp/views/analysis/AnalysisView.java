package com.osten.halp.views.analysis;

import com.osten.halp.api.model.gui.PopulatableView;
import com.osten.halp.api.model.shared.DataModel;
import com.osten.halp.api.model.shared.FilterModel;
import com.osten.halp.api.model.shared.ProfileModel;
import com.osten.halp.api.model.statistics.DataPoint;
import com.osten.halp.api.model.statistics.Statistic;
import com.osten.halp.impl.shared.LongDataModel;
import com.osten.halp.impl.shared.LongFilterModel;
import com.osten.halp.impl.shared.LongProfileModel;
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

    //Domain references
    private ProfileModel<Long> profileModel;
    private DataModel<Long> dataModel;
    private FilterModel<Long> filterModel;

	public AnalysisView( MainWindowView parentView )
	{
		this.parentView = parentView;
        this.dataModel = new LongDataModel();
        this.filterModel = new LongFilterModel();
        this.profileModel = new LongProfileModel();

		FXMLUtils.load( this );
	}

	@Override
	public void initialize( URL url, ResourceBundle resourceBundle )
	{

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

        this.dataModel = properties;
        this.filterModel = filterModel;
        this.profileModel = profileModel;

        rebuildLineChart();
        properties.printModel();
    }

    private void rebuildLineChart(){
        lineChart.getData().clear();
        for ( Statistic<Long> statistic : dataModel.getData() ){
            lineChart.getData().add( toSeries( statistic ) );
        }
    }

    @Override
	public DataModel<Long> getPropertyModel()
	{
		return parentView.getDataModel();
	}
}
