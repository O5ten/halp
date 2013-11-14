package com.osten.halp.utils;

import javafx.beans.property.Property;

/**
 * Created with IntelliJ IDEA.
 * User: osten
 * Date: 11/12/13
 * Time: 9:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class PropUtils
{
	public static Long toLong( String property ){
		return Long.valueOf( property );
	}

	public static Integer toInt( String property ){
		return Integer.valueOf( property );
	}
}
