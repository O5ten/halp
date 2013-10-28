package com.osten.halp.api.model.io;

import com.osten.halp.api.model.statistics.Statistic;

import java.io.File;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: osten
 * Date: 10/28/13
 * Time: 5:12 PM
 * Purpose of this class is to take Statistics of type <T>
 */
public interface DataCruncher<T>
{
	/**
	 * Using the suggested reader, the headers and corresponding column of data will be picked out from the file the reader is attached to.
	 * @param headers
	 * @return a list
	 */
	public List<Statistic<T>> crunch( DataReader reader, String[] headers);
}
