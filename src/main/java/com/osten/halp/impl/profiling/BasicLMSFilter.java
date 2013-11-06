package com.osten.halp.impl.profiling;

import com.osten.halp.api.model.profiling.AdaptiveFilter;

import java.util.List;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-11-06
 * Time: 11:33
 * To change this template use File | Settings | File Templates.
 */
public class BasicLMSFilter implements AdaptiveFilter<Long> {

    private Type filterType;

    public BasicLMSFilter(){
        this.filterType = Type.LMS;
    }

    @Override
    public Long adapt(Long measurement) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Long estimate() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public List<Long> getAllEstimates() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public List<Long> getAllResiduals() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Properties getParameters() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void setParameters(Properties properties) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Type getType() {
       return filterType;
    }
}
