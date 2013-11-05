package com.osten.halp.impl.shared;

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
public class LongDataCruncher implements DataCruncher<Long> {

    @Override
    public List<Statistic<Long>> crunch(DataReader reader, ObservableList<String> requiredHeaders) {

        ArrayList<Statistic<Long>> statistics = new ArrayList<Statistic<Long>>();

        try {
            String[] headers = reader.readLine(1);
            ArrayList<String[]> data = reader.readFile();
            List<Integer> positions = locatePositions(requiredHeaders, headers);

            for (int column : positions) {
                LongStatistic stat = new LongStatistic(headers[column]);

                //Why 1? it ignores the header
                for (int row = 1; row < data.size(); row++) {
                    try {
                        stat.addData(new LongDataPoint( data.get(row)[column] ) );
                    } catch (NumberFormatException e) {
                        System.out.println("cannot parse " + stat.getName() + ", is it really a number? Ignoring and continuing with the rest.");
                        break;
                    }
                }

                if(!stat.getData().isEmpty()){
                    statistics.add(stat);
                }
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
                if (requiredHeaders.get(i).compareTo(headers[j]) == 0) {
                    headerIndices.add(j);
                    break;
                }
            }
        }
        return headerIndices;
    }
}
