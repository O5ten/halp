package com.osten.halp.impl.io;

import com.osten.halp.model.io.DataCruncher;
import com.osten.halp.model.io.DataReader;
import com.osten.halp.model.statistics.DataPoint;
import com.osten.halp.model.statistics.Statistic;
import com.osten.halp.impl.statistics.LongDataPoint;
import com.osten.halp.impl.statistics.LongStatistic;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: osten
 * Date: 10/28/13
 * Time: 5:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class LongDataCruncher implements DataCruncher<Long>
{

	@Override
	public List<Statistic<Long>> crunch( DataReader reader, ObservableList<String> requiredHeaders )
	{

		ArrayList<Statistic<Long>> statistics = new ArrayList<Statistic<Long>>();

		try
		{
			String[] headers = reader.readLine( 1 );
			ArrayList<String[]> data = reader.readFile();
			List<Integer> positions = locatePositions( requiredHeaders, headers );

			for( int column : positions )
			{
				LongStatistic stat = new LongStatistic( headers[column] );
				int counter = 0;
				//Why 1? it ignores the header
				for( int row = 1; row < data.size(); row++ )
				{

					if( data.get( row )[column].equals( "" ) )
					{
						counter++;

						//Finishing up if any trailing empty fields.
						if( row == data.size() - 1 && counter > 0 )
						{
							while( counter != 0 )
							{
								long value = stat.getDataAsList().get( stat.size() - 1 ).getValue();
								stat.addData( new LongDataPoint( value ) );
								counter--;
							}
						}
						continue;
					}
					else
					{
						//Fill in the blanks with linear data.
						if( counter > 0 )
						{
							long a = stat.getDataAsList().isEmpty() ? new LongDataPoint( data.get( row )[column] ).getValue() : stat.getDataAsList().get( stat.size() - 1 ).getValue();
							long b = new LongDataPoint( data.get( row )[column] ).getValue();

							long diff = ( b - a ) / counter;

							while( counter != 0 )
							{
								a += diff;
								stat.addData( new LongDataPoint( a ) );
								counter--;
							}
							continue;
						}
					}
					try
					{
						stat.addData( new LongDataPoint( data.get( row )[column] ) );
					}
					catch( NumberFormatException e )
					{
						System.out.println( "cannot parse " + stat.getName() + ", is [ " + data.get( row )[column] + " ] really a number? Ignoring and continuing with the rest." );
						break;
					}
				}

				if( !stat.getDataAsList().isEmpty() )
				{
					statistics.add( stat );
				}
			}

		}
		catch( IOException exception )
		{
			exception.printStackTrace();
		}


		return normalize( statistics );
	}

	@Override
	public List<Statistic<Long>> crunch( DataReader reader )
	{
		ArrayList<Statistic<Long>> statistics = new ArrayList<Statistic<Long>>();

		try
		{
			String[] headers = reader.readLine( 1 );
			ArrayList<String[]> data = reader.readFile();
			List<Integer> positions = locatePositions( headers );

			for( int column : positions )
			{
				LongStatistic stat = new LongStatistic( headers[column] );
				int counter = 0;
				//Why 1? it ignores the header
				for( int row = 1; row < data.size(); row++ )
				{
					System.out.println(row);
					if( data.get( row )[column].equals( "" ) )
					{
						counter++;

						//Finishing up if any trailing empty fields.
						if( row == data.size() - 1 && counter > 0 )
						{
							while( counter != 0 )
							{
								long value = stat.getDataAsList().get( stat.size() - 1 ).getValue();
								stat.addData( new LongDataPoint( value ) );
								counter--;
							}
						}
						continue;
					}
					else
					{
						//Fill in the blanks with linear data.
						if( counter > 0 )
						{
							long a = stat.getDataAsList().isEmpty() ? new LongDataPoint( data.get( row )[column] ).getValue() : stat.getDataAsList().get( stat.size() - 1 ).getValue();
							long b = new LongDataPoint( data.get( row )[column] ).getValue();

							long diff = ( b - a ) / counter;

							while( counter != 0 )
							{
								a += diff;
								stat.addData( new LongDataPoint( a ) );
								counter--;
							}
							continue;
						}
					}
					try
					{
						stat.addData( new LongDataPoint( data.get( row )[column] ) );
					}
					catch( NumberFormatException e )
					{
						System.out.println( "cannot parse " + stat.getName() + ", is [ " + data.get( row )[column] + " ] really a number? Ignoring and continuing with the rest." );
						break;
					}
				}

				if( !stat.getDataAsList().isEmpty() )
				{
					statistics.add( stat );
				}
			}

		}
		catch( IOException exception )
		{
			exception.printStackTrace();
		}


		return normalize( statistics );
	}

	public List<Statistic<Long>> normalize( List<Statistic<Long>> statistics )
	{

		int length = longest( statistics );
		//Normalize all lengths of measurements.
		for( Statistic<Long> stat : statistics )
		{
			if( length > stat.getDataAsList().size() )
			{
				int n = length - stat.getDataAsList().size();
				DataPoint<Long> fillerData = stat.getDataAsList().get( stat.getDataAsList().size() - 1 );
				while( n > 0 )
				{
					stat.addData( fillerData );
					n--;
				}
			}
		}
		return statistics;
	}

	private int longest( List<Statistic<Long>> statistics )
	{
		int i = 0;
		for( Statistic<Long> statistic : statistics )
		{
			if( statistic.getDataAsList().size() > i )
			{
				i = statistic.getDataAsList().size();
			}
		}
		return i;
	}

	private List<Integer> locatePositions( String[] headers )
	{
		LinkedList<Integer> headerIndices = new LinkedList<Integer>();
		for( int i = 0; i < headers.length; i++ )
		{
			headerIndices.add( i );
		}
		return headerIndices;
	}

	private List<Integer> locatePositions( ObservableList<String> requiredHeaders, String[] headers )
	{

		LinkedList<Integer> headerIndices = new LinkedList<Integer>();

		for( int i = 0; i < requiredHeaders.size(); i++ )
		{
			for( int j = 0; j < headers.length; j++ )
			{
				if( requiredHeaders.get( i ).compareTo( headers[j] ) == 0 )
				{
					headerIndices.add( j );
					break;
				}
			}
		}
		return headerIndices;
	}
}
