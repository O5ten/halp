package com.osten.halp.views.main;

import com.osten.halp.model.gui.PopulatableView;
import com.osten.halp.model.shared.DataModel;
import com.osten.halp.model.shared.DetectorModel;
import com.osten.halp.model.shared.FilterModel;
import com.osten.halp.model.shared.ProfileModel;
import com.osten.halp.impl.shared.LongDataModel;
import com.osten.halp.impl.shared.LongDetectorModel;
import com.osten.halp.impl.shared.LongFilterModel;
import com.osten.halp.impl.shared.LongProfileModel;
import com.osten.halp.views.analysis.AnalysisView;
import com.osten.halp.views.selection.SelectionView;
import com.osten.halp.views.profiling.ProfilingView;
import com.osten.halp.views.home.HomeView;
import com.osten.halp.utils.FXMLUtils;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-10-15
 * Time: 11:45
 * To change this template use File | Settings | File Templates.
 */
public class MainWindowView extends BorderPane {

    private ExecutorService executor;

    @FXML
    private Tab homeTab;

    @FXML
    private Tab selectionTab;

    @FXML
    private Tab profilingTab;

    @FXML
    private Tab analysisTab;

    @FXML
    private TabPane tabpane;

    @FXML
    private Label subHeadline;

    private List<PopulatableView> popViews;

    //Domain
    private DataModel<Long> dataModel;
    private FilterModel<Long> filterModel;
    private DetectorModel<Long> detectorModel;
    private ProfileModel<Long> profileModel;

    public DataModel<Long> getDataModel() {
        return dataModel;
    }

    public FilterModel<Long> getFilterModel() {
        return filterModel;
    }

    public ProfileModel<Long> getProfileModel() {
        return profileModel;
    }

    public DetectorModel<Long> getDetectorModel(){
        return detectorModel;
    }

    public void rePopulateViews() {
        for (PopulatableView p : popViews) {
            p.populate(dataModel, filterModel, detectorModel, profileModel);
        }
    }

    public MainWindowView() {
        FXMLUtils.load(this);
    }

    public Label getSubHeadline() {
        return subHeadline;
    }

    public ObservableList<Tab> getTabs() {
        return tabpane.getTabs();
    }

    private SingleSelectionModel<Tab> selectionModel;

    public SingleSelectionModel<Tab> getSelectionModel() {
        return selectionModel;
    }

    public void initialize() {

        selectionModel = tabpane.getSelectionModel();

        executor = Executors.newFixedThreadPool(10);

        executor.execute(new Runnable() {

            @Override
            public void run() {
                System.out.println("ExecutorService Online");
            }
        });

        homeTab.setContent(new HomeView(this));
        selectionTab.setContent(new SelectionView(this));

        ProfilingView profilingView = new ProfilingView(this);
        AnalysisView analysisView = new AnalysisView(this);

        //Views that can be repopulated as data or settings is changed.
        popViews = new ArrayList<PopulatableView>();
        popViews.add(profilingView);
        popViews.add(analysisView);

        profilingTab.setContent(profilingView);
        analysisTab.setContent(analysisView);

        //Initialize domain
        dataModel = new LongDataModel();
        filterModel = new LongFilterModel();
        profileModel = new LongProfileModel();
        detectorModel = new LongDetectorModel();

    }

    public ExecutorService getExecutor() {
        return executor;
    }


}
