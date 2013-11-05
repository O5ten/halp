package com.osten.halp.api.model.shared;

import com.osten.halp.api.model.statistics.DataPoint;
import com.osten.halp.api.model.statistics.Statistic;

import java.util.HashMap;
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
	/**
	 * To be used for properties that are very small only. Bear in mind that they will need casting.
	 */
	private HashMap<String,Object> properties = new HashMap<String, Object>();

	public Object getProperty( String key )
	{
		return properties.get( key );
	}

	public void setProperty( String key, Object data )
	{
		properties.put( key, data );
	}

	public void printModel(  )
	{
		System.out.println( "Data set: \n==============");
		for( Statistic<Data> statistic : getData() )
		{
			System.out.print( statistic.getName() + "= [ " );
			for( DataPoint<Data> point : statistic.getData() )
			{
				System.out.print( point.getData() + " " );
			}
			System.out.println( "]" );
		}
		System.out.print("\nGeneral properties\n==============\n");
		for( String key : properties.keySet() ){
			System.out.println( key + " ==> " + properties.get( key ).toString() );
		}

		if( properties.values().isEmpty()){
			System.out.println("EMPTY SET");
		}
		System.out.println("==============\n");
	}
}
