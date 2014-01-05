package com.osten.halp.impl.profiling.detector;

import com.osten.halp.model.profiling.AdaptiveFilter;
import com.osten.halp.model.profiling.ChangeDetector;
import com.osten.halp.model.profiling.Detection;

import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-11-26
 * Time: 15:19
 * To change this template use File | Settings | File Templates.
 */
public class Activity extends ChangeDetector<Long> {

    public String THRESHOLD_PROPERTY = "THRESHOLD";
    public Integer THRESHOLD_DEFAULT_VALUE = 3;

    /**
     * Practially an inverse of the Passivity. This means that the Statistic should stay passive in the best of cases.
     * If filter has Zero then preferably this detector should not detect anything.
     * Imagine, discarded messages, errors, queues, running threads and so on.
     */
    public Activity(){
        HashMap<String, Number> settings = new HashMap<String, Number>();
        settings.put( THRESHOLD_PROPERTY, THRESHOLD_DEFAULT_VALUE);
        initialize(settings, DetectorType.Activity);
    }

    public Activity(HashMap<String, Number> settings){
        initialize(settings, DetectorType.Activity);
    }

    @Override
    public void detect(AdaptiveFilter<Long> filter) {

        int threshold = getSetting( THRESHOLD_PROPERTY ).intValue();
        List<Detection<Long>> detections = getDetections();

        for( int i = 0; i < filter.getResiduals().size(); i++ ){
            Detection<Long> lastDetection = null;

            if( detections.size() > 0){
                lastDetection = detections.get( detections.size()-1);
            }
            long residual = filter.getResiduals().getDataByIndex( i ).getValue();

            if ( residual > threshold ){
                if( lastDetection == null || lastDetection.hasStop() ){
                    detections.add( new Detection<Long>( new Long( i ), residual ) );
                }else{
                    lastDetection.touch( new Long( i ) );
                }
            }else{
                if(lastDetection != null && !lastDetection.hasStop()){
                    lastDetection.setStop( new Long( i ));
                }
            }
        }
        Detection<Long> detection = getDetections().get(getDetections().size()-1);
        if(!detection.hasStop()){
            detection.setStop(new Long( filter.getResiduals().size() ) );
        }

    }
}
