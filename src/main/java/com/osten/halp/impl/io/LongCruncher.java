package com.osten.halp.impl.io;

import com.osten.halp.api.model.io.DataCruncher;
import com.osten.halp.api.model.io.DataReader;
import com.osten.halp.api.model.statistics.DataPoint;
import com.osten.halp.api.model.statistics.Statistic;
import com.osten.halp.impl.statistics.LongDataPoint;
import com.osten.halp.impl.statistics.LongStatistic;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: osten
 * Date: 10/28/13
 * Time: 5:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class LongCruncher implements DataCruncher<Long> {

    @Override
    public List<Statistic<Long>> crunch(DataReader reader, ObservableList<String> requiredHeaders) {

       ArrayList<Statistic<Long>> statistics = new ArrayList<Statistic<Long>>();

        try {
            String[] headers = reader.readLine(1);
            ArrayList<String[]> data = reader.readFile();
            List<Integer> positions = locatePositions(requiredHeaders, headers);

            for( Integer column : positions ){
                LongStatistic stat = new LongStatistic( headers[column] );

                for( int row = 0; row < data.size(); row++){
                    stat.addData( new LongDataPoint( data.get( row )[ column ] ) );
                }
                statistics.add( stat );
            }

        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return statistics;
    }

    private List<Integer> locatePositions(ObservableList<String> requiredHeaders, String[] headers) {

        LinkedList<Integer> headerIndices = new LinkedList<Integer>();

        for (int i = 0; i < requiredHeaders.size(); i++) {
            for (int j = 0; j < headers.length; j++) {
                if (requiredHeaders.get( i ).compareTo( headers[j] ) == 0) {
                    headerIndices.add( j );
                    break;
                }
            }
        }
        return headerIndices;
    }
}
