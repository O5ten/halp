package com.osten.halp.impl.profiling.filter.wls;

import com.osten.halp.api.model.profiling.AdaptiveFilter;
import com.osten.halp.api.model.statistics.DataPoint;
import com.osten.halp.api.model.statistics.Statistic;
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
public class BasicWLSFilter extends AdaptiveFilter<Long> {
    /**
     * DEFAULTS
     */
    private final int DEFAULT_WINDOW_SIZE = 4;

    /*******************************
     * PROPERTIES OF THIS FILTER
     *******************************/

    /**
     * The N most recent samples will be used to calculate the estimation.
     */
    public static final String WINDOW_SIZE_PROPERTY = "WINDOW_SIZE_PROPERTY";

    public BasicWLSFilter() {
        HashMap<String, Number> settings = new HashMap<String, Number>();
        settings.put(WINDOW_SIZE_PROPERTY, DEFAULT_WINDOW_SIZE);
        initialize( settings, FilterType.BasicWLS);
    }

    public BasicWLSFilter(HashMap<String, Number> settings, FilterType type) {
        initialize( settings, type);
    }

    @Override
    public void adapt(Statistic<Long> measurements) {
        this.signal_measurements = measurements.copyOf();
        this.getMeasurements().setType(Statistic.AggregatedStatisticType.Measurement);
        this.signal_estimates.setName( measurements.getName() );
        this.signal_variance.setName( measurements.getName() );
        this.signal_residuals.setName( measurements.getName() );
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

        long lastEstimate = -1; //The recursive estimation
        for (DataPoint<Long> measuredValue : getMeasurements().getData()) {
            if (window.size() == maximumWindowSize) {
                window.removeLast();
            }
            window.addFirst(measuredValue);

            //Estimate Theta of t : Signal estimation.
            int L = window.size();

            long estimate = 0;
            if (window.size() == 1) {
                estimate = window.getFirst().getData() + ((window.getFirst().getData() - window.getLast().getData()) / L);
                lastEstimate = estimate;
            } else {
                estimate = lastEstimate + ((window.getFirst().getData() - window.getLast().getData()) / L);
                lastEstimate = estimate;
            }

            getEstimates().getData().add(new LongDataPoint(estimate));

            //Minimize error of latest measures in window.
            long lossFactor = 0;
            for (DataPoint<Long> point : window) {
                lossFactor += Math.pow(measuredValue.getData() - estimate, 2);
            }

            //Estimated signal noise
            long estimated_noise = Math.round(1.0 / L * lossFactor);
            this.getResiduals().getData().add(new LongDataPoint(estimated_noise));

            //Estimated signal variance
            double signal_variance = (1.0 / Math.pow(L, 2)) * lossFactor;
            this.getVariance().getData().add(new LongDataPoint(Math.round(signal_variance)));
        }
    }


}
