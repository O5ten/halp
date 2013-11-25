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
    protected Statistic<Long> signal_residuals;
    protected Statistic<Long> signal_measurements;
    protected Statistic<Long> signal_variance;

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
        this.signal_residuals = new LongStatistic();
        this.getResiduals().setType(Statistic.AggregatedStatisticType.Residual);
        this.signal_variance = new LongStatistic();
        this.getVariance().setType(Statistic.AggregatedStatisticType.Variance);
        this.signal_measurements = new LongStatistic();
        this.signal_measurements.setType(Statistic.AggregatedStatisticType.Measurement);
    }

    public void reset(){
        this.signal_estimates.getData().clear();
        this.signal_residuals.getData().clear();
        this.signal_variance.getData().clear();
        this.signal_measurements.getData().clear();
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
        for (DataPoint<Long> point : getMeasurements().getData()) {
            System.out.print(point.getData() + " ");
        }
        System.out.println("]");

        System.out.print("Signal Estimates:\n[ ");
        for (DataPoint<Long> point : getEstimates().getData()) {
            System.out.print(point.getData() + " ");
        }
        System.out.println("]");

        System.out.print("Signal Variance:\n[ ");
        for (DataPoint<Long> point : getVariance().getData()) {
            System.out.print(point.getData() + " ");
        }
        System.out.println("]");

        System.out.print("Signal Residuals:\n[ ");
        for (DataPoint<Long> point : getResiduals().getData()) {
            System.out.print(point.getData() + " ");
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
        return signal_residuals;
    }

    public Statistic<Long> getMeasurements() {
        return signal_measurements;
    }

    public Statistic<Long> getVariance() {
        return signal_variance;
    }

    public FilterType getFilterType() {
        return filterType;
    }

    public enum FilterType {
		 FastWLS, SlowWLS, BaseWLS, Kalman
    }
}
