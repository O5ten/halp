package com.osten.halp.gui.main;

import com.osten.halp.gui.analysis.AnalysisView;
import com.osten.halp.gui.selection.SelectionView;
import com.osten.halp.gui.profiling.ProfilingView;
import com.osten.halp.gui.home.HomeView;
import com.osten.halp.utils.FXMLUtils;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-10-15
 * Time: 11:45
 * To change this template use File | Settings | File Templates.
 */
public class MainWindowView extends BorderPane{

    private static Logger logger = LoggerFactory.getLogger(MainWindowView.class);

    private ExecutorService executor;

    @FXML
    private Tab home;

    @FXML
    private Tab selection;

    @FXML
    private Tab profiling;

    @FXML
    private Tab analysis;

    @FXML
    private TabPane tabpane;

    @FXML
    private Label subHeadline;

    public MainWindowView(){
        FXMLUtils.load( this );
    }

    public Label getSubHeadline(){
        return subHeadline;
    }

    public ObservableList<Tab> getTabs(){
        return tabpane.getTabs();
    }

    private SingleSelectionModel<Tab> selectionModel;

    public SingleSelectionModel<Tab> getSelectionModel(){
        return selectionModel;
    }

    public void initialize() {

            selectionModel = tabpane.getSelectionModel();

            executor = Executors.newFixedThreadPool(10);

            executor.execute(new Runnable(){

                @Override
                public void run() {
                    System.out.println("ExecutorService Online");
                }
            });

            home.setContent(new HomeView(this));
            selection.setContent(new SelectionView(this));
            profiling.setContent(new ProfilingView(this));
            analysis.setContent( new AnalysisView(this) );

    }

    public ExecutorService getExecutor(){
        return executor;
    }


}
