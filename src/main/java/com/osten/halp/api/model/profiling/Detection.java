package com.osten.halp.api.model.profiling;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-11-20
 * Time: 11:49
 * To change this template use File | Settings | File Templates.
 */
public class Detection<Data> {
    private Data discretePointInTime;
    private Data magnitude;

    public Detection( Data discretePointInTime, Data magnitude){
        this.discretePointInTime = discretePointInTime;
        this.magnitude = magnitude;
    }

    public Data getDiscretePointInTime() {
        return discretePointInTime;
    }

    public Data getMagnitude() {
        return magnitude;
    }
}
