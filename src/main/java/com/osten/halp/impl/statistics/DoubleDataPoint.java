package com.osten.halp.impl.statistics;

import com.osten.halp.api.model.statistics.DataPoint;

/**
 * Created with IntelliJ IDEA.
 * User: osten
 * Date: 11/12/13
 * Time: 3:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class DoubleDataPoint implements DataPoint<Double>
{
	private Double data;

	public DoubleDataPoint( Double data){
		 this.data = data;
	}

	@Override
	public Double getValue()
	{
		return data;
	}

	@Override
	public void setData( Double data )
	{
		this.data = data;
	}
}
