package com.osten.halp.impl.profiling;

import com.osten.halp.api.model.profiling.AdaptiveFilter;
import com.osten.halp.api.model.statistics.DataPoint;
import com.osten.halp.api.model.statistics.Statistic;
import com.osten.halp.impl.statistics.LongDataPoint;
import com.osten.halp.impl.statistics.LongStatistic;

import java.util.List;
import java.util.Properties;

/**
 * This is a Geometric Moving Average filter.
 * Algorithm is as follows:
 * <p/>
 * Estimation:              Theta
 * Weight of measurement:   W
 * Measurement:             Y
 * Lambda:                  a design parameter to adapt the speed and probable error of this filter
 * <p/>
 * Estimation algorithm:
 * Theta = sum( k = 0 TO k = infinity) of ( Wk * Yt )
 * Or
 * Estimation of next value = the sum from start to end of measurements
 */
public class BasicGMAFilter implements AdaptiveFilter<Long> {

    private Statistic<Long> estimates;
    private Statistic<Long> residuals;
    private Statistic<Long> measurements;
    private FilterType filterType;

    public BasicGMAFilter(){
        this.filterType = FilterType.BasicGMA;
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
        return residuals;
    }


    @Override
    public Properties getParameters() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void setParameters(Properties properties) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public FilterType getType() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Enum[] getEnums() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
