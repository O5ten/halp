package com.osten.halp.api.model.statistics;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-10-21
 * Time: 14:09
 * A bean keeping a Date (Timestamp) and a datastore.
 */
public interface Statistic<Data>
{

	/**
	 * Replaces all the data
	 * @param data
	 */
	public void setData( List<DataPoint<Data>> data, DataType dataType);

	/**
	 * Adds data at the last point in the list
	 * @param data
	 */
	public void addData( DataPoint<Data> data );

	/**
	 * Insert data should add the data at specified index,
	 *
	 * Special Conditions:
	 * - if index is higher than the largest index of the statisticList then add it at the end.
	 * - If index is duplicate with another index in list then all indices after that index is shifted right to make room for new DataPoint
	 *
	 * @param index
	 * @param data
	 */
	public void insertData( int index, DataPoint<Data> data );

	/**
	 * Returns all the data that this statistic represents.
	 * @return
	 */
	public List<DataPoint<Data>> getDataAsList();

	/**
	 * Returns the data by using the index.
	 * @param index
	 * @return
	 */
	public DataPoint<Data> getDataByIndex( int index );

	/**
	 * What type of data is this, returns an enum residing in Statistic.FilterType
	 * @return
	 */
	public DataType getType();

	/**
	 * The name of the statistics
	 * @return
	 */
	public String getName();

    public void setName( String name );

    public int size();


	public void setType( DataType dataType );

    public void setType ( AggregatedStatisticType dataType );



    public AggregatedStatisticType getAggregatedType();

        /**
         * What type of data is this.
         *
         * Garbage Collection:
         * ==================================
         * - Garbage collection usually has this pattern, with regular and BIG sudden changes.
         *   they should not be detected as changes, however, if this data stops changing regularly then we have a problem.
         *
         * Throughput,
         * ==================================
         * A value that can change drastically as the number of users increase or decreases.
         * Should almost always be correlated to a FIXED rate or MovingRate.
         * If there is a big change in a fixed or moving rate at the same time as Throughput then this lessens the importance of the alarm.
         *
         * Memory
         * ==================================
         * Fast on startup, slow changes when steady state reached.
         * Alarm should be raised if this seldom or never decreases.
         * I.E if there is a memory leak.
         *
         * Accumulative
         * ==================================
         * Number of completed transactions, counters of the many sorts.
         * Alarm should be raised if they suddenly stop compared to a pattern.
         *
         * Average
         * ==================================
         * Like troughput it coincides with rates and other similar systems.
         * A difference is that the filter should be more forgiving as averages move
         * continuously while throughput moves more seldom but spikes more often.
         * Average is continuous but without the same spiking as TP.
         *
         * Rate:
         * ==================================
         * A rate, let's say a sending-rate of a fixed number of requests per seconds.
         * Seldom changes, and when it does it shouldn't really trigger any alarms as it is likely user or tester inflicted.
         *
         * Moving Rate:
         * ==================================
         * A rate that moves with time, let's say a linear increase, in correlation to this rate not
         * changing drastically then an alarm at a TP stat that does change drastically is more definite.
         *
         * Activity:
         * ==================================
         * Statistic is and should be Activity in the best case scenario, examples are dropped messages, discarded messages
         * 404 Resource cannot be found, 500 Internal Server Error,
         *
         * Time
         * ==================================
         * Time taken to get a response, fluctuates a'lot when it eventually moves at all. Highly relevant for the peak profile
         * Good to correlate to Rate.
         *
         * Unknown
         * ==================================
         * A timestamp for example or something that does not imply anything of importance
         * like number of valid responses or timestamps or similar. I.E, this does not get measured or correlated unless explicitly specified.
         *
         * CPU
         * ==================================
         * A very specific measurement that fluctuates much and often. If it lingers on very high very long then there is a need for an alarm.
         *
         */
	public enum DataType
	{
		Throughput, ResponseTime, RAM, Swap, CPU, Accumulative, Rate, Zero, Unknown
	}

    public enum AggregatedStatisticType{
        Residual, Estimation, Measurement, Variance, Not_Aggregated
    }

    public Statistic<Data> copyOf();
}
