package com.osten.halp.gui.selection;

import com.osten.halp.impl.io.CSVReader;
import com.osten.halp.gui.main.MainWindowView;
import com.osten.halp.utils.FXMLUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.javafxdata.datasources.provider.CSVDataSource;
import org.javafxdata.datasources.reader.DataSourceReader;
import org.javafxdata.datasources.reader.FileSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-10-15
 * Time: 15:18
 * To change this template use File | Settings | File Templates.
 */
public class SelectionView extends HBox implements Initializable {

    MainWindowView parentWindow;

    @FXML
    private VBox content;

    @FXML
    private TableView<String[]> table;

    @FXML
    private ToggleButton editButton;

    @FXML
    private ComboBox<String> dataFormatBox;

    @FXML
    private ListView<String> selectionList;

    @FXML
    private Button save;

    @FXML
    private Button continueButton;

    @FXML
    private TextField fileField;

    File lastDirectory;
    ExecutorService executor;
    CSVDataSource csvData;

    @FXML
    private void handleContinue(){

        System.out.println( " Compiling statistics for next view.  " );
        parentWindow.getSelectionModel().selectNext();

    }

    @FXML
    private void handleBrowse(ActionEvent event) {

        FileChooser filepicker = new FileChooser();

        if ( dataFormatBox.getItems().size() == 1) {
            dataFormatBox.getSelectionModel().selectFirst();
        }

        String format = dataFormatBox.getSelectionModel().getSelectedItem();
        filepicker.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(format, "*." + format.toLowerCase())
        );

        filepicker.setTitle("Select table");
        filepicker.setInitialDirectory(lastDirectory);

        File file = filepicker.showOpenDialog(parentWindow.getScene().getWindow());

        if (file != null) {
            lastDirectory = file.getParentFile();
            fileField.setText(file.toString());
            populateViews(file, format);
        }
    }

    private void populateViews(File file, String format) {

        if ( dataFormatBox.getSelectionModel().getSelectedItem().compareTo(format) == 0) {

            /************************
             * Build TableView using DataFX
             ************************/
            String[] headers = readHeaders(file);
            CSVDataSource csvData = readCSVFile(file, headers);
            table.setItems(csvData.getData());
            table.getColumns().addAll(csvData.getColumns());

            /************************
             * Build ListView using DataFX
             ************************/
            ObservableList<String> observableHeaders = FXCollections.observableArrayList( headers );
            selectionList.setItems( observableHeaders );
            this.csvData = csvData;
        }
    }

    private String[] readHeaders(File file) {
        CSVReader reader = null;
        String[] headers = null;
        try {
            reader = new CSVReader(file);
            headers = reader.readHeader();

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return headers;
    }

    private CSVDataSource readCSVFile(File file, String[] headers) {
        CSVDataSource dataSource = null;
        try {
            DataSourceReader source = new FileSource(file);
            dataSource = new CSVDataSource(source, headers);
        } catch (FileNotFoundException e) {
            System.err.println("Cannot find file");
        } catch (IOException e) {
            System.err.println("Cannot read file");
        }
        return dataSource;
    }

    public SelectionView(MainWindowView mainWindowView) {
        parentWindow = mainWindowView;
        FXMLUtils.load(this);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        dataFormatBox.getItems().setAll("CSV");
        selectionList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        selectionList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {

                continueButton.setVisible( true );

                if(csvData != null){
                    int i = 0;

                    //Reset all columns
                    for( TableColumn column : table.getColumns() ){
                        column.setStyle("-fx-opacity: 0.4;");
                    }

                    //Mark selected columns
                    for ( String column : selectionList.getSelectionModel().getSelectedItems()){
                        TableColumn selectedTableColumn = csvData.getNamedColumn( column );
                        selectedTableColumn.setStyle("-fx-opacity: 1; ");
                        System.out.println( column + " is selected. " );
                    }
                }
            }
        });



        executor = parentWindow.getExecutor();
    }
}
