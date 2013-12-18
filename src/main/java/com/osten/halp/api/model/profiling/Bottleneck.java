package com.osten.halp.api.model.profiling;

import com.osten.halp.api.model.shared.ProfileModel;

/**
 * Created by osten on 12/10/13.
 */
public class Bottleneck
{
	private ProfileModel.Profile type;
	private String description;
	private double likeliness;

	/**
	 * A bottleneck, from A to B, discrete time. Contains relevantDetections on statistics.
	 */
	public Bottleneck( ProfileModel.Profile type, String description, double likeliness )
	{
		this.type = type;
		this.description = description;
		this.setLikeliness( likeliness );
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

	public double getLikeliness()
	{
		return likeliness;
	}

	public void setLikeliness( double likeliness )
	{
		this.likeliness = likeliness;
	}
}
