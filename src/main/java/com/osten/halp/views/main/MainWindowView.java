package com.osten.halp.views.main;

import com.osten.halp.api.model.gui.PopulatableView;
import com.osten.halp.api.model.shared.DataModel;
import com.osten.halp.impl.shared.LongDataModel;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class MainWindowView extends BorderPane{

    private static Logger logger = LoggerFactory.getLogger(MainWindowView.class);

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

	 private DataModel<Long> dataModel;

	 public DataModel<Long> getDataModel(){
		 return dataModel;
	 }

	 public void rePopulateViews( ){

		  for( PopulatableView p : popViews ){
			  p.populate(dataModel);
		  }
	 }

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

            homeTab.setContent( new HomeView( this ) );
            selectionTab.setContent( new SelectionView( this ) );

		 		ProfilingView profilingView = new ProfilingView( this );
		 		AnalysisView analysisView = new AnalysisView( this );

		 		//Views that can be repopulated as data or settings is changed.
		 		popViews = new ArrayList<PopulatableView>();
		      popViews.add( profilingView );
		 		popViews.add( analysisView );

		 		profilingTab.setContent( profilingView );
            analysisTab.setContent( analysisView );

		      dataModel = new LongDataModel();

    }

    public ExecutorService getExecutor(){
        return executor;
    }


}
