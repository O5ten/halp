package com.osten.halp.impl.shared;

import com.osten.halp.model.profiling.*;
import com.osten.halp.model.shared.ProfileModel;
import com.osten.halp.model.statistics.Statistic;
import com.osten.halp.errorhandling.UnsupportedElementException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.net.URL;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-11-05
 * Time: 16:20
 * To change this template use File | Settings | File Templates.
 */
public class LongProfileModel implements ProfileModel<Long>
{

	private Map<Profile, List<Relation>> profiles;
	private Map<Profile, String> descriptions;

	private Profile selectedProfile;

	public LongProfileModel()
	{

		profiles = new HashMap<Profile, List<Relation>>();
		descriptions = new HashMap<Profile, String>();

		this.selectedProfile = Profile.Custom;

		buildProfiles();
	}

	@Override
	public List<Relation> getRelations()
	{
		return profiles.get( getSelectedProfile() );
	}

	public List<Relation> getRelationsByNewProfile( Profile newProfile )
	{
		this.selectedProfile = newProfile;
		return profiles.get( newProfile );
	}

	@Override
	public AdaptiveFilter.FilterType getFilterByDataType( Statistic.DataType dataType )
	{
		AdaptiveFilter.FilterType foundFilter = null;
		for( Relation relation : profiles.get( selectedProfile ) )
		{
			if( relation.getType() == dataType )
			{
				foundFilter = relation.getFilter();
			}
		}
		return foundFilter;
	}

	@Override
	public ChangeDetector.DetectorType getDetectorByDataType( Statistic.DataType dataType )
	{
		ChangeDetector.DetectorType suggestedDetector = null;
		for( Relation relation : profiles.get( selectedProfile ) )
		{
			if( relation.getType() == dataType )
			{
				suggestedDetector = relation.getDetector();
			}
		}
		return suggestedDetector;
	}

	@Override
	public String getDescriptionByProfile( Profile profile )
	{
		return descriptions.get( profile );
	}

	@Override
	public String getDescription()
	{
		return descriptions.get( selectedProfile );
	}

	@Override
	public void selectProfile( Profile profile )
	{
		this.selectedProfile = profile;
	}

	@Override
	public void resetModel()
	{
		//Nothing to reset, yet.
	}

	private enum Element
	{
		Profiling, Profile, Description, Relations, Relation, Type, State, Detector, Filter
	}

	@Override
	public void buildProfiles()
	{
		try
		{
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = null;

			saxParser = factory.newSAXParser();


			DefaultHandler handler = new DefaultHandler()
			{
				Element currentElement;

				public void startElement( String uri, String localName, String qName, Attributes attributes ) throws SAXException
				{

					if( qName.equalsIgnoreCase( "PROFILING" ) )
					{
						currentElement = Element.Profiling;

						//ROOT element
					}
					else if( qName.equalsIgnoreCase( "PROFILE" ) )
					{
						currentElement = Element.Profile;
						String profile = attributes.getValue( 0 );

						if( profile.equalsIgnoreCase( Profile.CPU.toString() ) )
						{

							addProfile( Profile.CPU );

						}
						else if( profile.equalsIgnoreCase( Profile.Memory.toString() ) )
						{

							addProfile( Profile.Memory );

						}
						else if( profile.equalsIgnoreCase( Profile.Network.toString() ) )
						{

							addProfile( Profile.Network );

						}
						else if( profile.equalsIgnoreCase( Profile.Custom.toString() ) )
						{

							addProfile( Profile.Custom );

						}
						else if( profile.equalsIgnoreCase( Profile.ALL.toString() ) )
						{

							addProfile( Profile.ALL );

						}
						else
						{
							throw new UnsupportedElementException();
						}

					}
					else if( qName.equalsIgnoreCase( "DESCRIPTION" ) )
					{
						currentElement = Element.Description;


					}
					else if( qName.equalsIgnoreCase( "RELATIONS" ) )
					{
						currentElement = Element.Relations;
						//Nothing

					}
					else if( qName.equalsIgnoreCase( "RELATION" ) )
					{
						currentElement = Element.Relation;

						profiles.get( selectedProfile ).add( new QuantumRelation() );

					}
					else if( qName.equalsIgnoreCase( "TYPE" ) )
					{
						currentElement = Element.Type;

					}
					else if( qName.equalsIgnoreCase( "STATE" ) )
					{
						currentElement = Element.State;

					}
					else if( qName.equalsIgnoreCase( "DETECTOR" ) )
					{
						currentElement = Element.Detector;

					}
					else if( qName.equalsIgnoreCase( "FILTER" ) )
					{
						currentElement = Element.Filter;

					}
					else
					{

					}
				}

				private void addProfile( Profile profile )
				{
					selectedProfile = profile;
					profiles.put( profile, new ArrayList<Relation>() );
				}

				private Relation getLastRelation()
				{
					return profiles.get( selectedProfile ).get( profiles.get( selectedProfile ).size() - 1 );
				}

				public void endElement( String uri, String localName, String qName ) throws SAXException
				{

				}

				public void characters( char ch[], int start, int length ) throws SAXException
				{

					switch( currentElement )
					{
						case Type:
							getLastRelation().setType( new String( ch, start, length ) );
							break;

						case Description:
							descriptions.put( selectedProfile, new String( ch, start, length ) );
							break;

						case State:
							getLastRelation().setState( new String( ch, start, length ) );
							break;

						case Detector:
							getLastRelation().setDetectorType( new String( ch, start, length ) );
							break;

						case Filter:
							getLastRelation().setFilterType( new String( ch, start, length ) );
							break;

						default:

					}
				}
			};

			URL path = getClass().getResource( "/profiling/relations.xml" );
			saxParser.parse( path.toString(), handler );
		}
		catch( Exception e )
		{
			System.err.println( "Failed to parse XML. " );
			e.printStackTrace();
		}
		//System.out.println( "Parsed profile-relations. " );

	}

	/**
	 * The only thing that should be possible to modify in this model.
	 */
	public Profile getSelectedProfile()
	{
		return selectedProfile;
	}


	private int getStateByStatistic( Statistic<Long> statistic )
	{
		for( Relation relation : getRelations() )
		{
			if( statistic.getType().equals( relation.getType() ) )
			{
				return relation.getState();
			}
		}
		return 0;
	}

	private PointsOfInterest poi = new PointsOfInterest( 0 );

	@Override
	public PointsOfInterest getPointsOfInterests(){
		return poi;
	}

	@Override
	public void generatePointsOfInterests( Map<Statistic<Long>, List<Detection<Long>>> detectionsByStatistic )
	{
		int i = 0;
		for( Statistic<Long> statistic : detectionsByStatistic.keySet() )
		{
			i = statistic.size();
			break;
		}

		PointsOfInterest poi = new PointsOfInterest( i );
		poi.setProfile( selectedProfile );
		Map<Integer, List<List<Detection<Long>>>> omniDetections = new HashMap<Integer, List<List<Detection<Long>>>>();
		omniDetections.put( 2, new ArrayList<List<Detection<Long>>>() );
		omniDetections.put( -2, new ArrayList<List<Detection<Long>>>() );
		for( Statistic<Long> statistic : detectionsByStatistic.keySet() )
		{
			int state = getStateByStatistic( statistic );

			poi.addInvolvedStatistic( statistic );

			//true
			if( state == 1 )
			{
				poi.and( detectionsByStatistic.get( statistic ) );
			}

			//false
			else if( state == -1 )
			{
				poi.not( detectionsByStatistic.get( statistic ) );
			}

			//Omnitrue
			else if( state == 2){
				omniDetections.get(state).add( detectionsByStatistic.get( statistic ) );

			//Omnifalse
			}else if(state == -2){
				omniDetections.get(state).add( detectionsByStatistic.get( statistic ) );
			}
			else
			{
				//Does not matter which
			}
		}
		for ( List<Detection<Long>> detections : omniDetections.get( 2 ) ){
			poi.or( detections );
		}
		for ( List<Detection<Long>> detections : omniDetections.get( -2 ) ){
			poi.not( detections );
		}
		this.poi = poi;
	}
}
