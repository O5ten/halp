package com.osten.halp.impl.statistics;

import com.osten.halp.api.model.statistics.DataPoint;

public class LongDataPoint implements DataPoint<Long>
{
	private Long data;

	@Override
	public Long getData()
	{
		return data;
	}

	@Override
	public void setData( Long data )
	{
		this.data = data;
	}
}