package com.osten.halp.impl.profiling;

import com.osten.halp.api.model.profiling.Relation;

/**
 *
 * User: server
 * Date: 2013-11-06
 * Time: 11:49
 * Bean to keep track of relations used closely with profiling.
 */
public class StatisticRelation implements Relation{


    @Override
    public void setRelation(String a, String b, Operation type) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void setA(String a) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void setB(String b) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getA() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getB() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Operation getOperation() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void setOperation() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
