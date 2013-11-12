package com.osten.halp.impl.shared;

import com.osten.halp.api.model.profiling.AdaptiveFilter;
import com.osten.halp.api.model.profiling.ChangeDetector;
import com.osten.halp.api.model.profiling.StopRule;
import com.osten.halp.api.model.shared.FilterModel;
import com.osten.halp.api.model.statistics.Statistic;
import com.osten.halp.errorhandling.UnsupportedFilterException;
import com.osten.halp.impl.profiling.BasicLMSFilter;
import com.osten.halp.impl.profiling.BasicRLSFilter;
import com.osten.halp.impl.profiling.BasicWLSFilter;

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
    Map<String, List<StopRule<Long>>> rules;
    Map<String, List<ChangeDetector<Long>>> detectors;

    public LongFilterModel() {
        filters = new HashMap<String, List<AdaptiveFilter<Long>>>();
    }

    @Override
    public void adapt(String name, Statistic<Long> measurements) {
        for (AdaptiveFilter<Long> filter : filters.get(name)) {
           filter.adapt( measurements );
        }
    }

    @Override
    public AdaptiveFilter<Long> getFilter(String statisticName, AdaptiveFilter.FilterType filterType) {
        AdaptiveFilter<Long> foundFilter = null;
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
		 		return Collections.unmodifiableList( filters.get(statisticName) );
	 	 }else{
			 	return Collections.unmodifiableList( new ArrayList<AdaptiveFilter<Long>>() );
		 }
    }

    @Override
    public void createFilter(String statisticName, AdaptiveFilter.FilterType filterType) {
        AdaptiveFilter<Long> filter;

        switch (filterType) {
            case BasicWLS:
                filter = new BasicWLSFilter();
                break;
            case BasicLMS:
                filter = new BasicLMSFilter();
                break;
				case BasicRLS:
					 filter = new BasicRLSFilter();
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
