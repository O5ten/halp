package com.osten.halp.api.model.profiling;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-11-05
 * Time: 14:30
 * To change this template use File | Settings | File Templates.
 */
public interface AdaptiveFilter<Data> {

    /**
     * Takes current estimation (local function) and the actual measurement and adapts the local parameters accordingly.
     * @param measurement the actual measurement
     * @return The residue between the measured data and its actual estimation.
     */
    public Data adapt( Data measurement );

    /**
     * Estimates a new value based on the latest batch of measurements.
     * @return
     */
    public Data estimate();

    /**
     * Returns a list of all the historical estimates this filter has done.
     * @return
     */
    public List<Data> getAllEstimates();

    /**
     * Get all residuals this filter has produced after adaptations.
     * @return
     */
    public List<Data> getAllResiduals();

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

    public Type getType();

    public enum Type{
        LMS
    }
}
