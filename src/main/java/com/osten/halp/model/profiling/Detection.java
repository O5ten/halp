package com.osten.halp.model.profiling;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-11-20
 * Time: 11:49
 * To change this template use File | Settings | File Templates.
 */
public class Detection<Data> {

    private Data start;
    private Data stop;
    private Data magnitude;
    private Data lastTouched;

    public Detection( Data start, Data magnitude){
        this.start = start;
        this.lastTouched = start;
        this.magnitude = magnitude;
        this.stop = start;
    }

    public Data getStart() {
        return start;
    }

    public Data getTouched(){
        return lastTouched;
    }

    public void touch( Data time ){
        this.lastTouched = time;
    }

    public Data getStop(){
        return stop;
    }

    public boolean hasStop(){
        return start != stop;
    }

    public void setStop( Data stop ){
        this.stop = stop;
    }

    public Data getMagnitude() {
        return magnitude;
    }
}
