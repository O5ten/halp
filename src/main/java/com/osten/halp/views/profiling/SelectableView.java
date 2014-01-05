package com.osten.halp.views.profiling;

import com.osten.halp.model.shared.DetectorModel;
import com.osten.halp.model.shared.FilterModel;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-11-25
 * Time: 16:58
 * To change this template use File | Settings | File Templates.
 */
public class SelectableView extends VBox {

    ProfilingView parentView;

    public SelectableView( ProfilingView parent ){
        this.parentView = parent;

        this.setSpacing(4);
        this.setPadding(new Insets(4, 4, 4, 4));
        this.getStyleClass().add("selectable-view");

        VBox.setVgrow(this, Priority.ALWAYS);
    }

    public ObservableList<Node> getFields() {
        return getChildren();
    }

    protected FilterModel<Long> getFilterModel() {
        return parentView.getFilterModel();
    }

    protected DetectorModel<Long> getDetectorModel(){
        return parentView.getDetectorModel();
    }
}
