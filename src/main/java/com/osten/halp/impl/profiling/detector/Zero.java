package com.osten.halp.impl.profiling.detector;

import com.osten.halp.api.model.profiling.AdaptiveFilter;
import com.osten.halp.api.model.profiling.ChangeDetector;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-11-26
 * Time: 15:19
 * To change this template use File | Settings | File Templates.
 */
public class Zero extends ChangeDetector<Long> {

    /**
     * Practially an inverse of the Accumulation. This means that the Statistic should stay Zero in the best of cases.
     * Imagine, discarded messages, errors, queues, running threads and so on.
     */
    public Zero(){

    }

    @Override
    public void detect(AdaptiveFilter<Long> filter) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
