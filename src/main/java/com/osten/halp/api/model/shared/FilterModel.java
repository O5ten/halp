package com.osten.halp.api.model.shared;

import com.osten.halp.api.model.profiling.AdaptiveFilter;
import com.osten.halp.api.model.statistics.Statistic;

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
     * Adapt performs the rough calculation needed for a statistic to turn into a filter, an estimation and a residual.
     * This should _NOT_ run in an interface-thread.
     *
     * @param name        What it this filter called
     * @param actualValue
     * @return Returns a map of the margins of error between actualValue and Estimation, the key is the name of the filter.
     */
    public void adapt(String name, Statistic<Data> actualValue);

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


}
