package com.osten.halp.api.model.profiling;

import java.util.Map;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-11-05
 * Time: 14:30
 * To change this template use File | Settings | File Templates.
 */
public abstract class AdaptiveFilter<Data> {


    /**
     * Takes current estimation (local function) and the actual measurement and adapts the local parameters accordingly.
     * @param measurement
     */
    public abstract void adapt( Data measurement );

    /**
     * Estimates a new value based on the latest batch of measurements.
     * @return
     */
    public abstract Data estimate();

    /**
     *
     * @return the parameters currently in effect in the form of properties.
     */
    public abstract Properties getParameters();

    /**
     * If a new set of properties needs to be
     * @param properties
     */
    public abstract void setParameters( Properties properties );

    public enum Type{
        LMR, AGF, ABB
    }
}
