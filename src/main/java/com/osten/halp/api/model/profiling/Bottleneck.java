package com.osten.halp.api.model.profiling;

import com.osten.halp.api.model.shared.ProfileModel;
import com.osten.halp.api.model.statistics.Statistic;

import java.util.List;

/**
 * Created by osten on 12/10/13.
 */
public class Bottleneck
{
	private ProfileModel.Profile type;
	private String description;

	/**
	 * A bottleneck, from A to B, discrete time. Contains relevantDetections on statistics.
	 */
	public Bottleneck(){

   }

	public ProfileModel.Profile getType()
	{
		return type;
	}

	public void setType( ProfileModel.Profile type )
	{
		this.type = type;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription( String description )
	{
		this.description = description;
	}
}
