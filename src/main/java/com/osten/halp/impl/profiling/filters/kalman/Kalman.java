package com.osten.halp.impl.profiling.filters.kalman;

import com.osten.halp.api.model.profiling.AdaptiveFilter;
import com.osten.halp.api.model.statistics.DataPoint;
import com.osten.halp.api.model.statistics.Statistic;
import com.osten.halp.impl.statistics.LongDataPoint;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-11-21
 * Time: 11:41
 * To change this template use File | Settings | File Templates.
 */
public class Kalman extends AdaptiveFilter<Long>
{

	/* customizable, will make the filter converge faster or slower */
	private final double DEFAULT_NOISE_VARIANCE = 0.0001;
	private final double DEFAULT_INITIAL_ESTIMATE = 0;
	private final double DEFAULT_INITIAL_ERROR_COVARIANCE = 1;

	public static final String NOISE_VARIANCE_PROPERTY = "NOISE_VARIANCE_PROPERTY";
	public static final String INITIAL_ESTIMATE_PROPERTY = "INITIAL_ESTIMATE_PROPERTY";
	public static final String INITIAL_ERROR_COVARIANCE_PROPERTY = "INITIAL_ERROR_COVARIANCE_PROPERTY";

	public Kalman()
	{
		HashMap<String, Number> settings = new HashMap<String, Number>();
		settings.put( NOISE_VARIANCE_PROPERTY, DEFAULT_NOISE_VARIANCE );
		settings.put( INITIAL_ESTIMATE_PROPERTY, DEFAULT_INITIAL_ESTIMATE );
		settings.put( INITIAL_ERROR_COVARIANCE_PROPERTY, DEFAULT_INITIAL_ERROR_COVARIANCE );
		initialize( settings, FilterType.Kalman );
	}

	public Kalman( HashMap<String, Number> settings )
	{
		this.settings = settings;
		initialize( settings, FilterType.Kalman );
	}

	/**
	 * This filter assumes some things about the general case of the kalman filter.
	 * First off, the general assumption suggests that any measurement x in state k is a linear combination of its previous value and a control signal u in state k and a process noise.
	 * In this implementation it there is no control signal and therefore it is left out of the algorithm.
	 * Also entities A, B and H which are constant coefficients to the parameters X in state k-1, the control signal and noise-functions are assumed to be one as is very common practice
	 * when a filter is used in a one-dimensional environment such as this.
	 * These assumtions simplifies the calculational effort of the kalman-filter severely.
	 */
	@Override
	public void adapt( Statistic<Long> measurements )
	{
		this.signal_measurements = measurements;

		double priorEstimate = settings.get( INITIAL_ESTIMATE_PROPERTY ).doubleValue();
		double priorErrorCovariance = settings.get( INITIAL_ERROR_COVARIANCE_PROPERTY ).doubleValue();
		double environmentNoise = settings.get( NOISE_VARIANCE_PROPERTY ).doubleValue();

        //calculations to ultimately get a noise-variance.
        double noiseMeanAccumulator = 0;
        double noiseMeanCounter = 0;

		this.signal_estimates.addData( new LongDataPoint( settings.get( INITIAL_ESTIMATE_PROPERTY ).longValue() ) );
		this.signal_noise_variance.addData( new LongDataPoint( settings.get( INITIAL_ERROR_COVARIANCE_PROPERTY ).longValue() ) );

		for( int i = 0; i < getMeasurements().getDataAsList().size(); i++ )
		{
			DataPoint<Long> measurement = getMeasurements().getDataAsList().get( i );

			//This measurement compared to the latest estimate produces a residual which can be measured in terms of change detection.
			double residual = signal_estimates.getDataAsList().get( i ).getValue() - measurement.getValue();
            noiseMeanCounter++;
            noiseMeanAccumulator += residual;
            double currentMean = noiseMeanAccumulator / noiseMeanCounter;

            //Noise variance update.
            double varianceAccumulator = 0;
            for( int j = 0; j < getMeasurements().getDataAsList().size(); j++ ){
                varianceAccumulator = Math.pow(getMeasurements().getDataAsList().get(j).getValue() - currentMean, 2);
            }
            double currentNoiseVariance = Math.sqrt( varianceAccumulator / getMeasurements().getDataAsList().size() );

			//Correction
			double kalmanGain = priorErrorCovariance / ( priorErrorCovariance + (environmentNoise ) );
			double actualEstimate = priorEstimate + kalmanGain * ( measurement.getValue() - priorEstimate );
			double actualErrorCovariance = ( 1 - kalmanGain ) * priorErrorCovariance;

			//For next round
			priorEstimate = actualEstimate;
			priorErrorCovariance = actualErrorCovariance;

			//And place everything where it should be.
			signal_estimates.getDataAsList().add( new LongDataPoint( Math.round( actualEstimate ) ) );
			signal_noise_variance.getDataAsList().add( new LongDataPoint( Math.round( currentNoiseVariance ) ) );
			signal_noise.getDataAsList().add( new LongDataPoint( Math.abs( Math.round( residual ) ) ) );
		}
		printAggregatedData();
	}
}
