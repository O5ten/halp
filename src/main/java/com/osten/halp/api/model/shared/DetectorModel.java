package com.osten.halp.api.model.shared;

import com.osten.halp.api.model.profiling.AdaptiveFilter;
import com.osten.halp.api.model.profiling.ChangeDetector;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-11-20
 * Time: 19:07
 * To change this template use File | Settings | File Templates.
 */
public interface DetectorModel<Data> {

    /**
     *  Same model as FilterModel, But for detectors
     **/
    public void detect( String statisticName, AdaptiveFilter<Data> filter);

    public List<ChangeDetector<Data>> getDetectorsByStatistic( String name );

    public ChangeDetector<Data> getDetector( String statisticName, ChangeDetector.DetectorType type );

    public void resetModel();

    public void createDetector( String statisticName, ChangeDetector.DetectorType type );

    public void removeFilter( String statisticName, ChangeDetector.DetectorType type );

}
