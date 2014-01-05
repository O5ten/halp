package com.osten.halp.model.profiling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private DetectorType type;

    /**
     * The central method that each type of ChangeDetector-implementation MUST implement.
     * @param filter
     */
    public abstract void detect( AdaptiveFilter<Data> filter );

    protected void initialize( HashMap<String, Number> settings, DetectorType type ){
        this.type = type;
        detections = new ArrayList<Detection<Data>>();
        setSettings( settings );
    }

    public List<Detection<Data>> getDetections(){
        return detections;
    }

    public HashMap<String,Number> getSettings(){
        return settings;
    }

    public void setSettings( HashMap<String, Number> settings ){
        this.settings = settings;
    }

    public Number getSetting(String settingKey){
        return this.settings.get( settingKey );
    }

    public DetectorType getType(){
        return type;
    }

    public void setType(DetectorType type ){
        this.type = type;
    }

    public Enum[] getEnums(){
        return DetectorType.values();
    }

    public void printDetections(){
        System.out.println("Detection set: " + this.getType());
        System.out.print( "time: [from, to]-->{ Magnitude }\n[ " );
        for( Detection<Data> detection : detections){
            System.out.print( " time: [" + detection.getStart() + ", " + detection.getStop() + " ]-->{ " + detection.getMagnitude() + "} " );
        }
        System.out.println( " ]" );
    }

    public enum DetectorType{
        CUSUM, Passivity, Activity,
    }
}
