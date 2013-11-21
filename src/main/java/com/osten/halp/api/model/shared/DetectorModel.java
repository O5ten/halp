package com.osten.halp.api.model.shared;

import com.osten.halp.api.model.profiling.AdaptiveFilter;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-11-20
 * Time: 19:07
 * To change this template use File | Settings | File Templates.
 */
public interface DetectorModel<Data> {
    public void detect( String statisticName, AdaptiveFilter<Data> filter);
}
