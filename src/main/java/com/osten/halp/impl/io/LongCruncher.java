package com.osten.halp.impl.io;

import com.osten.halp.api.model.io.DataCruncher;
import com.osten.halp.api.model.io.DataReader;
import com.osten.halp.api.model.statistics.Statistic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: osten
 * Date: 10/28/13
 * Time: 5:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class LongCruncher implements DataCruncher<Long>
{

	@Override
	public List<Statistic<Long>> crunch( DataReader reader, String[] requiredHeaders )
	{
		try
		{
			String[] headers = reader.readLine( 1 );
			ArrayList<String[]> data = reader.readFile();
			//TODO Implement

			return null;

		}
		catch( IOException exception )
		{
			exception.printStackTrace();
		}
		return null;
	}

	private Long[] locatePosition(String[] requiredHeaders, String[] allHeaders ){

		//TODO Implement
		return new Long[]{};
	}
}
