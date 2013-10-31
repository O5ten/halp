package com.osten.halp.impl.shared;

import com.osten.halp.api.model.shared.PropertyModel;
import com.osten.halp.api.model.statistics.Statistic;

import java.util.HashMap;
import java.util.List;

/**
 * User: osten
 * Date: 10/31/13
 * Time: 1:54 PM
 * Everything selected settings, properties, filters, data and the likes are stored in this bean.
 */
public class LongPropertyModel extends PropertyModel<Long>
{

	/**
	 * Crunched data from any type of File that has values that can become longs.
	 */
	private List<Statistic<Long>> crunchedData;

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
}
