package com.osten.halp.model.profiling;

import com.osten.halp.model.shared.ProfileModel;
import com.osten.halp.model.statistics.Statistic;

import java.util.ArrayList;
import java.util.Collections;
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

				//outside before or after.
				if( ( detection.getStop() <= range.getStart() ) || ( detection.getStart() >= range.getStop() ) )
				{
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
					newPointsOfInterest.add( new Range( range.getStart(), range.getStop() ) );
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

	public void or( List<Detection<Long>> detectionList )
	{
		if( !detectionList.isEmpty() )
		{
			LinkedList<Range> original = new LinkedList<Range>();
			LinkedList<Range> detections = new LinkedList<Range>();

			for( Detection<Long> detection : detectionList )
			{
				detections.add( detectionToRange( detection ) );
			}
			Collections.sort( detections );
			original.addAll( pointsOfInterest );

			//New list of pointsOfInterests;
			LinkedList<Range> store = new LinkedList<Range>();

			//Initial search first the next first.

			if( detections.isEmpty() ){
				return;
			}

			Range A = original.pop();
			Range B = detections.pop();

			//LinkedList hopping between smallest starts of detections and points of interests and assembles a list containing the OR result of both lists.
			do
			{
				//Identical segments. continue.
				if( A.getStop().equals( B.getStop() ) && A.getStart().equals( B.getStart() ) )
				{
					store.add( A );
					if( hasNext( original, detections )){
						A = original.pop();
						B = detections.pop();
					}
					continue;
				}

				//B segment is behind A is behind B.
				if ( B.getStop() < A.getStart() ){
					store.add( B );
					if(hasNext( detections ) ){
						B = detections.pop();
					}
				}

				//A segment is behind B, store it.
				if( A.getStop() < B.getStart() )
				{
					store.add( A );
					if( hasNext( original )){
						A = original.pop();
					}
				}

				//B segment is inside A.
				if( A.getStop() > B.getStart() && A.getStop() >= B.getStop() )
				{
					if(hasNext( detections )){
						B = detections.pop();
					}
				}

				//A segment is inside B, don't care.
				if( A.getStop() <= B.getStop() && A.getStart() >= B.getStart()){
					if(hasNext( original ) ){
						A = original.pop();
					}
				}

				//B Segment overlaps before A segment
				if( A.getStart() > B.getStart() && A.getStop() > B.getStop() ){
					B = new Range( B.getStart(), A.getStop() );
					if(hasNext( original )){
						A = original.pop();
					}
				}

				//B segment overlaps after A segment.
				if( A.getStop() > B.getStart() && A.getStop() < B.getStop() && A.getStart() < B.getStart())
				{
					B = new Range( A.getStart(), B.getStop() );
					if(hasNext( original )){
						A = original.pop();
					}
				}

				if( !hasNext( original ) ){
					if( B.getStart() < A.getStart() ){
						store.add( B );
					}else{
						store.add( A );
					}
				}

			} while( original.size() > 0 || detections.size() > 0 );

 			 pointsOfInterest = store;
		}
	}

	public boolean hasNext( LinkedList<Range> listA, LinkedList<Range> listB )
	{
		return listA.size() > 0 && listB.size() > 0;
	}

	public boolean hasNext( LinkedList<Range> list )
	{
		return list.size() > 0;
	}


	public boolean first( LinkedList<Range> listA, LinkedList<Range> listB )
	{

		Range a = listA.peek();
		Range b = listB.peek();
		if( b.getStart() > a.getStart() )
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean first( LinkedList<Range> listA, Range B )
	{
		if( !listA.isEmpty() )
		{
			if( B.getStart() > listA.peek().getStart() )
			{
				return true;
			}
		}
		return false;
	}

	private Range detectionToRange( Detection<Long> detection )
	{
		return new Range( detection.getStart(), detection.getStop() );
	}

	public void not( List<Detection<Long>> notDetectionList )
	{
		if( !notDetectionList.isEmpty() )
		{
			LinkedList<Range> original = new LinkedList<Range>();
			LinkedList<Range> detections = new LinkedList<Range>();

			for( Detection<Long> detection : notDetectionList )
			{
				detections.add( detectionToRange( detection ) );
			}

			if( pointsOfInterest.size() == 1 )
			{
				original.add( pointsOfInterest.getFirst() );
			}
			else
			{
				original.addAll( pointsOfInterest );
			}


			Collections.sort( detections );
			Collections.sort( original );
			//New list of pointsOfInterests;
			LinkedList<Range> store = new LinkedList<Range>();

			if(original.size() == 0 || detections.size() == 0){
				return;
			}

			Range A = original.pop();
			Range B = detections.pop();

			//LinkedList hopping between smallest starts of detections and points of interests and assembles a list containing the OR result of both lists.
			do
			{
				//Identical segments. Remove.
				if( A.getStop().equals( B.getStop() ) && A.getStart().equals( B.getStart() ) )
				{
					if( hasNext( original ) ){
						A = original.pop();
					}
					if( hasNext( detections ) ){
						B = detections.pop();
					}
					continue;
				}

				//Segment A Before B
				if( A.getStop() <= B.getStart() )
				{
					store.add( A );
					if( hasNext( original ) )
					{
						A = original.pop();
					}
					else
					{
						break;
					}
					continue;
				}

				//Segment A After B
				if( A.getStart() >= B.getStop() )
				{
					if( hasNext( detections ) )
					{
						B = detections.pop();
					}else{
						if( hasNext( original )){
							A = original.pop();
						}
					}
					continue;
				}

				//since B segment is inside A we can conclude that the first part of A before B is a new store and recreate A to be the second part.
				if( A.getStart() <= B.getStart() && A.getStop() > B.getStop() )
				{
					if( A.getStart() < B.getStart() )
					{
						store.add( new Range( A.getStart(), B.getStart() ) );
					}
					A = new Range( B.getStop(), A.getStop() );
					if(hasNext(detections)){
						B = detections.pop();
					}else{
						store.add( A );
					}
					continue;
				}

				//Segment B is inside segment A
				if( A.getStart() >= B.getStart() && A.getStop() <= B.getStop()){
					if( hasNext( original ) ){
						A = original.pop();
					}
					continue;
				}

				//B segment overlaps after A segment.
				if( A.getStop() >= B.getStart() && A.getStop() < B.getStop() )
				{
					store.add( new Range( A.getStart(), B.getStart() ) );
					if(hasNext(original)){
						A = original.pop();
					}
					continue;
				}

				//Overlapping in to
				if( A.getStart() > B.getStart() && A.getStop() > B.getStop() )
				{
					A = new Range( B.getStop(), A.getStop() );
				}

				//Stop the last element.
				if( !hasNext( detections ) )
				{
					store.add( new Range( A.getStart(), B.getStart() ) );

					if( B.getStop() < A.getStop() )
					{
						store.add( new Range( B.getStop(), A.getStop() ) );
					}
				}
			} while( original.size() > 0 || detections.size() > 0 );

			//Cleanup, if anything is left in any of the lists then push them onto store
			if( original.size() > 0 )
			{
				for( Range range : original )
				{
					store.add( range );
				}
			}

			pointsOfInterest = store;
		}
	}


	public LinkedList<Range> getPointsOfInterest()
	{
		return pointsOfInterest;
	}

	public Relevance getRelevance( Range range )
	{

		long relevance = range.getStop() - range.getStart();
		if( relevance < 3 )
		{
			return Relevance.Irrelevant;
		}
		else if( relevance < 15 )
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

	public enum Relevance
	{
		Irrelevant, Moderate, Substantial
	}
}
