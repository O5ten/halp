package com.osten.halp.api.model.io;

import com.osten.halp.api.model.statistics.Statistic;
import com.osten.halp.api.model.statistics.StatisticMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-10-22
 * Time: 12:26
 * To change this template use File | Settings | File Templates.
 */
public interface DataReader {

    public String[] readLine( int whatline ) throws FileNotFoundException, IOException;
    public ArrayList<String[]> readFile();

}
