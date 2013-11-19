package com.osten.halp.impl.profiling;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-11-19
 * Time: 15:53
 * To change this template use File | Settings | File Templates.
 */
public class LargeWindowWLSFilter extends BasicWLSFilter {

    private final static int LARGE_WINDOW_SIZE = 6;

    public LargeWindowWLSFilter(){
        HashMap<String, Number> settings = new HashMap<String, Number>();
        settings.put(WINDOW_SIZE_PROPERTY, LARGE_WINDOW_SIZE);
        initialize( settings, FilterType.LargeWindowWLS);
    }
}
