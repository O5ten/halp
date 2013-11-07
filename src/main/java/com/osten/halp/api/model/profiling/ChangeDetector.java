package com.osten.halp.api.model.profiling;

import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-10-21
 * Time: 13:52
 * To change this template use File | Settings | File Templates.
 */
public interface ChangeDetector<Data> extends Enumerable{
    public boolean detect();
    public void extractParameters( Properties properties );
    public void applyParameters( Properties properties );
}
