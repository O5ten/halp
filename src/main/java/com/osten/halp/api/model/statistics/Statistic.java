package com.osten.halp.api.model.statistics;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-10-21
 * Time: 14:09
 * A bean keeping a Date (Timestamp) and a datastore.
 *
 */
public interface Statistic<Data extends Object> {

    public void setData(Data data);
    public Data getData();

}
