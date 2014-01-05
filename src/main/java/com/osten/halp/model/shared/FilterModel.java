package com.osten.halp.model.shared;

import com.osten.halp.model.profiling.AdaptiveFilter;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-11-05
 * Time: 14:12
 * To change this template use File | Settings | File Templates.
 */
public interface FilterModel<Data> {

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
    public List<AdaptiveFilter<Data>> getFiltersByStatisticName( String statisticName );

    /**
     * Clears out all the data.
     */
    public void resetModel();

    /**
     * Creates a new filter according to the type selected.
     *
     * @param statisticName
     * @param type
     */
    public void createFilter( String statisticName, AdaptiveFilter.FilterType type );

	/**
	 * Does a statistic have the filter-type already.
	 * @return true or false.
	 */
	public boolean statisticHasFilter( String statisticName, AdaptiveFilter.FilterType type);

    /**
     * Removes an old filter according to the type deselected.
     * @param statisticName
     * @param type
     */
    public void removeFilter( String statisticName, AdaptiveFilter.FilterType type );


}
