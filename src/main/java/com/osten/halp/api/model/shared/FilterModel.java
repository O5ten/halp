package com.osten.halp.api.model.shared;

import com.osten.halp.api.model.profiling.AdaptiveFilter;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-11-05
 * Time: 14:12
 * To change this template use File | Settings | File Templates.
 */
public interface FilterModel<Data> {

    /**
     * @param name        What it this filter called
     * @param actualValue
     * @return Returns a map of the margins of error between actualValue and Estimation, the key is the name of the filter.
     */
    public Map<String, Data> adapt(String name, Data actualValue);

    /**
     * Get the absolute residue between the latest estimation and latest measurement.
     *
     * @param name
     * @return
     */
    public List<Data> getEstimate(String name);

    /**
     * Returns the specific filter type.
     *
     * @param name the name of the statistic owning the filter.
     * @return
     */
    public AdaptiveFilter<Data> getFilter(String name);

    /**
     * Estimate the next value based on historic data.
     *
     * @param name
     * @return
     */
    public List<Data> estimate( String name );

}
