package com.osten.halp.impl.shared;

import com.osten.halp.api.model.profiling.AdaptiveFilter;
import com.osten.halp.api.model.profiling.ChangeDetector;
import com.osten.halp.api.model.profiling.Relation;
import com.osten.halp.api.model.statistics.Statistic;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-12-07
 * Time: 00:09
 * To change this template use File | Settings | File Templates.
 */
public class QuantumRelation implements Relation {

    public final static int FALSE = -1;
    public final static int TRUE = 1;
    public final static int SUPER = 0;

    private int state;
    private Statistic.DataType dataType;
    private ChangeDetector.DetectorType detectorType;
    private AdaptiveFilter.FilterType filterType;

    public QuantumRelation(Statistic.DataType type, int state, AdaptiveFilter.FilterType filterType, ChangeDetector.DetectorType detectorType) {
        this.state = state;
        this.dataType = type;
        this.filterType = filterType;
        this.detectorType = detectorType;
    }

    public QuantumRelation() {}

    @Override
    public Statistic.DataType getType() {
        return dataType;
    }

    @Override
    public void setType(Statistic.DataType type) {
        this.dataType = type;
    }

    public void setType(String dataType) {

        if (dataType.equalsIgnoreCase(Statistic.DataType.Memory.toString())) {

            this.dataType = Statistic.DataType.Memory;

        } else if (dataType.equalsIgnoreCase(Statistic.DataType.CPU.toString())) {

            this.dataType = Statistic.DataType.CPU;

        } else if (dataType.equalsIgnoreCase(Statistic.DataType.ResponseTime.toString())) {

            this.dataType = Statistic.DataType.ResponseTime;

        } else if (dataType.equalsIgnoreCase(Statistic.DataType.Swap.toString())) {

            this.dataType = Statistic.DataType.Swap;

        } else if (dataType.equalsIgnoreCase(Statistic.DataType.Throughput.toString())) {

            this.dataType = Statistic.DataType.Throughput;

        } else {

            System.out.println( "Tried to parse " + dataType + " into a DataType. but Failed. ");
            throw new UnsupportedOperationException();

        }
    }

    @Override
    public int getState() {
        return state;
    }

    @Override
    public void setState(int state) {
        this.state = state;
    }

    @Override
    public void setState(String state) {
        if( state.equalsIgnoreCase("true") ){
          this.state = 1;
        }else if(state.equalsIgnoreCase("false")){
          this.state = -1;
        }else{
          this.state = 0;
        }
    }

    @Override
    public AdaptiveFilter.FilterType getFilter() {
        return filterType;
    }

    @Override
    public void setFilterType(AdaptiveFilter.FilterType filterType) {
        this.filterType = filterType;
    }

    public void setFilterType(String filterType) {

        if (filterType.equalsIgnoreCase(AdaptiveFilter.FilterType.Kalman.toString())) {

            this.filterType = AdaptiveFilter.FilterType.Kalman;

        } else if (filterType.equalsIgnoreCase(AdaptiveFilter.FilterType.BaseWLS.toString())) {

            this.filterType = AdaptiveFilter.FilterType.BaseWLS;

        } else if (filterType.equalsIgnoreCase(AdaptiveFilter.FilterType.SlowWLS.toString())) {

            this.filterType = AdaptiveFilter.FilterType.SlowWLS;

        } else if (filterType.equalsIgnoreCase(AdaptiveFilter.FilterType.FastWLS.toString())) {

            this.filterType = AdaptiveFilter.FilterType.FastWLS;

        } else {

            throw new UnsupportedOperationException();

        }
    }

    @Override
    public ChangeDetector.DetectorType getDetector() {
        return detectorType;
    }

    @Override
    public void setDetectorType(ChangeDetector.DetectorType detectorType) {
       this.detectorType = detectorType;
    }

    public void setDetectorType( String detectorType ){

        if (detectorType.equalsIgnoreCase( ChangeDetector.DetectorType.CUSUM.toString() ) ) {

            this.detectorType = ChangeDetector.DetectorType.CUSUM;

        } else if (detectorType.equalsIgnoreCase( ChangeDetector.DetectorType.Activity.toString())) {

            this.detectorType = ChangeDetector.DetectorType.Activity;

        } else if (detectorType.equalsIgnoreCase( ChangeDetector.DetectorType.Passivity.toString())) {

            this.detectorType = ChangeDetector.DetectorType.Passivity;

        } else {
            throw new UnsupportedOperationException();
        }
    }
}
