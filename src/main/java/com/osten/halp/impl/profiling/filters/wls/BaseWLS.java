package com.osten.halp.impl.profiling.filters.wls;

import com.osten.halp.model.profiling.AdaptiveFilter;
import com.osten.halp.model.statistics.DataPoint;
import com.osten.halp.model.statistics.Statistic;
import com.osten.halp.impl.statistics.LongDataPoint;

import java.util.ArrayDeque;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-11-08
 * Time: 14:03
 * To change this template use File | Settings | File Templates.
 */
public class BaseWLS extends AdaptiveFilter<Long> {

    /**
     * DEFAULTS
     */
    private final int DEFAULT_WINDOW_SIZE = 4;

    /**
     * The N most recent samples will be used to calculate the estimation.
     */
    public static final String WINDOW_SIZE_PROPERTY = "WINDOW_SIZE_PROPERTY";

    public BaseWLS() {
        HashMap<String, Number> settings = new HashMap<String, Number>();
        settings.put(WINDOW_SIZE_PROPERTY, DEFAULT_WINDOW_SIZE);
        initialize(settings, FilterType.BaseWLS);
    }

    public BaseWLS(HashMap<String, Number> settings, FilterType type) {
        initialize(settings, type);
    }

    @Override
    public void adapt(Statistic<Long> measurements) {
        this.signal_measurements = measurements.copyOf();
        this.getMeasurements().setType(Statistic.AggregatedStatisticType.Measurement);
        this.signal_estimates.setName(measurements.getName());
        this.signal_noise_variance.setName(measurements.getName());
        this.signal_noise.setName(measurements.getName());
        performAlgorithm();
    }

    /**
     * The implemented algorithm of this Filter.
     * WLS, Windowed Least Squared.
     *
     * @return
     */
    private void performAlgorithm() {
        int maximumWindowSize = settings.get(WINDOW_SIZE_PROPERTY).intValue();

        //define window
        ArrayDeque<DataPoint<Long>> window = new ArrayDeque<DataPoint<Long>>(maximumWindowSize);
        double noiseMeanCounter = 0;
        double noiseMeanAccumulator = 0;
        double lastEstimate = -1; //The recursive estimation
        getEstimates().addData( new LongDataPoint( 0L ) );
        getVariance().addData( new LongDataPoint( 1L ) );

        for ( int i = 0; i < getMeasurements().getDataAsList().size(); i++){

            if (window.size() == maximumWindowSize) {
                window.removeLast();
            }
            window.addFirst( getMeasurements().getDataByIndex( i ) );

            //Estimate Theta of t : Signal estimation.
            int L = window.size();

            double estimate = 0;
            if (window.size() == 1) {
                estimate = window.getFirst().getValue() + ((window.getFirst().getValue() - window.getLast().getValue()) / L);
                lastEstimate = estimate;
            } else {
                double accumulator = 0;
                double loss = 1;
                for ( DataPoint<Long> measurement : window ){
                    loss /= 2;
                    accumulator += loss * measurement.getValue();
                }
                accumulator += loss * getMeasurements().getDataByIndex( i ).getValue();

                estimate = accumulator;
            }

            //Calculate everything needed for change detectors.
            double residual = getEstimates().getDataByIndex( i ).getValue() - getMeasurements().getDataByIndex(i).getValue();
            noiseMeanCounter++;
            noiseMeanAccumulator += residual;
            double currentMean = noiseMeanAccumulator / noiseMeanCounter;

            double varianceAccumulator = 0;
            for( int j = 0; j < getMeasurements().size(); j++ ){
                varianceAccumulator = Math.pow(getMeasurements().getDataByIndex( j ).getValue() - currentMean, 2);
            }
            double currentNoiseVariance = Math.sqrt( varianceAccumulator / getMeasurements().getDataAsList().size());

            //And place everything where it should be.
            getEstimates().getDataAsList().add( new LongDataPoint( Math.round( estimate ) ) );
            getVariance().getDataAsList().add(new LongDataPoint(Math.round(currentNoiseVariance)));
            getResiduals().getDataAsList().add( new LongDataPoint( Math.abs( Math.round( residual ) ) ) );
        }
    }


}
