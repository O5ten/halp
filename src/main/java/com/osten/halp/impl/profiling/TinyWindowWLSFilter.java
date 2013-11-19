package com.osten.halp.impl.profiling;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-11-19
 * Time: 16:01
 * To change this template use File | Settings | File Templates.
 */
public class TinyWindowWLSFilter extends BasicWLSFilter {

    private final static int TINY_WINDOW_SIZE = 2;

    public TinyWindowWLSFilter(){
        HashMap<String, Number> settings = new HashMap<String, Number>();
        settings.put(WINDOW_SIZE_PROPERTY, TINY_WINDOW_SIZE);
        initialize( settings, FilterType.TinyWindowWLS);
    }
}
