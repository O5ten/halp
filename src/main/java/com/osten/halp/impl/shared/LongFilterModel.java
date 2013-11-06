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
    public Map<AdaptiveFilter.Type, Long> adapt(String name, Long actualValue) {

        Map<AdaptiveFilter.Type, Long> residues = new HashMap<AdaptiveFilter.Type, Long>();

        for (AdaptiveFilter<Long> filter : filters.get(name)) {
            residues.put(filter.getType(), filter.adapt(actualValue));
        }

        return residues;
    }

    @Override
    public Map<AdaptiveFilter.Type, Long> getEstimations(String name) {
        Map<AdaptiveFilter.Type, Long> residuals = new HashMap<AdaptiveFilter.Type, Long>();

        for (AdaptiveFilter<Long> filter : filters.get(name)) {
            residuals.put(filter.getType(), filter.estimate());
        }

        return residuals;
    }

    @Override
    public AdaptiveFilter<Long> getFilter(String statisticName, AdaptiveFilter.Type filterType) {
        AdaptiveFilter<Long> filter;
        switch (filterType) {
            case LMS:
                filter = new BasicLMSFilter();
                break;
            default:
                throw new UnsupportedFilterException();
        }
        if(filters.containsKey( statisticName) ){
            filters.get( statisticName ).add( filter );
        }else{
            ArrayList<AdaptiveFilter<Long>> filterList = new ArrayList<AdaptiveFilter<Long>>();
            filterList.add( filter );
            filters.put( statisticName, filterList );
        }

        return filter;
    }

    @Override
    public List<AdaptiveFilter<Long>> getFilters(String statisticName) {
        return Collections.unmodifiableList(filters.get(statisticName));
    }


    @Override
    public Map<AdaptiveFilter.Type, Long> estimate(String name) {
        Map<AdaptiveFilter.Type, Long> estimates = new HashMap<AdaptiveFilter.Type, Long>();

        for (AdaptiveFilter<Long> filter : filters.get( name )){
            estimates.put( filter.getType(), filter.estimate() );
        }

        return estimates;
    }
}
