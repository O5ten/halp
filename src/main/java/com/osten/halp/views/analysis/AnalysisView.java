package com.osten.halp.views.analysis;

import com.osten.halp.api.model.gui.PopulatableView;
import com.osten.halp.api.model.profiling.AdaptiveFilter;
import com.osten.halp.api.model.shared.DataModel;
import com.osten.halp.api.model.shared.DetectorModel;
import com.osten.halp.api.model.shared.FilterModel;
import com.osten.halp.api.model.shared.ProfileModel;
import com.osten.halp.api.model.statistics.DataPoint;
import com.osten.halp.api.model.statistics.Statistic;
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
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleButtonBuilder;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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
public class AnalysisView extends HBox implements Initializable, PopulatableView<Long> {

    MainWindowView parentView;

    @FXML
    private ToggleButton filterButton;

    @FXML
    private LineChart<Number, Number> lineChart;

    @FXML
    private VBox statisticSelector;

    public AnalysisView(MainWindowView parentView) {
        this.parentView = parentView;
        FXMLUtils.load(this);
    }

    @FXML
    public void handleFilterButton(ActionEvent actionEvent) {
        rebuildLineChart();
        if (filterButton.selectedProperty().get()) {
            for (Statistic<Long> data : getDataModel().getData()) {
                if(!getSelectedData().contains( data )){
                    addFiltersByStatistic( data );
                }
            }
        }
    }

    private void addFiltersByStatistic( Statistic<Long> statistic){
        for (AdaptiveFilter<Long> filter : getFilterModel().getFiltersByStatisticName(statistic.getName())) {
            if(!getSelectedData().contains( statistic.getName() )){
                filter.getEstimates().setName(statistic.getName() + "->" + filter.getType() + "->" + filter.getEstimates().getAggregatedType());
                lineChart.getData().add(asSeries(filter.getEstimates()));
            }
        }
    }

    private List<String> getSelectedData(){
        List<String> selectedList = new ArrayList<String>();
        for( Node child : statisticSelector.getChildren() ){
            if(child.getId().equals( "hideDataButton" )){
                ToggleButton button = (ToggleButton)child;
                if(button.selectedProperty().get()){
                    selectedList.add( button.getText() );
                }
            }
        }
        return selectedList;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        filterButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    filterButton.setText("Hide Filters");
                } else {
                    filterButton.setText("Display Filters");
                }
            }
        });
    }

    private XYChart.Series asSeries(Statistic<Long> statistic) {
        XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
        series.setName(statistic.getName());

        List<DataPoint<Long>> data = statistic.getDataAsList();

        for (int i = 0; i < data.size(); i++) {
            series.getData().add(new XYChart.Data<Number, Number>(i + 0.05, data.get(i).getValue()));
        }

        return series;
    }

    @Override
    public void populate(DataModel<Long> dataModel, FilterModel<Long> filterModel, DetectorModel<Long> detectorModel, ProfileModel<Long> profileModel) {

        rebuildStatisticSelector();
        rebuildLineChart();
        dataModel.printModel();
    }

    private void rebuildLineChart() {
        lineChart.getData().clear();
        List<String> selectedData = getSelectedData();
        for (Statistic<Long> statistic : getDataModel().getData()) {
            if(!selectedData.contains( statistic.getName())){
                lineChart.getData().add(asSeries(statistic));
            }
        }
    }

    private void rebuildStatisticSelector() {
        statisticSelector.getChildren().clear();
        for (Statistic<Long> statistic : getDataModel().getData()) {
            final ToggleButton statisticButton = ToggleButtonBuilder.create().prefWidth(250).id("hideDataButton").text(statistic.getName()).build();
            statisticButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean selected) {
                    if (selected) {
                        removeSeriesByName(statisticButton.getText());
                        statisticButton.setStyle("-fx-opacity: 0.2;");
                    } else {
                        Statistic<Long> data = getDataModel().getDataByName(statisticButton.getText());
                        lineChart.getData().add(asSeries(data));
                        statisticButton.setStyle("-fx-opacity: 1;");

                        //If the filterButton is selected then we should add the filters for that statistic.
                        if( filterButton.selectedProperty().get() ){
                            addFiltersByStatistic( data );
                        }
                    }
                }
            });
            statisticSelector.getChildren().add(statisticButton);
        }
    }

    private void removeSeriesByName(String name) {
        List<XYChart.Series> candidatesForRemoval = new ArrayList<XYChart.Series>();
        for (XYChart.Series<Number, Number> currentSeries : lineChart.getData()) {
            //The statistic
            if (currentSeries.getName().equals(name)) {
                candidatesForRemoval.add( currentSeries );
                continue;
            }
            //Any filters applied to the chart.
            if( filterButton.selectedProperty().get() && currentSeries.getName().startsWith( name )){
                candidatesForRemoval.add( currentSeries );
                continue;
            }
        }
        for( XYChart.Series series : candidatesForRemoval ) {
            lineChart.getData().remove(series);
        }


    }

    @Override
    public DataModel<Long> getDataModel() {
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
