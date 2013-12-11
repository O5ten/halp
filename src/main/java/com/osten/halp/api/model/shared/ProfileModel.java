package com.osten.halp.api.model.shared;

import com.osten.halp.api.model.profiling.*;
import com.osten.halp.api.model.statistics.Statistic;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-11-05
 * Time: 14:12
 * To change this template use File | Settings | File Templates.
 */
public interface ProfileModel<Data>
{

	/**
	 * This model governs the consequent actions when having selected a profile other than customized
	 * It goes to work when selecting a data-type fora statistic. It checks for other datatypes and sees if any connection related to this
	 * specific profile can be done.
	 */
	public List<Relation> getRelations();

	/**
	 * Using
	 *
	 * @param dataType
	 * @return
	 */

	public AdaptiveFilter.FilterType getFilterByDataType( Statistic.DataType dataType );

	/*
	 * Fetch description by profile
	 */
	public String getDescriptionByProfile( Profile profile );


	/**
	 * Fetch Description for selectedProfile
	 *
	 * @return
	 */
	public String getDescription();


	public void selectProfile( Profile profile );

	public Profile getSelectedProfile();

	public PointsOfInterest getPointsOfInterests( Map<Statistic<Long>, List<Detection<Data>>> detectionsByStatistic );

	public void resetModel();

	public void buildProfiles();

	public static enum Profile
	{
		Custom, CPU, Memory, Network, ALL
	}
}
