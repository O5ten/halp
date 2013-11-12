package com.osten.halp.impl.profiling;

import com.osten.halp.api.model.profiling.AdaptiveFilter;
import com.osten.halp.api.model.statistics.DataPoint;
import com.osten.halp.api.model.statistics.Statistic;
import com.osten.halp.impl.statistics.DoubleDataPoint;
import com.osten.halp.impl.statistics.LongDataPoint;
import com.osten.halp.impl.statistics.LongStatistic;
import com.osten.halp.utils.PropUtils;

import java.util.ArrayDeque;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-11-08
 * Time: 14:03
 * To change this template use File | Settings | File Templates.
 */
public class BasicWLSFilter implements AdaptiveFilter<Long>
{
	/**
	 * DEFAULTS
	 */
	private final int DEFAULT_WINDOW_SIZE = 5;

	/*******************************
	 * PROPERTIES OF THIS FILTER
	 *******************************/

	/**
	 * The N most recent samples will be used to calculate the estimation.
	 */
	public static final String WINDOW_SIZE_PROPERTY = "WINDOW_SIZE_PROPERTY";

	/**
	 * The N most recent samples are weighted depending on time away from estimation.
	 * By default its an inverse logarithmic function with a sum resulting in 1.
	 * The Relevance_factor is the level of decline of that logarithmic function as of
	 * Wk: [ 1/(2*F) ] where F is the distance from estimation position k for each value
	 * up to at the most the length of RECENT_SAMPLES.
	 */
	private Statistic<Long> signal_estimates;     /* theta_hat of t */
	private Statistic<Long> residuals;            /* */
	private Statistic<Long> signal_measurements;
	private Statistic<Double> signal_variance;

	private FilterType filterType;

	/**
	 * Settings of this filter are the following:
	 * Relevant samples = Weights of latest
	 */
	private Properties settings;

	public BasicWLSFilter()
	{
		this.filterType = FilterType.BasicWLS;
		this.settings = new Properties();
		this.settings.put( WINDOW_SIZE_PROPERTY, DEFAULT_WINDOW_SIZE );
	}

	public BasicWLSFilter( Properties settings )
	{
		this.filterType = FilterType.BasicLMS;
		this.settings = settings;
	}

	@Override
	public void adapt( Statistic<Long> measurements )
	{
		this.signal_measurements = measurements.copyOf();

		performAlgorithm();
		this.signal_estimates.setType( Statistic.AggregatedStatisticType.Estimation );
		this.residuals.setType( Statistic.AggregatedStatisticType.Residual );
		this.signal_measurements.setType( Statistic.AggregatedStatisticType.Measurement );
		this.signal_variance.setType( Statistic.AggregatedStatisticType.Variance );
	}

	/**
	 * The implemented algorithm of this Filter.
	 * WLS, Windowed Least Squared.
	 *
	 * @return
	 */
	private void performAlgorithm()
	{
		int windowSize = PropUtils.toInt( settings.getProperty( WINDOW_SIZE_PROPERTY ) );

		LongStatistic estimates = new LongStatistic( signal_measurements.getName() );

		ArrayDeque<DataPoint<Long>> window = new ArrayDeque<DataPoint<Long>>( windowSize );

		for( DataPoint<Long> measuredValue : signal_measurements.getData() )
		{
			int L = window.size();
			if( window.size() == windowSize )
			{
				window.removeLast();
			}
			window.addFirst( measuredValue );

			//Estimate Theta_hat of t : Signal estimation.
			long estimate = 0;
			if( window.isEmpty() )
			{
				//Special case: Initial estimate should be same as measuredValue
				estimate = measuredValue.getData();
			}
			else
			{
				estimate = window.getFirst().getData() + ( ( window.getFirst().getData() - window.getLast().getData() ) / L );
			}
			signal_estimates.getData().add( new LongDataPoint( estimate ) );

			//Minimize error of latest measures in window.
			long lossFactor = 0;
			for( DataPoint<Long> point : window )
			{
				lossFactor += Math.pow( measuredValue.getData() - estimate, 2 );
			}

			//Estimated signal noise
			long estimated_noise = ( 1 / L ) * lossFactor;
			this.residuals.getData().add( new LongDataPoint( estimated_noise ) );

			//Estimated signal variance
			double signal_variance = ( ( 1 / Math.pow( L, 2 ) ) * lossFactor );
			this.signal_variance.getData().add( new DoubleDataPoint( signal_variance ) );
		}
	}

	@Override
	public Statistic<Long> getEstimates()
	{
		return signal_estimates;
	}

	@Override
	public Statistic<Long> getResiduals()
	{
		return residuals;
	}

	@Override
	public Properties getParameters()
	{
		return settings;
	}

	@Override
	public void setParameters( Properties properties )
	{
		this.settings = properties;
	}

	@Override
	public FilterType getType()
	{
		return filterType;
	}

	@Override
	public Enum[] getEnums()
	{
		return AdaptiveFilter.FilterType.values();
	}
}
