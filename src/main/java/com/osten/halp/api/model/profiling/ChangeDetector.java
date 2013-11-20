package com.osten.halp.api.model.profiling;

import com.osten.halp.api.model.statistics.Statistic;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-10-21
 * Time: 13:52
 * To change this template use File | Settings | File Templates.
 */
public abstract class ChangeDetector<Data> implements Enumerable{

    private List<Detection<Data>> detections;
    private HashMap<String, Number> settings;

    /**
     * The central method that each type of ChangeDetector-implementation MUST implement.
     * @param filter
     */
    public abstract void detect( AdaptiveFilter<Data> filter );

    public List<Detection<Data>> getDetections(){
        return detections;
    }

    public HashMap<String,Number> getSettings(){
        return settings;
    }

    public void setSettings( HashMap<String, Number> settings ){
        this.settings = settings;
    }

    public Enum[] getEnums(){
        return DetectorType.values();
    }

    private enum DetectorType{
        GLR, CUSUM, SPRT,
    }


}
