package com.osten.halp.views.profiling;

import com.osten.halp.api.model.profiling.AdaptiveFilter;
import com.osten.halp.api.model.shared.FilterModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.ComboBox;
import javafx.scene.control.LabelBuilder;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-11-06
 * Time: 16:23
 * To change this template use File | Settings | File Templates.
 */
public class SelectableFilterView extends SelectableView {

    private List<ComboBox<AdaptiveFilter.FilterType>> listOfComboBoxes;

    public SelectableFilterView(ProfilingView profilingView) {
        super( profilingView );
        this.listOfComboBoxes = new ArrayList<ComboBox<AdaptiveFilter.FilterType>>();
        String statisticName = parentView.getSelectedStatistic();

        for (AdaptiveFilter<Long> filter : getFilterModel().getFiltersByStatisticName(statisticName)) {
            getChildren().addAll( createField( filter.getType() ) );
        }

        getChildren().add(addButton());

        System.out.println("Created SelectableFilterView");
    }

    public ObservableList<Node> getFields() {
        return getChildren();
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

    public ComboBox createFilterComboBox(AdaptiveFilter.FilterType selectedType) {
        final ComboBox<AdaptiveFilter.FilterType> box = new ComboBox();
        HBox.setHgrow(box, Priority.ALWAYS);

        box.getSelectionModel().select(getFreeFilters().get(0));
        listOfComboBoxes.add(box);

        List<AdaptiveFilter.FilterType> filterTypes = getFreeFilters();
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

    private List<AdaptiveFilter.FilterType> getFreeFilters() {

        List<AdaptiveFilter.FilterType> freeFilters = new ArrayList();

        for (AdaptiveFilter.FilterType type : AdaptiveFilter.FilterType.values()) {
            freeFilters.add(type);
        }

        for (ComboBox<AdaptiveFilter.FilterType> box : listOfComboBoxes) {
            freeFilters.remove(box.getSelectionModel().getSelectedItem());
        }

        return freeFilters;
    }

    private Button negativeButton(final ComboBox<AdaptiveFilter.FilterType> comboBox) {
        return ButtonBuilder
                .create()
                .styleClass()
                .onAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
                    @Override
                    public void handle(javafx.event.ActionEvent actionEvent) {
                        getChildren().remove(((Button) actionEvent.getSource()).getParent());
                        getFilterModel().removeFilter(parentView.getSelectedStatistic(), comboBox.getSelectionModel().getSelectedItem());
                        listOfComboBoxes.remove(comboBox);
                        refreshOptionsInComboBoxes( getFreeFilters() );
                    }
                })
                .graphic(
                        RegionBuilder.create().styleClass("negative-graphic").build()
                )
                .build();
    }

    public HBox createField(AdaptiveFilter.FilterType selectedType) {
        ComboBox<AdaptiveFilter.FilterType> comboBox = createFilterComboBox(selectedType);

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
        ComboBox<AdaptiveFilter.FilterType> comboBox = createFilterComboBox(AdaptiveFilter.FilterType.values()[0]);

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
