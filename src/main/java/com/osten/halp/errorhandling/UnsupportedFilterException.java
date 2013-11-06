package com.osten.halp.errorhandling;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-11-06
 * Time: 15:43
 * To change this template use File | Settings | File Templates.
 */
public class UnsupportedFilterException extends UnsupportedOperationException {

    public UnsupportedFilterException(){
        super( "This filter is not supported yet, make sure that the filtermodel can handle it." );
    }

}
