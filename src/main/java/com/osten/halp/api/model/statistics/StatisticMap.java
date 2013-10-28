package com.osten.halp.api.model.statistics;


import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-10-22
 * Time: 12:53
 * To change this template use File | Settings | File Templates.
 */
public interface StatisticMap {

    public String getTimestamp();

    public Statistic getStatisticByTitle(String title);

    public void add(String title, Statistic statistic);

    public void clear();
}
