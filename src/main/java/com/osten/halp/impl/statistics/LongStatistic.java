package com.osten.halp.impl.statistics;

import com.osten.halp.api.model.statistics.DataPoint;
import com.osten.halp.api.model.statistics.Statistic;

import java.util.ArrayList;
import java.util.Collections;
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
    private AggregatedStatisticType aggregatedType;

    public LongStatistic(){
        this( "kastamattomat" );
    }

    public LongStatistic( String name ){
        this( name, Statistic.DataType.Unknown);
    }

	public LongStatistic( String name, DataType dataType ){
		this.name = name;
		this.dataType = dataType;
        this.data = new ArrayList<DataPoint<Long>>();
        this.aggregatedType = AggregatedStatisticType.Not_Aggregated;

    }

    public LongStatistic( String name, AggregatedStatisticType aggregatedType ){
        this.name = name;
        this.aggregatedType = aggregatedType;
        this.data = new ArrayList<DataPoint<Long>>();
        this.aggregatedType = AggregatedStatisticType.Not_Aggregated;
    }

    public LongStatistic( Statistic<Long> statistic){
        this.name = statistic.getName();
        this.data = Collections.unmodifiableList( statistic.getData() );
        this.aggregatedType = statistic.getAggregatedType();
        this.dataType = statistic.getType();
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
    public AggregatedStatisticType getAggregatedType(){
    return aggregatedType;
}

	@Override
	public void setType( DataType dataType )
	{
		this.dataType = dataType;
	}

    @Override
    public void setType(AggregatedStatisticType aggregatedType) {
        this.aggregatedType = aggregatedType;
    }

    @Override
    public void setName(String name){
        this.name = name;
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
	public String getName()
	{
		return name;
	}

    @Override
    public Statistic<Long> copyOf(){
        return new LongStatistic ( this );
    }
}
