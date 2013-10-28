package com.osten.halp.api.impl.statistics;

import com.osten.halp.api.model.statistics.Statistic;
import com.osten.halp.api.model.statistics.StatisticMap;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-10-22
 * Time: 13:27
 * To change this template use File | Settings | File Templates.
 */
public class CsvStatisticMap implements StatisticMap {

    Map<String, Statistic> store = new HashMap<String, Statistic>();
    String timestamp;

    public CsvStatisticMap( String timestamp ){
        this.timestamp = timestamp;
    }

    @Override
    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public Statistic getStatisticByTitle( String title ) {
        return store.get( title );  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void add(String title, Statistic statistic) {
       store.put( title, statistic );
    }

    @Override
    public void clear(){
        store.clear();
    }

}
