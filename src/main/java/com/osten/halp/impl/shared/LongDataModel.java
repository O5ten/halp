package com.osten.halp.impl.shared;

import com.osten.halp.api.model.shared.DataModel;
import com.osten.halp.api.model.statistics.Statistic;

import java.util.ArrayList;
import java.util.List;

/**
 * User: osten
 * Date: 10/31/13
 * Time: 1:54 PM
 * Everything selected settings, properties, filters, data and the likes are stored in this bean.
 */
public class LongDataModel extends DataModel<Long>
{
	/**
	 * Crunched data from any type of File that has values that can become longs.
	 */
	private List<Statistic<Long>> crunchedData = new ArrayList<Statistic<Long>>();

	@Override
	public List<Statistic<Long>> getData()
	{
		return crunchedData;
	}

	@Override
	public void setData( List<Statistic<Long>> freshData )
	{
		 this.crunchedData = freshData;
	}

    @Override
    public List<String> getStatisticNames() {
        List<String> names = new ArrayList<String>();
        for(Statistic<Long> s : crunchedData){
            names.add( s.getName() );
        }
        return names;
    }

    @Override
    public Statistic<Long> getDataByName(String name){
        for(Statistic<Long> statistic :  crunchedData){
            if( statistic.getName().compareToIgnoreCase( name ) == 0){
                return statistic;
            }
        }
        throw new RuntimeException("Cannot find " + name + " among crunched data, this should not be possible because names of statistics do not change.");
    }
}
