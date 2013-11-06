package com.osten.halp.impl.shared;

import com.osten.halp.api.model.profiling.AdaptiveFilter;
import com.osten.halp.api.model.shared.FilterModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-11-05
 * Time: 16:20
 * To change this template use File | Settings | File Templates.
 */
public class LongFilterModel implements FilterModel<Long> {

    Map<String, List<AdaptiveFilter<Long>>> filters;

    public LongFilterModel(){
        filters = new HashMap<String, List<AdaptiveFilter<Long>>>();
    }

    @Override
    public Map<String, Long> adapt(String name, Long actualValue) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public List<Long> getEstimate(String name) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public AdaptiveFilter<Long> getFilter(String name) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public List<Long> estimate(String name) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
