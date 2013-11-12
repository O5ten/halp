package com.osten.halp.api.model.statistics;

/**
 * Created with IntelliJ IDEA.
 * User: osten
 * Date: 10/28/13
 * Time: 1:48 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DataPoint<T>
{
	public T getData();
	public void setData( T data );
}

