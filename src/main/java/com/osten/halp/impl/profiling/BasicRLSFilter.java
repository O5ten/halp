package com.osten.halp.impl.profiling;

import com.osten.halp.api.model.profiling.AdaptiveFilter;
import com.osten.halp.api.model.statistics.Statistic;

import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: osten
 * Date: 11/12/13
 * Time: 3:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class BasicRLSFilter implements AdaptiveFilter<Long>
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
	public Properties getParameters()
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void setParameters( Properties properties )
	{
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public FilterType getType()
	{
		return filtertype;
	}

	@Override
	public Enum[] getEnums()
	{
		return new Enum[0];  //To change body of implemented methods use File | Settings | File Templates.
	}
}
