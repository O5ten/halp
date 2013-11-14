package com.osten.halp.impl.profiling;

import com.osten.halp.api.model.profiling.AdaptiveFilter;
import com.osten.halp.api.model.statistics.Statistic;

import java.util.HashMap;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: osten
 * Date: 11/12/13
 * Time: 3:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class BasicRLSFilter extends AdaptiveFilter<Long>
{

	FilterType filtertype;

	public BasicRLSFilter( ){
		 this.filtertype = FilterType.BasicRLS;
	}

	@Override
	public void adapt( Statistic<Long> measurement )
	{
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public Statistic<Long> getEstimates()
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public Statistic<Long> getResiduals()
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

    @Override
    public HashMap<String, Number> getParameters() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void setParameters(HashMap<String, Number> properties) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

   	@Override
	public FilterType getType()
	{
		return filtertype;
	}

    @Override
    public void printAggregatedData() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
	public Enum[] getEnums()
	{
		return new Enum[0];  //To change body of implemented methods use File | Settings | File Templates.
	}
}
