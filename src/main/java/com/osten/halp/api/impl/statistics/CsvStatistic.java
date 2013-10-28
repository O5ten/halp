package com.osten.halp.api.impl.statistics;

import com.osten.halp.api.model.statistics.Statistic;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-10-22
 * Time: 13:26
 * To change this template use File | Settings | File Templates.
 */
public class CsvStatistic implements Statistic {

    private long data;

    public CsvStatistic( Date date, long data ){
        this.data = data;
    }

    @Override
    public void setData(Object o) {
        data = (Long) o;
    }

    @Override
    public Object getData() {
        return data;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
