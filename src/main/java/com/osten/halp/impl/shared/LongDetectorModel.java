package com.osten.halp.impl.shared;

import com.osten.halp.api.model.profiling.AdaptiveFilter;
import com.osten.halp.api.model.profiling.ChangeDetector;
import com.osten.halp.api.model.shared.DetectorModel;
import com.osten.halp.errorhandling.UnsupportedFilterException;
import com.osten.halp.impl.profiling.detector.Accumulation;
import com.osten.halp.impl.profiling.detector.Cusum;
import com.osten.halp.impl.profiling.detector.Sprt;
import com.osten.halp.impl.profiling.detector.Zero;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-11-21
 * Time: 08:53
 * To change this template use File | Settings | File Templates.
 */
public class LongDetectorModel implements DetectorModel<Long> {

    Map<String, List<ChangeDetector<Long>>> detectors;

    public LongDetectorModel() {
        detectors = new HashMap<String, List<ChangeDetector<Long>>>();
    }

    @Override
    public void detect(String statisticName, AdaptiveFilter<Long> filter) {
        for (ChangeDetector<Long> detector : detectors.get(statisticName)) {
            detector.detect(filter);
        }
    }

    @Override
    public List<ChangeDetector<Long>> getDetectorsByStatisticName(String statisticName) {
        List<ChangeDetector<Long>> filterList = detectors.get(statisticName);
        if (filterList != null) {
            return detectors.get(statisticName);
        } else {
            return new ArrayList<ChangeDetector<Long>>();
        }
    }

    @Override
    public ChangeDetector<Long> getDetector(String statisticName, ChangeDetector.DetectorType type) {
        ChangeDetector<Long> foundDetector = null;
        if (detectors.get(statisticName) == null) {
            return foundDetector;
        }
        for (ChangeDetector<Long> detector : detectors.get(statisticName)) {
            if (detector.getType() == type) {
                foundDetector = detector;
                break;
            }
        }
        return foundDetector;
    }

    @Override
    public void resetModel() {
        detectors.clear();
    }

    @Override
    public void createDetector(String statisticName, ChangeDetector.DetectorType detectorType) {

        ChangeDetector<Long> detector;

        switch (detectorType) {
            case CUSUM:
                detector = new Cusum();
                break;
            case SPRT:
                detector = new Sprt();
                break;
            case Accumulator:
                detector = new Accumulation();
                break;
            default:
                throw new UnsupportedFilterException();
        }

        if (detectors.containsKey(statisticName)) {
            System.out.println("Statistic " + statisticName + " added Change Detector of type " + detector.getType());
            detectors.get(statisticName).add(detector);
        } else {
            System.out.println("Statistic " + statisticName + " created and Change Detector of type " + detector.getType() + " added ");
            ArrayList<ChangeDetector<Long>> detectorList = new ArrayList<ChangeDetector<Long>>();
            detectorList.add(detector);
            detectors.put(statisticName, detectorList);
        }
    }

    @Override
    public void removeDetector(String statisticName, ChangeDetector.DetectorType detectorType) {
        ChangeDetector<Long> filterToRemove = null;

        for (ChangeDetector<Long> detector : detectors.get(statisticName)) {
            if (detector.getType() == detectorType) {
                filterToRemove = detector;
                System.out.println("Statistic " + statisticName + " removes filter of type " + detector.getType());
            }
        }
        detectors.get(statisticName).remove(filterToRemove);

        if (detectors.get(statisticName).size() == 0) {
            System.out.println("Statistic " + statisticName + " has now no active filters.");
            detectors.remove(statisticName);
        }
    }
}
