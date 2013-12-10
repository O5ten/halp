package com.osten.halp.api.model.profiling;

import com.osten.halp.api.model.statistics.Statistic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by osten on 12/10/13.
 */
public class PointOfInterest
{
	LinkedList<Range> pointsOfInterest;

	public PointOfInterest()
	{
		this.pointsOfInterest = new LinkedList<Range>();
	}

	public PointOfInterest( Statistic<Long> statistic )
	{
		this();
		pointsOfInterest.add( new Range( 0L, new Long(statistic.getDataAsList().size() ) ) );
	}

	public List<Range> getPointOfInterest()
	{
		return pointsOfInterest;
	}

	public void and( List<Detection<Long>> detections )
	{
		LinkedList<Range> newPointsOfInterest = new LinkedList<Range>();
		for( Detection<Long> detection : detections )
		{
			for( Range range : pointsOfInterest )
			{
				//Inside
				if( detection.getStart() > range.getFrom() && detection.getStop() < range.getTo() )
				{
					newPointsOfInterest.add( new Range( detection.getStart(), detection.getStop()) );
					continue;
				}

				//Inside to out
				if( detection.getStart() > range.getFrom() && detection.getStop() > range.getTo() )
				{
					newPointsOfInterest.add( new Range( detection.getStart(), range.getTo()) );
					continue;
				}

				//Outside to in
				if( detection.getStart() < range.getFrom() && detection.getStop() < range.getTo() )
				{
					newPointsOfInterest.add( new Range( range.getFrom(), detection.getStop()));
					continue;
				}

				//Overlapping
				if( detection.getStart() < range.getFrom() && detection.getStop() > range.getTo() ){
					newPointsOfInterest.add( new Range( detection.getStart(), detection.getStop() ) );
				}

				//outside before or after.
				if( ( detection.getStop() < range.getFrom() ) || ( detection.getStart() > range.getTo() ) )
				{
					continue;
				}
			}
		}
		pointsOfInterest = newPointsOfInterest;
	}

	public void nand( Detection<Long> detection )
	{
		//TODO Not finished, copied from above.
		/*for( Detection<Long> detection : detections )
		{
			LinkedList<Range> newPointsOfInterest = new LinkedList<Range>();
			for( Range range : pointsOfInterest )
			{
				//Inside
				if( detection.getStart() > range.getFrom() && detection.getStop() < range.getTo() )
				{
					newPointsOfInterest.add( new Range( detection.getStart(), detection.getStop()) );
					continue;
				}

				//Inside to out
				if( detection.getStart() > range.getFrom() && detection.getStop() > range.getTo() )
				{
					newPointsOfInterest.add( new Range( detection.getStart(), range.getTo()) );
					continue;
				}

				//Outside to in
				if( detection.getStart() < range.getFrom() && detection.getStop() < range.getTo() )
				{
					newPointsOfInterest.add( new Range( range.getFrom(), detection.getStop()));
					continue;
				}

				//outside before or after.
				if( ( detection.getStop() < range.getFrom() ) || ( detection.getStart() > range.getTo() ) )
				{
					continue;
				}
			}
		}   */
	}


}
