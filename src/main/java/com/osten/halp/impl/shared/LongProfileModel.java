package com.osten.halp.impl.shared;

import com.osten.halp.api.model.profiling.AdaptiveFilter;
import com.osten.halp.api.model.profiling.Relation;
import com.osten.halp.api.model.shared.ProfileModel;
import com.osten.halp.api.model.statistics.Statistic;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-11-05
 * Time: 16:20
 * To change this template use File | Settings | File Templates.
 */
public class LongProfileModel implements ProfileModel<Long> {

    @Override
    public List<Relation> getRelations() {
        throw new UnsupportedOperationException( "Not yet implemented");
    }

    @Override
    public AdaptiveFilter<Long> getFilterByProfile(Profile profile, Statistic.DataType dataType) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

	@Override
	public void resetModel()
	{
		//throw new UnsupportedOperationException("Not yet implemented");
		//Nothing to reset, yet.
	}
}
