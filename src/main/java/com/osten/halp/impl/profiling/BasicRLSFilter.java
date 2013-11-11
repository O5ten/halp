package com.osten.halp.impl.profiling;

import com.osten.halp.api.model.profiling.AdaptiveFilter;
import com.osten.halp.api.model.statistics.DataPoint;
import com.osten.halp.api.model.statistics.Statistic;
import com.osten.halp.impl.statistics.LongDataPoint;
import com.osten.halp.impl.statistics.LongStatistic;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-11-08
 * Time: 14:03
 * To change this template use File | Settings | File Templates.
 */
public class BasicRLSFilter implements AdaptiveFilter<Long>{

    /**
     * DEFAULTS
     */
    private final int DEFAULT_RECENT_SAMPLES = 5;
    private final int DEFAULT_RELEVANCE_FACTOR = 2;

    /*******************************
     * PROPERTIES OF THIS FILTER
     *******************************/

    /**
     * The N most recent samples will be used to calculate the estimation.
     */
    public final String RECENT_SAMPLES_PROPERTY = "RECENT_SAMPLES";

    /**
     * The N most recent samples are weighted depending on time away from estimation.
     * By default its an inverse logarithmic function with a sum resulting in 1.
     *
     * The Relevance_factor is the level of decline of that logarithmic function as of
     * Wk: [ 1/(2*F) ] where F is the distance from estimation position k for each value
     * up to at the most the length of RECENT_SAMPLES.
     *
     */
    public final String RELEVANCE_FACTOR_PROPERTY = "RELEVANCE_FACTOR";

    private Statistic<Long> estimates;
    private Statistic<Long> residuals;
    private Statistic<Long> measurements;
    private FilterType filterType;

    /**
     * Settings of this filter are the following:
     * Relevant samples = Weights of latest
     *
     */
    private Properties settings;

    public BasicRLSFilter(){
        this.filterType = FilterType.BasicLMS;
        this.settings = new Properties();
        this.settings.put( "RECENT_SAMPLES", DEFAULT_RECENT_SAMPLES );
        this.settings.put( "RELEVANCE_FACTOR", DEFAULT_RELEVANCE_FACTOR );
    }

    public BasicRLSFilter( Properties settings ){
        this.filterType = FilterType.BasicLMS;
        this.settings = settings;
    }

    @Override
    public void adapt(Statistic<Long> measurements) {

        this.measurements = measurements.copyOf();

        this.estimates = performAlgorithm();
        this.estimates.setType( Statistic.AggregatedStatisticType.Estimation );

        this.residuals = calculateResiduals();
        this.residuals.setType( Statistic.AggregatedStatisticType.Residual );
    }

    /**
     * The implemented algorithm of this Filter.
     *
     * RLS
     * transposed W multiplied by the vector of previous measurements.
     * @return
     */
    private LongStatistic performAlgorithm() {
        LongStatistic estimates = new LongStatistic(measurements.getName());
        ArrayDeque<DataPoint<Long>> fifo = new ArrayDeque< DataPoint <Long>>( RECENT_SAMPLES );

        for( DataPoint<Long> measuredValue : measurements.getData() ){


        }

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
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Statistic<Long> getResiduals() {
        throw new UnsupportedOperationException("Not yet implemented");
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
