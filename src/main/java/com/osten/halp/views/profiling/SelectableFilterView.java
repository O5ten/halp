package com.osten.halp.views.profiling;

import com.osten.halp.api.model.profiling.AdaptiveFilter;
import com.osten.halp.api.model.profiling.Enumerable;
import com.osten.halp.api.model.shared.FilterModel;
import com.osten.halp.utils.FXMLUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.ComboBox;
import javafx.scene.control.LabelBuilder;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-11-06
 * Time: 16:23
 * To change this template use File | Settings | File Templates.
 */
public class SelectableFilterView extends VBox {

    private ProfilingView parentView;

    public SelectableFilterView( ProfilingView profilingView) {
        this.parentView = profilingView;
        getChildren().addAll( createField(), addButton() );
        System.out.println( "Created SelectableFilterView") ;
    }

    public ObservableList<Node> getFields(){
        return getChildren();
    }

    private FilterModel<Long> getFilterModel() {
        return parentView.getFilterModel();
    }

    private HBox addButton() {
        return HBoxBuilder.create().children(
                ButtonBuilder
                .create()
                .text("+")
                .graphic(RegionBuilder
                        .create()
                        .styleClass("positive-graphic")
                        .build()
                )
                .onAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        getChildren().add(0, createField());
                    }
                })
                .build(),
                LabelBuilder.create().text("Add another filter").build()
        ).build()                             ;

    }

    private ComboBox createFilterComboBox() {

        final ComboBox<AdaptiveFilter.FilterType> box = new ComboBox();
        box.getItems().addAll(AdaptiveFilter.FilterType.values());

        EventHandler<ActionEvent> evt = new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent actionEvent) {
                FilterModel<Long> filterModel = getFilterModel();
                filterModel.createFilter(parentView.getSelectedStatistic(), box.getSelectionModel().getSelectedItem());
            }
        };
        box.setOnAction(evt);
        box.getSelectionModel().selectFirst();
        return box;
    }

    private Button negativeButton() {
        return ButtonBuilder
                .create()
                .text( "-" )
                .styleClass()
                .onAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
                    @Override
                    public void handle(javafx.event.ActionEvent actionEvent) {
                        getChildren().remove(((Button) actionEvent.getSource()).getParent());
                        //TODO remove filter gorgeously from statistic.
                    }
                })
                .graphic(
                        RegionBuilder.create().styleClass("negative-graphic").build()
                )
                .build();
    }

    private HBox createField() {
        return HBoxBuilder
                .create()
                .spacing(4)
                .children(
                        negativeButton(),
                        createFilterComboBox()
                )
                .build();
    }
}
