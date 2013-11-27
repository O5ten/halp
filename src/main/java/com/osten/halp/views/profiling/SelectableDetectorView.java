package com.osten.halp.views.profiling;

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

    private List<ComboBox<ChangeDetector.DetectorType>> listOfComboBoxes;

    public SelectableDetectorView(ProfilingView parent) {
        super( parent );

        this.listOfComboBoxes = new ArrayList<ComboBox<ChangeDetector.DetectorType>>();
        String statisticName = parentView.getSelectedStatistic();

        for (ChangeDetector<Long> detector : getDetectorModel().getDetectorsByStatisticName(statisticName)) {
            getChildren().addAll(createField(detector.getType()));
        }

        getChildren().add(addButton());
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
                                List<ChangeDetector.DetectorType> detectorTypes = getFreeDetectors();
                                if (detectorTypes.size() > 0) {
                                    getChildren().add(getChildren().size() - 1, createField());
                                }
                            }
                        })
                        .build(),
                LabelBuilder.create().text("Add Detector").build()
        ).build();

    }

    private void refreshOptionsInComboBoxes(List<ChangeDetector.DetectorType> detectorTypes) {

        for (ComboBox<ChangeDetector.DetectorType> comboBox : listOfComboBoxes) {

            ChangeDetector.DetectorType selectedType = null;

            selectedType = comboBox.getSelectionModel().getSelectedItem();
            comboBox.getItems().clear();
            comboBox.getItems().add(selectedType);

            comboBox.getItems().addAll(detectorTypes);
            comboBox.getSelectionModel().clearSelection();
            comboBox.getSelectionModel().select( selectedType );
        }
    }

    public ComboBox createDetectorCombobox(ChangeDetector.DetectorType selectedType) {
        final ComboBox<ChangeDetector.DetectorType> box = new ComboBox();
        HBox.setHgrow(box, Priority.ALWAYS);

        box.getSelectionModel().select(getFreeDetectors().get(0));
        listOfComboBoxes.add(box);

        List<ChangeDetector.DetectorType> detectorTypes = getFreeDetectors();
        box.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ChangeDetector.DetectorType>() {
            @Override
            public void changed(ObservableValue<? extends ChangeDetector.DetectorType> observableValue, ChangeDetector.DetectorType oldDetectorType, ChangeDetector.DetectorType newDetectorType) {
                if (newDetectorType != null) {
                    String statistic = parentView.getSelectedStatistic();

                    boolean noDetectorsExistSoThereIsNothingToRemove = !getDetectorModel().getDetectorsByStatisticName(statistic).isEmpty();

                    if (noDetectorsExistSoThereIsNothingToRemove) {
                        getDetectorModel().removeDetector(statistic, oldDetectorType);
                    }

                    boolean detectorDoesNotExistYet = getDetectorModel().getDetector(statistic, newDetectorType) == null;
                    if (detectorDoesNotExistYet) {
                        getDetectorModel().createDetector(statistic, newDetectorType);
                    }
                }
            }
        });
        refreshOptionsInComboBoxes(detectorTypes);
        return box;
    }

    private List<ChangeDetector.DetectorType> getFreeDetectors() {

        List<ChangeDetector.DetectorType> freeDetectors = new ArrayList();

        for (ChangeDetector.DetectorType type : ChangeDetector.DetectorType.values()) {
            freeDetectors.add(type);
        }

        for (ComboBox<ChangeDetector.DetectorType> box : listOfComboBoxes) {
            freeDetectors.remove(box.getSelectionModel().getSelectedItem());
        }

        return freeDetectors;
    }

    private Button negativeButton(final ComboBox<ChangeDetector.DetectorType> comboBox) {
        return ButtonBuilder
                .create()
                .styleClass()
                .onAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
                    @Override
                    public void handle(javafx.event.ActionEvent actionEvent) {
                        getChildren().remove(((Button) actionEvent.getSource()).getParent());
                        getDetectorModel().removeDetector(parentView.getSelectedStatistic(), comboBox.getSelectionModel().getSelectedItem());
                        listOfComboBoxes.remove(comboBox);
                        refreshOptionsInComboBoxes( getFreeDetectors() );
                    }
                })
                .graphic(
                        RegionBuilder.create().styleClass("negative-graphic").build()
                )
                .build();
    }

    public HBox createField(ChangeDetector.DetectorType selectedType) {
        ComboBox<ChangeDetector.DetectorType> comboBox = createDetectorCombobox(selectedType);

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
        ComboBox<ChangeDetector.DetectorType> comboBox = createDetectorCombobox(ChangeDetector.DetectorType.values()[0]);

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
