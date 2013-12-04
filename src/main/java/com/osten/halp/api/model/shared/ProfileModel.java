package com.osten.halp.api.model.shared;

import com.osten.halp.api.model.profiling.AdaptiveFilter;
import com.osten.halp.api.model.profiling.Relation;
import com.osten.halp.api.model.statistics.Statistic;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-11-05
 * Time: 14:12
 * To change this template use File | Settings | File Templates.
 */
public interface ProfileModel<Data> {

    /**
     * This model governs the consequent actions when having selected a profile other than customized
     * It goes to work when selecting a data-type fora statistic. It checks for other datatypes and sees if any connection related to this
     * specific profile can be done.
     */

    public List<Relation> getRelations();

    /**
     * Using
     * @param dataType
     * @param profile What profile is currently selected by the user, this has some effect on what kind of filter is suggested for a dataType.
     * @return
     */
    public AdaptiveFilter<Data> getFilterByProfile( ProfileModel.Profile profile, Statistic.DataType dataType);

	 public void resetModel();

    public static enum Profile {
        Custom, CPU, Memory, Network, ALL
    }
}
