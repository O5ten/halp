package com.osten.halp.impl.shared;

import com.osten.halp.api.model.profiling.AdaptiveFilter;
import com.osten.halp.api.model.profiling.ChangeDetector;
import com.osten.halp.api.model.profiling.StopRule;
import com.osten.halp.api.model.shared.FilterModel;
import com.osten.halp.errorhandling.UnsupportedFilterException;
import com.osten.halp.impl.profiling.BasicLMSFilter;

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
    public Map<AdaptiveFilter.FilterType, Long> adapt(String name, Long actualValue) {

        Map<AdaptiveFilter.FilterType, Long> residues = new HashMap<AdaptiveFilter.FilterType, Long>();

        for (AdaptiveFilter<Long> filter : filters.get(name)) {
            residues.put(filter.getType(), filter.adapt(actualValue));
        }

        return residues;
    }

    @Override
    public Map<AdaptiveFilter.FilterType, Long> getEstimations(String name) {
        Map<AdaptiveFilter.FilterType, Long> residuals = new HashMap<AdaptiveFilter.FilterType, Long>();

        for (AdaptiveFilter<Long> filter : filters.get(name)) {
            residuals.put(filter.getType(), filter.estimate());
        }

        return residuals;
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
    public List<AdaptiveFilter<Long>> getFilters(String statisticName) {
        return Collections.unmodifiableList(filters.get(statisticName));
    }

    @Override
    public void createFilter(String statisticName, AdaptiveFilter.FilterType filterType) {
        AdaptiveFilter<Long> filter;

        switch (filterType) {
            case LMS:
                filter = new BasicLMSFilter();
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


    @Override
    public synchronized Map<AdaptiveFilter.FilterType, Long> estimate(String name) {
        Map<AdaptiveFilter.FilterType, Long> estimates = new HashMap<AdaptiveFilter.FilterType, Long>();

        for (AdaptiveFilter<Long> filter : filters.get( name )){
            estimates.put( filter.getType(), filter.estimate() );
        }

        return estimates;
    }
}
