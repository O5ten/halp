package com.osten.halp.api.model.profiling;

import com.osten.halp.impl.statistics.LongDataPoint;
import com.osten.halp.impl.statistics.LongStatistic;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by osten on 12/10/13.
 */
public class PointOfInterestTest
{

	@Test
	public void oneAndOneShouldBeOne(){
		System.out.println("Preparing to test Point of Interest class.");

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

		PointOfInterest PoI = new PointOfInterest( statistic );
		PoI.and( detections );

		PoI.and( detections2 );

		assert ( PoI.getPointOfInterest().size() == 2 );
	}

}

