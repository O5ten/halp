package com.osten.halp.impl.profiling.detector;

import com.osten.halp.api.model.profiling.AdaptiveFilter;
import com.osten.halp.api.model.profiling.ChangeDetector;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-11-26
 * Time: 15:11
 * To change this template use File | Settings | File Templates.
 */
public class Accumulation extends ChangeDetector<Long> {

    /**
     * By definition an accumulator cannot decrease, it accumulates.
     * I.E collects data, suggesting that each measurement should increase or linger but never decrease.
     * Changes in an accumulator are collected and compared to the filter estimates.
     *
     * If the filter has not accumulated for ROBUSTNESS_PROPERTY( int ) time units then a change will register at the time that it stopped accumulating.
     */
    public Accumulation(){
       setType( DetectorType.Accumulator );
    }

    @Override
    public void detect(AdaptiveFilter<Long> filter) {

    }
}
