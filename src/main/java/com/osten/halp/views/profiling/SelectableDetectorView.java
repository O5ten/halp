package com.osten.halp.views.profiling;

import com.osten.halp.api.model.profiling.AdaptiveFilter;
import com.osten.halp.api.model.profiling.ChangeDetector;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.ComboBox;
import javafx.scene.control.LabelBuilder;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RegionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-11-25
 * Time: 16:57
 * To change this template use File | Settings | File Templates.
 */
public class SelectableDetectorView extends SelectableView{

    private List<ComboBox<AdaptiveFilter.FilterType>> listOfComboBoxes;

    public SelectableDetectorView(ProfilingView parent) {
        super(parent);

        this.listOfComboBoxes = new ArrayList<ComboBox<AdaptiveFilter.FilterType>>();
        String statisticName = parentView.getSelectedStatistic();
        getChildren().add(addButton());

        for (ChangeDetector<Long> filter : getDetectorModel().getDetectorsByStatistic(statisticName)) {
            getChildren().addAll(createField(filter.getType()));
        }
    }

    private HBox addButton() {
        return HBoxBuilder.create().spacing(4).children(
                ButtonBuilder
                        .create()
                        .graphic(RegionBuilder
                                .create()
                                .styleClass("positive-graphic")
                                .build()
                        )
                        .onAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                List<AdaptiveFilter.FilterType> filterTypes = getFreeFilters();
                                if (filterTypes.size() > 0) {
                                    getChildren().add(getChildren().size() - 1, createField());
                                }
                            }
                        })
                        .build(),
                LabelBuilder.create().text("Add filter").build()
        ).build();

    }

    private void refreshOptionsInComboBoxes(List<AdaptiveFilter.FilterType> filterTypes) {

        for (ComboBox<AdaptiveFilter.FilterType> comboBox : listOfComboBoxes) {

            AdaptiveFilter.FilterType selectedType = null;

            selectedType = comboBox.getSelectionModel().getSelectedItem();
            comboBox.getItems().clear();
            comboBox.getItems().add(selectedType);

            comboBox.getItems().addAll(filterTypes);
            comboBox.getSelectionModel().clearSelection();
            comboBox.getSelectionModel().select( selectedType );
        }
    }

    public ComboBox createFilterComboBox(ChangeDetector.DetectorType selectedType) {
        final ComboBox<ChangeDetector.DetectorType> box = new ComboBox();
        HBox.setHgrow(box, Priority.ALWAYS);

        box.getSelectionModel().select(getFreeFilters().get(0));
        listOfComboBoxes.add(box);

        List<ChangeDetector.DetectorType> filterTypes = getFreeFilters();
        box.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<AdaptiveFilter.FilterType>() {
            @Override
            public void changed(ObservableValue<? extends AdaptiveFilter.FilterType> observableValue, AdaptiveFilter.FilterType oldFilterType, AdaptiveFilter.FilterType newFilterType) {
                if (newFilterType != null) {
                    String statistic = parentView.getSelectedStatistic();

                    boolean noFiltersExistSoThereIsNothingToRemove = !getFilterModel().getFiltersByStatisticName(statistic).isEmpty();

                    if (noFiltersExistSoThereIsNothingToRemove) {
                        getFilterModel().removeFilter(statistic, oldFilterType);
                    }

                    boolean filterDoesNotExistYet = getFilterModel().getFilter(statistic, newFilterType) == null;
                    if (filterDoesNotExistYet) {
                        getFilterModel().createFilter(statistic, newFilterType);
                    }
                }
            }
        });
        refreshOptionsInComboBoxes(filterTypes);
        return box;
    }

    private List<ChangeDetector.DetectorType> getFreeFilters() {

        List<ChangeDetector.DetectorType> freeFilters = new ArrayList();

        for (ChangeDetector.DetectorType type : ChangeDetector.DetectorType.values()) {
            freeFilters.add(type);
        }

        for (ComboBox<AdaptiveFilter.FilterType> box : listOfComboBoxes) {
            freeFilters.remove(box.getSelectionModel().getSelectedItem());
        }

        return freeFilters;
    }

    private Button negativeButton(final ComboBox<ChangeDetector.DetectorType> comboBox) {
        return ButtonBuilder
                .create()
                .styleClass()
                .onAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
                    @Override
                    public void handle(javafx.event.ActionEvent actionEvent) {
                        getChildren().remove(((Button) actionEvent.getSource()).getParent());
                        getDetectorModel().removeFilter(parentView.getSelectedStatistic(), comboBox.getSelectionModel().getSelectedItem());
                        listOfComboBoxes.remove(comboBox);
                        refreshOptionsInComboBoxes( getFreeFilters() );
                    }
                })
                .graphic(
                        RegionBuilder.create().styleClass("negative-graphic").build()
                )
                .build();
    }

    public HBox createField(ChangeDetector.DetectorType selectedType) {
        ComboBox<ChangeDetector.DetectorType> comboBox = createFilterComboBox(selectedType);

        return HBoxBuilder
                .create()
                .spacing(4)
                .children(
                        negativeButton(comboBox),
                        comboBox
                )
                .build();
    }

    private HBox createField() {
        ComboBox<ChangeDetector.DetectorType> comboBox = createFilterComboBox(ChangeDetector.DetectorType.values()[0]);

        return HBoxBuilder
                .create()
                .spacing(4)
                .children(
                        negativeButton(comboBox),
                        comboBox
                )
                .build();
    }

}
