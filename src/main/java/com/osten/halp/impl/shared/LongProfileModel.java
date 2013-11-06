package com.osten.halp.impl.shared;

import com.osten.halp.api.model.profiling.AdaptiveFilter;
import com.osten.halp.api.model.profiling.Relation;
import com.osten.halp.api.model.shared.ProfileModel;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-11-05
 * Time: 16:20
 * To change this template use File | Settings | File Templates.
 */
public class LongProfileModel implements ProfileModel<Long> {


    public LongProfileModel( ){

    }

    @Override
    public List<Relation> getRelations() {
        throw new UnsupportedOperationException( "Not yet implemented");
    }

    @Override
    public AdaptiveFilter<Long> getFilterByProfile() {
        throw new UnsupportedOperationException( "Not yet implemented");
    }
}
