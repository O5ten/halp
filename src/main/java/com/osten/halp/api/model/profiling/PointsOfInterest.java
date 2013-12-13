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

				//Inside out
				if( detection.getStart() >= range.getStart() && detection.getStop() > range.getStop() && detection.getStart() <= range.getStop() )
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
				if( detection.getStop() < range.getStart() )
				{
					if( getPointsOfInterest().getLast().equals( range ) )
					{
						newPointsOfInterest.add( range );
						continue;
					}
				}

				//Overlap
				if( detection.getStart() <= range.getStart() && detection.getStop() >= range.getStop() )
				{
					droppedRanges.add( range );
				}
			}

			if( !newPointsOfInterest.isEmpty() )
			{
				setPointsOfInterest( newPointsOfInterest );
			}
			if( newPointsOfInterest.isEmpty() && !droppedRanges.isEmpty() )
			{
				for( Range range : droppedRanges )
				{
					getPointsOfInterest().remove( range );
				}
			}
		}
	}

	public void or( List<Detection<Long>> detections )
	{
		for( Detection<Long> detection : detections )
		{
			LinkedList<Range> newPointsOfInterest = new LinkedList<Range>();
			long previousDetectionStart = -1;

			for( int i = 0; i < getPointsOfInterest().size(); i++ )
			{
				Range range = getPointsOfInterest().get( i );

				//Before and therefore irrelevant.
				if(detection.getStart() > range.getStart()){

				}

				//Inside
				if( detection.getStart() >= range.getStart() && detection.getStop() <= range.getStop() )
				{
					newPointsOfInterest.add( range );
					continue;
				}

				//Inside out
				if( detection.getStart() >= range.getStart() && detection.getStop() > range.getStop() && detection.getStart() <= range.getStop() )
				{
					previousDetectionStart = range.getStart();
				}

				//Outside to in
				if( detection.getStart() <= range.getStart() && detection.getStop() >= range.getStart() && detection.getStop() <= range.getStop() )
				{
					if( previousDetectionStart != -1 )
					{
						newPointsOfInterest.add( new Range( previousDetectionStart, range.getStop() ) );
						previousDetectionStart = -1;
					}
					else
					{
						newPointsOfInterest.add( new Range( detection.getStart(), range.getStop() ) );
					}
				}

				//Overlap
				if( detection.getStart() < range.getStart() && detection.getStop() >= range.getStop() )
				{
					if( previousDetectionStart == -1 )
					{
						previousDetectionStart = detection.getStart();
					}
				}

				//After the last one
				if( detection.getStart() >= range.getStop() && i == getPointOfInterest().size() - 1 )
				{
					if(detection.getStart() == range.getStop()){
						newPointsOfInterest.add( new Range( range.getStart(), detection.getStop() ) );

					}else{
						newPointsOfInterest.add( range );
						newPointsOfInterest.add( new Range( detection.getStart(), detection.getStop() ) );
					}
					break;
				}

				//Inbetween two
				if( detection.getStart() > range.getStop() && detection.getStop() < getPointOfInterest().get( i+1 ).getStart()){
					newPointsOfInterest.add( new Range( detection.getStart(), detection.getStop()) );
				}


				//Last one
				if( i == getPointOfInterest().size() - 1 && previousDetectionStart != -1){
					newPointsOfInterest.add( new Range( previousDetectionStart, detection.getStop() ) );
				}
			}

			if( !newPointsOfInterest.isEmpty() )
			{
				setPointsOfInterest( newPointsOfInterest );
			}
		}
	}

	public void nor()
	{
		throw new UnsupportedOperationException( "Not yet implemented" );
	}


	public LinkedList<Range> getPointsOfInterest()
	{
		return pointsOfInterest;
	}

	public Relevance getRelevance( Range range )
	{

		long relevance = range.getStop() - range.getStart();
		if( relevance < 5 )
		{
			return Relevance.Irrelevant;
		}
		else if( relevance < 10 )
		{
			return Relevance.Moderate;
		}
		else
		{
			return Relevance.Substantial;
		}

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

	private enum Relevance
	{
		Irrelevant, Moderate, Substantial
	}
}
