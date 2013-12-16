package com.osten.halp.api.model.profiling;

import com.osten.halp.impl.statistics.LongDataPoint;
import com.osten.halp.impl.statistics.LongStatistic;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by osten on 12/10/13.
 */
public class PointsOfInterestTest
{

	@Test
	public void oneAndOneShouldBeOne(){
		LongStatistic statistic = new LongStatistic();

		for(long i = 0; i < 10; i++){
			statistic.addData( new LongDataPoint( i ) );
		}

		Range range = new Range(0L, 10L);
		List<Detection<Long>> detections = new ArrayList <Detection<Long>>();
		List<Detection<Long>> detections2 = new ArrayList <Detection<Long>>();

		Detection<Long> detection = new Detection<Long>( 6l, 9l );
		detection.setStop( 9l );
		Detection<Long> detection2 = new Detection<Long>( 2l, 4l );
		detection2.setStop( 4l );
		Detection<Long> detection3 = new Detection<Long>( 3l, 5l );
		detection3.setStop( 8l );

      detections.add( detection );
		detections.add( detection2 );

		detections2.add( detection3 );

		PointsOfInterest PoI = new PointsOfInterest( statistic );
		PoI.and( detections );

		PoI.and( detections2 );

		assert ( PoI.getPointOfInterest().size() == 2 );
	}

	@Test
	public void twoAndOneShouldBeOne(){
		LongStatistic statistic = new LongStatistic();

		for(long i = 0; i < 100; i++){
			statistic.addData( new LongDataPoint( i ) );
		}
		List<Detection<Long>> detections = new ArrayList <Detection<Long>>();

		Detection<Long> detection = new Detection<Long>( 0l, 35l );
		detection.setStop( 35l );
		Detection<Long> detection2 = new Detection<Long>( 40l, 75l );
		detection2.setStop( 75l );
		Detection<Long> detection3 = new Detection<Long>( 30l, 35l );
		detection3.setStop( 35l );
		Detection<Long> detection4 = new Detection<Long>( 95l, 100l );
		detection4.setStop( 99l );

		detections.add( detection );
		detections.add( detection2 );
		detections.add( detection3 );
		detections.add( detection4 );

		List<Detection<Long>> lotsOfOverlappingDetections = new ArrayList<Detection<Long>>();

		Detection<Long> detection5 = new Detection<Long>( 75l, 100l );
		detection5.setStop( 95l );
		Detection<Long> detection6 = new Detection<Long>( 99l, 100l );
		detection6.setStop( 100l );
		Detection<Long> detection7 = new Detection<Long>( 35l, 100l );
		detection7.setStop( 40l );

		lotsOfOverlappingDetections.add( detection6 );
		lotsOfOverlappingDetections.add( detection5 );
		lotsOfOverlappingDetections.add( detection7 );

		PointsOfInterest PoI = new PointsOfInterest( statistic );
		PoI.and( detections );

		PoI.not( detections );

		PoI.or( lotsOfOverlappingDetections );

		PoI.getPointOfInterest();

	}

}

