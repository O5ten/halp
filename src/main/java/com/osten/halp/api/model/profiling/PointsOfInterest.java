package com.osten.halp.api.model.profiling;

import com.osten.halp.api.model.shared.ProfileModel;
import com.osten.halp.api.model.statistics.Statistic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by osten on 12/10/13.
 */
public class PointsOfInterest
{
	private LinkedList<Range> pointsOfInterest = new LinkedList<Range>();
	private ProfileModel.Profile profile;
	private List<Statistic<Long>> involvedStatistics;

	public PointsOfInterest( int size )
	{
		getPointsOfInterest().add( new Range( 0L, new Long( size ) ) );
		involvedStatistics = new ArrayList<Statistic<Long>>();
	}

	public PointsOfInterest( Statistic<Long> statistic )
	{
		this( statistic.getDataAsList().size() );
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
			for( Range range : getPointsOfInterest() )
			{
				//Inside
				if( detection.getStart() >= range.getStart() && detection.getStop() <= range.getStop() )
				{
					newPointsOfInterest.add( new Range( detection.getStart(), detection.getStop() ) );
					continue;
				}

				//Inside to out
				if( detection.getStart() >= range.getStart() && detection.getStop() >= range.getStop() )
				{
					newPointsOfInterest.add( new Range( detection.getStart(), range.getStop() ) );
					continue;
				}

				//Outside to in
				if( detection.getStart() <= range.getStart() && detection.getStop() <= range.getStop() )
				{
					newPointsOfInterest.add( new Range( range.getStart(), detection.getStop() ) );
					continue;
				}

				//Overlapping
				if( detection.getStart() <= range.getStart() && detection.getStop() >= range.getStop() )
				{
					newPointsOfInterest.add( new Range( detection.getStart(), detection.getStop() ) );
					continue;
				}

				//outside before or after.
				if( ( detection.getStop() <= range.getStart() ) || ( detection.getStart() >= range.getStop() ) )
				{
					continue;
				}
			}
		}
		if( !newPointsOfInterest.isEmpty() )
		{
			setPointsOfInterest( newPointsOfInterest );
		}
	}

	public void nand( List<Detection<Long>> detections )
	{
		for( Detection<Long> detection : detections )
		{
			LinkedList<Range> newPointsOfInterest = new LinkedList<Range>();
			LinkedList<Range> droppedRanges = new LinkedList<Range>();
			for( Range range : getPointsOfInterest() )
			{
				//Inside
				if( detection.getStart() >= range.getStart() && detection.getStop() <= range.getStop() )
				{
					newPointsOfInterest.add( new Range( range.getStart(), detection.getStart() ) );
					newPointsOfInterest.add( new Range( detection.getStop(), range.getStop() ) );
					continue;
				}

				//Inside to out
				if( detection.getStart() >= range.getStart() && detection.getStop() > range.getStop() && detection.getStart() <= range.getStop()  )
				{
					newPointsOfInterest.add( new Range( range.getStart(), detection.getStart() ) );
					continue;
				}

				//Outside to in
				if( detection.getStart() <= range.getStart() && detection.getStop() >= range.getStart() && detection.getStop() <= range.getStop() )
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
					if( getPointsOfInterest().getLast().equals( range )){
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
				setPointsOfInterest( newPointsOfInterest );
			}
			if( newPointsOfInterest.isEmpty() && !droppedRanges.isEmpty()){
				for(Range range : droppedRanges ){
					getPointsOfInterest().remove( range );
				}
			}
		}
	}


	public LinkedList<Range> getPointsOfInterest()
	{
		return pointsOfInterest;
	}

	public void setPointsOfInterest( LinkedList<Range> pointsOfInterest )
	{
		this.pointsOfInterest = pointsOfInterest;
	}

	public ProfileModel.Profile getProfile()
	{
		return profile;
	}

	public void setProfile( ProfileModel.Profile profile )
	{
		this.profile = profile;
	}

	public List<Statistic<Long>> getInvolvedStatistics()
	{
		return involvedStatistics;
	}

	public void addInvolvedStatistic( Statistic<Long> involvedStatistic )
	{
		involvedStatistics.add( involvedStatistic );
	}
}
