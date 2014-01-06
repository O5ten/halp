package com.osten.halp.impl.profiling.detector;

import com.osten.halp.model.profiling.AdaptiveFilter;
import com.osten.halp.model.profiling.ChangeDetector;
import com.osten.halp.model.profiling.Detection;
import com.osten.halp.model.statistics.DataPoint;
import com.osten.halp.model.statistics.Statistic;

import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-11-26
 * Time: 15:11
 * To change this template use File | Settings | File Templates.
 */
public class Passivity extends ChangeDetector<Long> {

    /**
     * How many measurements is it allowed to not change.
     */
    public static String ROBUSTNESS_PROPERTY = "ROBUSTNESS";
    public static String THRESHOLD_PROPERTY = "THRESHOLD";
	public static String LOWER_LIMIT_PROPERTY = "LIMIT";

    private Integer DEFAULT_ROBUSTNESS = 10;
    private Integer DEFAULT_THRESHOLD = 10;
	 private Integer DEFAULT_LOWERLIMIT = 20;
    /**
     * The no-activity-detector.
      ***/
    public Passivity(){
        HashMap<String, Number> settings = new HashMap<String, Number>();
        settings.put(ROBUSTNESS_PROPERTY, DEFAULT_ROBUSTNESS);
        settings.put(THRESHOLD_PROPERTY, DEFAULT_THRESHOLD);
		  settings.put(LOWER_LIMIT_PROPERTY, DEFAULT_LOWERLIMIT);
        initialize( settings, DetectorType.Passivity );
    }

    public Passivity(HashMap<String, Number> settings){
        initialize(settings, DetectorType.Passivity);
    }


    @Override
    public void detect(AdaptiveFilter<Long> filter) {

       int robustness = getSetting( ROBUSTNESS_PROPERTY ).intValue();
       int threshold = getSetting( THRESHOLD_PROPERTY ).intValue();
		 int limit = getSetting( LOWER_LIMIT_PROPERTY).intValue();

       Statistic<Long> residuals = filter.getResiduals();
       List<Detection<Long>> detections = getDetections();

       for( int i = 0; i < residuals.size(); i++ ){

           long residual = residuals.getDataByIndex(i).getValue();

           Detection<Long> detection = null;
           if(detections.size() > 0){
               detection = detections.get( detections.size()-1 );
           }
			  long v = filter.getMeasurements().getDataAsList().get( i ).getValue();
           if( residual < threshold &&  v > limit ){
               robustness--;
               if( detection == null || robustness == 0 ){
                   if( ( detection == null || detection.hasStop() ) ) {
                       detections.add(new Detection<Long>( new Long( i ), residual) );
                   }else if( detection.hasStop() ){
                       detections.add(new Detection<Long>( new Long( i - getSetting(ROBUSTNESS_PROPERTY).intValue() ), residual) );
                   }
                   else{
                       robustness++;
                   }
               }else if(i == residuals.size()-1 && !detection.hasStop()){
						detection.setStop( new Long( i ) );
					}
           }else{
               if(detection != null && !detection.hasStop()){
                   detection.setStop(new Long(i));
               }
               robustness = getSetting( ROBUSTNESS_PROPERTY ).intValue();
           }
       }

    }
}
