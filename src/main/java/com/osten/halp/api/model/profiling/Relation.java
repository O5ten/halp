package com.osten.halp.api.model.profiling;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-11-06
 * Time: 10:05
 * To change this template use File | Settings | File Templates.
 */
public interface Relation {

    public void setRelation(String a, String b, Operation type);
    public void setA( String a );
    public void setB( String b );
    public String getA();
    public String getB();
    public Operation getOperation();
    public void setOperation();

    public enum Operation{
         AND, OR, XOR, NOR, XNOR, NAND, NOOP
    }
}
