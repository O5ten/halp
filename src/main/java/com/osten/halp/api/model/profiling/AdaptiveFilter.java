package com.osten.halp.api.model.profiling;

import com.osten.halp.api.model.statistics.DataPoint;
import com.osten.halp.api.model.statistics.Statistic;
import com.osten.halp.impl.statistics.LongStatistic;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-11-05
 * Time: 14:30
 * To change this template use File | Settings | File Templates.
 */
public abstract class AdaptiveFilter<Data> implements Enumerable{

    protected Statistic<Long> signal_estimates;
    protected Statistic<Long> signal_noise;
    protected Statistic<Long> signal_measurements;
    protected Statistic<Long> signal_noise_variance;

    protected FilterType filterType;

    /**
     * Initialize local fields and define their types.
     * @param settings
     */
    protected void initialize( HashMap<String, Number> settings, FilterType type ){
        this.filterType = type;
        this.settings = settings;

        this.signal_estimates = new LongStatistic();
        this.getEstimates().setType(Statistic.AggregatedStatisticType.Estimation);
        this.signal_noise = new LongStatistic();
        this.getResiduals().setType(Statistic.AggregatedStatisticType.Residual);
        this.signal_noise_variance = new LongStatistic();
        this.getVariance().setType(Statistic.AggregatedStatisticType.Variance);
        this.signal_measurements = new LongStatistic();
        this.signal_measurements.setType(Statistic.AggregatedStatisticType.Measurement);
    }

    public void reset(){
        this.signal_estimates.getDataAsList().clear();
        this.signal_noise.getDataAsList().clear();
        this.signal_noise_variance.getDataAsList().clear();
        this.signal_measurements.getDataAsList().clear();
    }

    /**
     * Settings of this filter are the following:
     * Relevant samples = Weights of latest
     */
    protected HashMap<String, Number> settings;

    /**
     * Takes current estimation (local function) and the actual measurement and adapts the local parameters accordingly.
     * @param measurement the actual measurement
     * @return The residue between the measured data and its actual estimation.
     */
    public abstract void adapt( Statistic<Data> measurement );

    /**
     * @return the parameters currently in effect in the form of properties.
     */
    public HashMap<String, Number> getParameters() {
        return settings;
    }

    /**
     * If a new set of properties needs to be
     * @param properties
     */
    public void setParameters(HashMap<String, Number> properties) {
        this.settings = properties;
    }

    /**
     * What type of filter is this.
     * @return
     */
    public FilterType getType() {
        return getFilterType();
    }

    /**
     * Textual representation of aggregated data on measurement according to this filter.
     */
    public void printAggregatedData() {

        System.out.println( "=========== " + this.getMeasurements().getName() + " ===========" );

        System.out.print("Signal Measurements:\n[ ");
        for (DataPoint<Long> point : getMeasurements().getDataAsList()) {
            System.out.print(point.getValue() + " ");
        }
        System.out.println("]");

        System.out.print("Signal Estimates:\n[ ");
        for (DataPoint<Long> point : getEstimates().getDataAsList()) {
            System.out.print(point.getValue() + " ");
        }
        System.out.println("]");

        System.out.print("Signal Noise Variance:\n[ ");
        for (DataPoint<Long> point : getVariance().getDataAsList()) {
            System.out.print(point.getValue() + " ");
        }
        System.out.println("]");

        System.out.print("Signal Noise:\n[ ");
        for (DataPoint<Long> point : getResiduals().getDataAsList()) {
            System.out.print(point.getValue() + " ");
        }
        System.out.println("]");
    }

    @Override
    public Enum[] getEnums() {
        return AdaptiveFilter.FilterType.values();
    }

    public Statistic<Long> getEstimates() {
        return signal_estimates;
    }

    public Statistic<Long> getResiduals() {
        return signal_noise;
    }

    public Statistic<Long> getMeasurements() {
        return signal_measurements;
    }

    public Statistic<Long> getVariance() {
        return signal_noise_variance;
    }

    public FilterType getFilterType() {
        return filterType;
    }

    public enum FilterType {
        Kalman, FastWLS, SlowWLS, BaseWLS
    }
}
