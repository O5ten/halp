package com.osten.halp.api.model.profiling;

import com.osten.halp.api.model.statistics.Statistic;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by osten on 12/10/13.
 */
public class PointsOfInterest
{
	LinkedList<Range> pointsOfInterest;

	public PointsOfInterest( int size )
	{
		this.pointsOfInterest = new LinkedList<Range>();
		pointsOfInterest.add( new Range( 0L, new Long( size ) ) );
	}

	public PointsOfInterest( Statistic<Long> statistic )
	{
		this( statistic.getDataAsList().size());
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
				if( detection.getStart() > range.getStart() && detection.getStop() < range.getStop() )
				{
					newPointsOfInterest.add( new Range( detection.getStart(), detection.getStop() ) );
					continue;
				}

				//Inside to out
				if( detection.getStart() > range.getStart() && detection.getStop() > range.getStop() )
				{
					newPointsOfInterest.add( new Range( detection.getStart(), range.getStop() ) );
					continue;
				}

				//Outside to in
				if( detection.getStart() < range.getStart() && detection.getStop() < range.getStop() )
				{
					newPointsOfInterest.add( new Range( range.getStart(), detection.getStop() ) );
					continue;
				}

				//Overlapping
				if( detection.getStart() < range.getStart() && detection.getStop() > range.getStop() )
				{
					newPointsOfInterest.add( new Range( detection.getStart(), detection.getStop() ) );
				}

				//outside before or after.
				if( ( detection.getStop() < range.getStart() ) || ( detection.getStart() > range.getStop() ) )
				{
					continue;
				}
			}
		}
		if( !newPointsOfInterest.isEmpty() )
		{
			pointsOfInterest = newPointsOfInterest;
		}
	}

	public void nand( List<Detection<Long>> detections )
	{
		for( Detection<Long> detection : detections )
		{
			LinkedList<Range> newPointsOfInterest = new LinkedList<Range>();
			LinkedList<Range> droppedRanges = new LinkedList<Range>();
			for( Range range : pointsOfInterest )
			{
				//Inside
				if( detection.getStart() > range.getStart() && detection.getStop() < range.getStop() )
				{
					newPointsOfInterest.add( new Range( range.getStart(), detection.getStart() ) );
					newPointsOfInterest.add( new Range( detection.getStop(), range.getStop() ) );
					continue;
				}

				//Inside to out
				if( detection.getStart() > range.getStart() && detection.getStop() > range.getStop() && detection.getStart() < range.getStop()  )
				{
					newPointsOfInterest.add( new Range( range.getStart(), detection.getStart() ) );
					continue;
				}

				//Outside to in
				if( detection.getStart() < range.getStart() && detection.getStop() > range.getStart() && detection.getStop() < range.getStop() )
				{
					newPointsOfInterest.add( new Range( detection.getStop(), range.getStop() ) );
					continue;
				}

				//after.
				if( ( detection.getStart() > range.getStop() ) )
				{
					newPointsOfInterest.add( range );
					continue;
				}

				//Before
				if( detection.getStop() < range.getStart() ){
					if(pointsOfInterest.getLast().equals( range )){
						newPointsOfInterest.add( range );
						continue;
					}
				}

				//Overlap
				if(detection.getStart() <= range.getStart() && detection.getStop() >= range.getStop()){
					droppedRanges.add( range );
				}
			}

			if( !newPointsOfInterest.isEmpty() )
			{
				pointsOfInterest = newPointsOfInterest;
			}
			if( newPointsOfInterest.isEmpty() && !droppedRanges.isEmpty()){
				for(Range range : droppedRanges ){
					pointsOfInterest.remove( range );
				}
			}
		}
	}


}
