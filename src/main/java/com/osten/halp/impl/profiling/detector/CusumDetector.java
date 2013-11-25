package com.osten.halp.impl.profiling.detector;

import com.osten.halp.api.model.profiling.AdaptiveFilter;
import com.osten.halp.api.model.profiling.ChangeDetector;
import com.osten.halp.api.model.profiling.Detection;
import com.osten.halp.api.model.statistics.DataPoint;
import com.osten.halp.api.model.statistics.Statistic;

import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-11-25
 * Time: 15:28
 * To change this template use File | Settings | File Templates.
 */
public class CusumDetector extends ChangeDetector<Long> {

    private final static int DEFAULT_THRESHOLD = 5;
    private final static double DEFAULT_DRIFT = 0.5;
    private final static int DEFAULT_ROBUSTNESS = 2;

    public final static String ROBUSTNESS_PROPERTY = "ROBUSTNESS";
    public final static String DRIFT_PROPERTY = "DRIFT";
    public final static String THRESHOLD_PROPERTY = "THRESHOLD";

    public CusumDetector() {

        HashMap<String, Number> settings = new HashMap<String, Number>();
        settings.put(ROBUSTNESS_PROPERTY, DEFAULT_ROBUSTNESS);
        settings.put(DRIFT_PROPERTY, DEFAULT_DRIFT);
        settings.put(THRESHOLD_PROPERTY, DEFAULT_THRESHOLD);
        initialize( settings );
    }

    public CusumDetector(HashMap<String, Number> settings) {
        initialize( settings );
    }

    @Override
    public void detect(AdaptiveFilter<Long> filter) {

        //Preamble
        int threshold = getSetting( THRESHOLD_PROPERTY ).intValue();
        double drift = getSetting( DRIFT_PROPERTY ).doubleValue();
        int robustness = getSetting( ROBUSTNESS_PROPERTY ).intValue();

        double negativeCusum = 0;
        double positiveCusum = 0;

        List<DataPoint<Long>> residuals = filter.getResiduals().getData();
        List<DataPoint<Long>> variances = filter.getVariance().getData();

        for(int i = 0; i < residuals.size(); i++){

            //Get residual
            double residual = residuals.get( i ).getData();
            double variance = variances.get( i ).getData();

            //normalize residual, but avoid division by zero. That's bad.
            if(variance != 0){
                residual = Math.pow(residual, 2 ) / Math.pow( variance, 2 );
            }else{
                residual = Math.pow(residual, 2);
            }

            //CUSUM algorithm performed on residuals
            positiveCusum = Math.max( 0, positiveCusum + drift + residual );
            negativeCusum = Math.max( 0, Math.abs(negativeCusum + drift - residual ) );/* Abs might not be needed -->*/

            if( positiveCusum > threshold || negativeCusum > threshold ){

                robustness--;
                if(robustness == 0){

                    getDetections().add( new Detection<Long>( new Long( i ) , Math.round( Math.max( positiveCusum, negativeCusum) ) ) );

                    positiveCusum = 0;
                    negativeCusum = 0;

                    robustness = getSetting( ROBUSTNESS_PROPERTY ).intValue();
                }
            }else{
                robustness = getSetting( ROBUSTNESS_PROPERTY ).intValue();
            }
        }
    }
}
