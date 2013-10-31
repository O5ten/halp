package com.osten.halp.impl.statistics;

import com.osten.halp.api.model.statistics.DataPoint;
import com.osten.halp.api.model.statistics.Statistic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: osten
 * Date: 10/28/13
 * Time: 3:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class LongStatistic implements Statistic<Long>
{
	private String name;
	private DataType dataType;
	private List<DataPoint<Long>> data;

    public LongStatistic( String name ){
        this.name = name;
        this.dataType = Statistic.DataType.UNDEFINED;
        this.data = new ArrayList<DataPoint<Long>>();
    }

	public LongStatistic( String name, DataType dataType ){
		this.name = name;
		this.dataType = dataType;
        this.data = new ArrayList<DataPoint<Long>>();
	}

	@Override
	public void setData( List<DataPoint<Long>> data, DataType dataType )
	{
		this.data = data;
	}

	@Override
	public void addData( DataPoint value )
	{
		data.add( value );
	}

	@Override
	public void insertData( int index, DataPoint value )
	{
		data.add( index, value );
	}

	@Override
	public List<DataPoint<Long>> getData()
	{
		return data;
	}

	@Override
	public DataPoint getDataByIndex( int index )
	{
		return data.get( index );
	}

	@Override
	public DataType getType()
	{
		return dataType;
	}

	@Override
	public void setType( DataType dataType )
	{
		this.dataType = dataType;
	}

	@Override
	public String getName()
	{
		return name;
	}
}
