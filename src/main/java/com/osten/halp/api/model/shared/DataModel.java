package com.osten.halp.api.model.shared;

import com.osten.halp.api.model.statistics.DataPoint;
import com.osten.halp.api.model.statistics.Statistic;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: osten
 * Date: 10/31/13
 * Time: 1:49 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class DataModel<Data>
{
	public abstract List<Statistic<Data>> getData();
	public abstract void setData( List<Statistic<Data>> data);
   public abstract List<String> getStatisticNames();
   public abstract Statistic<Data> getDataByName( String name );
   public abstract void resetModel();

	public void printModel(  )
	{
		System.out.println( "Data set: \n==============");
		for( Statistic<Data> statistic : getData() )
		{
			System.out.print( statistic.getName() + "= [ " );
			for( DataPoint<Data> point : statistic.getDataAsList() )
			{
				System.out.print( point.getValue() + " " );
			}
			System.out.println( "]" );
		}
	}
}
