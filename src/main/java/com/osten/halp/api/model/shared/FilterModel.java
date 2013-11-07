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
     * Adapt parameters governing this algorithm so that the estimation of the next value is adapting to historical data and pattern of this filter.
     *
     * @param name        What it this filter called
     * @param actualValue
     * @return Returns a map of the margins of error between actualValue and Estimation, the key is the name of the filter.
     */
    public Map<AdaptiveFilter.FilterType, Data> adapt(String name, Data actualValue);

    /**
     * Get the absolute residue between the latest estimation and latest measurement for each filter.
     * This is also shown by the adapt-method, but will only act on the latest complete measurement and estimation.
     *
     * @param name
     * @return
     */
    public Map<AdaptiveFilter.FilterType, Data> getEstimations( String name );

    /**
     * Returns the specific filter that a statisticname holds.
     *
     * @param filterType the name of the statistic owning the filter.
     * @param statisticName the name of the statistic holding the filters.
     * @return the Filter
     */
    public AdaptiveFilter<Data> getFilter(String statisticName, AdaptiveFilter.FilterType filterType);

    /**
     * Returns the specific filters that a statisticname holds.
     *
     * @param statisticName the name of the statistic holding the filters.
     * @return the Filter
     */
    public List<AdaptiveFilter<Data>> getFilters(String statisticName);


    /**
     * Creates a new filter according to the type selected.
     *
     * @param statisticName
     * @param type
     */
    public void createFilter( String statisticName, AdaptiveFilter.FilterType type );

    /**
     * Removes an old filter according to the type deselected.
     * @param statisticName
     * @param type
     */
    public void removeFilter( String statisticName, AdaptiveFilter.FilterType type );

    /**
     * Estimate the next value based on historic measurements.
     * Returns a map of estimations per filter, but estimation is stored locally.
     *
     * This method is closely tied to the adapt-method that provides more historical data.
     * If estimation is attempted several times before the adapt-method is not called, then the same estimation as before is returned.
     * This is because an empty/0/impossible measurement should still keep an estimation if needed.
     *
     * @param name
     * @return the estimations, sorted on FilterType.
     */
    public Map<AdaptiveFilter.FilterType, Data> estimate( String name );

}
