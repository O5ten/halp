package com.osten.halp.model.profiling;

/**
 * Created by osten on 12/10/13.
 */
public class Range implements Comparable<Range>
{
	private Long from;
	private Long to;

	public Range(Long from, Long to){
		this.to = to;
		this.from = from;
	}

	public Long getStart()
	{
		return from;
	}

	public void setFrom( Long from )
	{
		this.from = from;
	}

	public Long getStop()
	{
		return to;
	}

	public void setTo( Long to )
	{
		this.to = to;
	}

	@Override
	public int compareTo( Range otherRange )
	{
		if((otherRange.getStart() - getStart()) == 0){
			return Math.round( getStop() - otherRange.getStop() );
		}else{
			return Math.round( getStart() - otherRange.getStart() );
		}

	}
}
