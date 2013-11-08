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
import javafx.geometry.Insets;
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
        this.setSpacing( 4 );
        this.setPadding( new Insets( 4, 4, 4, 4));
        this.getStyleClass().add( "selectable-filter-view" );
        VBox.setVgrow(this, Priority.ALWAYS);

        System.out.println( "Created SelectableFilterView") ;
    }

    public ObservableList<Node> getFields(){
        return getChildren();
    }

    private FilterModel<Long> getFilterModel() {
        return parentView.getFilterModel();
    }

    private HBox addButton() {
        return HBoxBuilder.create().spacing ( 4 ).children(
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
                        getChildren().add( getChildren().size()-1, createField());
                    }
                })
                .build(),
                LabelBuilder.create().text("Add filter").build()
        ).build();

    }

    private ComboBox createFilterComboBox() {

        final ComboBox<AdaptiveFilter.FilterType> box = new ComboBox();
        HBox.setHgrow( box, Priority.ALWAYS );
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

    private Button negativeButton( final ComboBox<AdaptiveFilter.FilterType> comboBox ) {
        return ButtonBuilder
                .create()
                .styleClass()
                .onAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
                    @Override
                    public void handle(javafx.event.ActionEvent actionEvent) {
                        getChildren().remove(((Button) actionEvent.getSource()).getParent());
                        getFilterModel().removeFilter( parentView.getSelectedStatistic(), comboBox.getSelectionModel().getSelectedItem() );
                    }
                })
                .graphic(
                        RegionBuilder.create().styleClass("negative-graphic").build()
                )
                .build();
    }

    private HBox createField() {
        ComboBox<AdaptiveFilter.FilterType> comboBox = createFilterComboBox();

        return HBoxBuilder
                .create()
                .spacing( 4 )
                .children(
                        negativeButton( comboBox ),
                        comboBox
                )
                .build();
    }
}
