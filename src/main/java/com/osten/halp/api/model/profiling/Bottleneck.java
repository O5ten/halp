package com.osten.halp.api.model.profiling;

import com.osten.halp.api.model.shared.ProfileModel;
import com.osten.halp.api.model.statistics.Statistic;

import java.util.List;

/**
 * Created by osten on 12/10/13.
 */
public class Bottleneck
{
	private List<Statistic> relevantStatistics;
	private List<Detection> relevantDetections;
	private int startTime;
	private int stopTime;
	private ProfileModel.Profile type;
	private String description;

	/**
	 * A bottleneck, from A to B, discrete time. Contains relevantDetections on statistics.
	 */
	public Bottleneck(){

	}

	/**
	 * Detections used to detect this bottleneck.
	 * @return
	 */
	public List<Detection> getRelevantDetections()
	{
		return relevantDetections;
	}

	/**
	 * Change the relevant detections for this bottleneck.
	 * @param relevantDetections
	 */
	public void setRelevantDetections( List<Detection> relevantDetections )
	{
		this.relevantDetections = relevantDetections;
	}

	public int getStartTime()
	{
		return startTime;
	}

	public void setStartTime( int startTime )
	{
		this.startTime = startTime;
	}

	public int getStopTime()
	{
		return stopTime;
	}

	public void setStopTime( int stopTime )
	{
		this.stopTime = stopTime;
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

	public List<Statistic> getRelevantStatistics()
	{
		return relevantStatistics;
	}

	public void setRelevantStatistics( List<Statistic> relevantStatistics )
	{
		this.relevantStatistics = relevantStatistics;
	}
}
