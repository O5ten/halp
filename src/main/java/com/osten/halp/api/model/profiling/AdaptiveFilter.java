package com.osten.halp.api.model.profiling;

import com.osten.halp.api.model.statistics.Statistic;

import java.util.List;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-11-05
 * Time: 14:30
 * To change this template use File | Settings | File Templates.
 */
public interface AdaptiveFilter<Data> extends Enumerable{

    /**
     * Takes current estimation (local function) and the actual measurement and adapts the local parameters accordingly.
     * @param measurement the actual measurement
     * @return The residue between the measured data and its actual estimation.
     */
    public void adapt( Statistic<Data> measurement );

    /**
     * Returns a list of all the historical estimates this filter has done.
     * @return
     */
    public Statistic<Data> getEstimates();

    /**
     * Get all residuals this filter has produced after adaptations.
     * @return
     */
    public Statistic<Data> getResiduals();

    /**
     *
     * @return the parameters currently in effect in the form of properties.
     */
    public Properties getParameters();

    /**
     * If a new set of properties needs to be
     * @param properties
     */
    public void setParameters( Properties properties );

    public FilterType getType();


    public enum FilterType {
        BasicLMS, BasicRLS, BasicGMA
    }
}
