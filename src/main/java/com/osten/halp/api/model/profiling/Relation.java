package com.osten.halp.api.model.profiling;

import com.osten.halp.api.model.statistics.Statistic;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-11-06
 * Time: 10:05
 * To change this template use File | Settings | File Templates.
 */
public interface Relation {

    public void setType(Statistic.DataType type );
    public void setType( String type );
    public void setState( int state );
    public void setState( String state );
    public void setFilterType( AdaptiveFilter.FilterType filterType );
    public void setFilterType( String filterType );
    public void setDetectorType( ChangeDetector.DetectorType detectorType );
    public void setDetectorType( String detectorType );

    public Statistic.DataType getType();
    public int getState();
    public AdaptiveFilter.FilterType getFilter();
    public ChangeDetector.DetectorType getDetector();

}
