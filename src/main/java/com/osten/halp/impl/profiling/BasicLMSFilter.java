package com.osten.halp.impl.profiling;

import com.osten.halp.api.model.profiling.AdaptiveFilter;
import com.osten.halp.api.model.statistics.Statistic;
import com.osten.halp.impl.statistics.LongDataPoint;
import com.osten.halp.impl.statistics.LongStatistic;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-11-06
 * Time: 11:33
 * To change this template use File | Settings | File Templates.
 */
public class BasicLMSFilter extends AdaptiveFilter<Long> {

    private Statistic<Long> estimates;
    private Statistic<Long> residuals;
    private Statistic<Long> measurements;
    private FilterType filterType;

    public BasicLMSFilter(){
        this.filterType = FilterType.BasicLMS;
    }

    @Override
    public void adapt(Statistic<Long> measurements) {

        this.measurements = measurements.copyOf();

        this.estimates = performAlgorithm();
        this.estimates.setType( Statistic.AggregatedStatisticType.Estimation );

        this.residuals = calculateResiduals();
        this.residuals.setType( Statistic.AggregatedStatisticType.Residual );
    }

    private LongStatistic performAlgorithm() {
        LongStatistic estimates = new LongStatistic(measurements.getName());

        //TODO Algorithm
        return estimates;
    }

    private LongStatistic calculateResiduals() {
        LongStatistic residuals = new LongStatistic(measurements.getName());

        for (int i = 0; i < measurements.getData().size(); i++) {
            residuals.insertData(i, new LongDataPoint(
                    Math.abs(
                            measurements.getDataByIndex(i).getData() - estimates.getDataByIndex(i).getData()
                    )
            )
            );
        }
        return residuals;
    }

    @Override
    public Statistic<Long> getEstimates() {
        return estimates;
    }

    @Override
    public Statistic<Long> getResiduals() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public HashMap<String, Number> getParameters() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void setParameters(HashMap<String, Number> properties) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public FilterType getType() {
       return filterType;
    }

    @Override
    public void printAggregatedData() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Enum[] getEnums(){
        return FilterType.values();
    }
}
