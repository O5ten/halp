package com.osten.halp.impl.shared;

import com.osten.halp.api.model.profiling.AdaptiveFilter;
import com.osten.halp.api.model.profiling.ChangeDetector;
import com.osten.halp.api.model.profiling.StopRule;
import com.osten.halp.api.model.shared.FilterModel;
import com.osten.halp.errorhandling.UnsupportedFilterException;
import com.osten.halp.impl.profiling.filters.kalman.Kalman;
import com.osten.halp.impl.profiling.filters.wls.BaseWLS;
import com.osten.halp.impl.profiling.filters.wls.FastWLS;
import com.osten.halp.impl.profiling.filters.wls.SlowWLS;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-11-05
 * Time: 16:20
 * To change this template use File | Settings | File Templates.
 */
public class LongFilterModel implements FilterModel<Long> {

    Map<String, List<AdaptiveFilter<Long>>> filters;
    Map<String, List<ChangeDetector<Long>>> detectors;

    public LongFilterModel() {
        filters = new HashMap<String, List<AdaptiveFilter<Long>>>();
    }

    @Override
    public AdaptiveFilter<Long> getFilter(String statisticName, AdaptiveFilter.FilterType filterType) {
        AdaptiveFilter<Long> foundFilter = null;
        if( filters.get(statisticName) == null ){
            return foundFilter;
        }
        for ( AdaptiveFilter<Long> filter : filters.get( statisticName ) ){
            if( filter.getType() == filterType){
                foundFilter = filter;
                break;
            }
        }
        return foundFilter;
    }

    @Override
    public List<AdaptiveFilter<Long>> getFiltersByStatisticName( String statisticName ) {
		 List<AdaptiveFilter<Long>> filterList = filters.get( statisticName );
		 if( filterList != null)
		 {
		 		return filters.get(statisticName);
	 	 }else{
			 	return new ArrayList<AdaptiveFilter<Long>>();
		 }
    }

    @Override
    public void resetModel() {
        this.filters.clear();
    }

    @Override
    public void createFilter(String statisticName, AdaptiveFilter.FilterType filterType) {
        AdaptiveFilter<Long> filter;

        switch (filterType) {
            case BaseWLS:
                filter = new BaseWLS();
                break;
            case SlowWLS:
                filter = new SlowWLS();
                break;
            case FastWLS:
                filter = new FastWLS();
                break;
            case Kalman:
                filter = new Kalman();
                break;
            default:
                throw new UnsupportedFilterException();
        }

        if(filters.containsKey( statisticName) ){
            System.out.println( "Statistic " + statisticName + " added filter of type " + filter.getType() );
            filters.get( statisticName ).add( filter );
        }else{
            System.out.println( "Statistic " + statisticName + " created and filter of type " + filter.getType() + " added " );
            ArrayList<AdaptiveFilter<Long>> filterList = new ArrayList<AdaptiveFilter<Long>>();
            filterList.add( filter );
            filters.put( statisticName, filterList );
        }
    }

    @Override
    public boolean statisticHasFilter(String statisticName, AdaptiveFilter.FilterType type) {
        return getFilter( statisticName, type ) != null;
    }

    @Override
    public void removeFilter(String statisticName, AdaptiveFilter.FilterType type) {
        AdaptiveFilter<Long> filterToRemove = null;

        for ( AdaptiveFilter<Long> filter : filters.get( statisticName )){
            if( filter.getType() == type ){
                filterToRemove = filter;
                System.out.println( "Statistic " + statisticName + " removes filter of type " + filter.getType() );
            }
        }
        filters.get( statisticName ).remove( filterToRemove );

        if( filters.get( statisticName ).size() == 0){
            System.out.println( "Statistic " + statisticName + " has now no active filters." );
            filters.remove( statisticName );
        }
    }
}
