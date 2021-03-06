package pwcg.mission;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.PlaneTypeFactory;
import pwcg.campaign.tank.PwcgRoleCategory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightTypes;

public class AIFlightPlanner
{
    public static List<FlightBuildInformation> createFlightBuildInformationForMission(Mission mission) throws PWCGException 
    {
        List<FlightBuildInformation> flightBuildInformationForMission = new ArrayList<>();

        int numAlliedFlights = 2;
        List<FlightBuildInformation> flightBuildInformationAllied = createFlightBuildInformationForCountry(mission, Side.ALLIED, numAlliedFlights);
        flightBuildInformationForMission.addAll(flightBuildInformationAllied);

        int numAxisFlights = 1;
        List<FlightBuildInformation> flightBuildInformationAxis = createFlightBuildInformationForCountry(mission, Side.AXIS, numAxisFlights);
        flightBuildInformationForMission.addAll(flightBuildInformationAxis);
        
        return flightBuildInformationForMission;
    }

    private static List<FlightBuildInformation> createFlightBuildInformationForCountry(Mission mission, Side side, int numFlights) throws PWCGException
    {
        List<FlightBuildInformation> flightBuildInformatioForSide = new ArrayList<>();
        if( mission.getSkirmish() != null)
        {
            return buildFlightInformationSkirmish(mission, side, flightBuildInformatioForSide);
        }
        else
        {
            return buildFlightInformationNormal(mission, side, numFlights, flightBuildInformatioForSide);
        }
    }

    private static List<FlightBuildInformation> buildFlightInformationSkirmish(Mission mission, Side side,
            List<FlightBuildInformation> flightBuildInformatioForSide) throws PWCGException
    {
        ICountry country= PWCGContext.getInstance().getCurrentMap().getGroundCountryForMapBySide(side);
        for(FlightTypes flightType : mission.getSkirmish().getIconicFlightTypesForSide(side))
        {
            FlightBuildInformation flightBuildInformation = buildFlightINformation(mission, side, country, flightType);
            flightBuildInformatioForSide.add(flightBuildInformation);
        }
        return flightBuildInformatioForSide;
    }

    private static List<FlightBuildInformation> buildFlightInformationNormal(Mission mission, Side side, int numFlights,
            List<FlightBuildInformation> flightBuildInformatioForSide) throws PWCGException
    {
        for(int i = 0; i <numFlights; ++i)
        {
            ICountry country= PWCGContext.getInstance().getCurrentMap().getGroundCountryForMapBySide(side);
            FlightTypes flightType = determineFlightType(mission.getCampaign(), country);
            FlightBuildInformation flightBuildInformation = buildFlightINformation(mission, side, country, flightType);
            flightBuildInformatioForSide.add(flightBuildInformation);
        }
        return flightBuildInformatioForSide;
    }

    private static FlightBuildInformation buildFlightINformation(Mission mission, Side side, ICountry country, FlightTypes flightType) throws PWCGException
    {
        PlaneType planeType = getPlaneTypeForMission(country, mission.getCampaign().getDate(), flightType);
        Airfield airfield = findHomeAirfield(mission, side);                        
        FlightBuildInformation flightBuildInformation = new FlightBuildInformation(mission, country, flightType, planeType, airfield.getPosition());
        return flightBuildInformation;
    }

    private static Airfield findHomeAirfield(Mission mission, Side side) throws PWCGException
    {
        Airfield airfield = PWCGContext.getInstance().getCurrentMap().getAirfieldManager().getAirfieldFinder().findClosestAirfieldForSide(
                mission.getMissionBorders().getCenter(), mission.getCampaign().getDate(), side);
        return airfield;

    }
    
    private static FlightTypes determineFlightType(Campaign campaign, ICountry country) throws PWCGException
    {
        if(country.getCountry() == Country.GERMANY)
        {
            return getGermanFlightType(campaign);
        }
        else
        {
            return getGenericFlightType();
        }
    }

    private static FlightTypes getGermanFlightType(Campaign campaign) throws PWCGException
    {
        int roll = RandomNumberGenerator.getRandom(100);
        if(roll < 30)
        {
            if (campaign.getDate().before(DateUtils.getDateYYYYMMDD("19431001")))
            {
                return FlightTypes.DIVE_BOMB;
            }
            else
            {
                return FlightTypes.GROUND_ATTACK;
            }

        }
        else if(roll < 50)
        {
            return FlightTypes.GROUND_ATTACK;
        }
        else if(roll < 65)
        {
            return FlightTypes.LOW_ALT_BOMB;
        }
        else if(roll < 75)
        {
            return FlightTypes.LOW_ALT_PATROL;
        }
        else
        {
            return FlightTypes.LOW_ALT_CAP;
        }
    }


    private static FlightTypes getGenericFlightType()
    {
        int roll = RandomNumberGenerator.getRandom(100);
        if(roll < 50)
        {
            return FlightTypes.GROUND_ATTACK;
        }
        else if(roll < 65)
        {
            return FlightTypes.LOW_ALT_BOMB;
        }
        else if(roll < 75)
        {
            return FlightTypes.LOW_ALT_PATROL;
        }
        else
        {
            return FlightTypes.LOW_ALT_CAP;
        }
    }

    private static PlaneType getPlaneTypeForMission(ICountry country, Date date, FlightTypes flightType) throws PWCGException
    {
        PlaneTypeFactory planeTypeFactory = PWCGContext.getInstance().getPlaneTypeFactory();
        if(flightType == FlightTypes.DIVE_BOMB)
        {
            return planeTypeFactory.findActivePlaneTypeByCountryDateAndRole(country, date, PwcgRoleCategory.ATTACK);
        }
        else if(flightType == FlightTypes.GROUND_ATTACK)
        {
            return planeTypeFactory.findActivePlaneTypeByCountryDateAndRole(country, date, PwcgRoleCategory.ATTACK);
        }
        else if(flightType == FlightTypes.LOW_ALT_BOMB)
        {
            return planeTypeFactory.findActivePlaneTypeByCountryDateAndRole(country, date, PwcgRoleCategory.BOMBER);
        }
        else if(flightType == FlightTypes.LOW_ALT_CAP)
        {
            return planeTypeFactory.findActivePlaneTypeByCountryDateAndRole(country, date, PwcgRoleCategory.FIGHTER);
        }
        else if(flightType == FlightTypes.LOW_ALT_PATROL)
        {
            return planeTypeFactory.findActivePlaneTypeByCountryDateAndRole(country, date, PwcgRoleCategory.FIGHTER);
        }
        else
        {
            return planeTypeFactory.findActivePlaneTypeByCountryDateAndRole(country, date, PwcgRoleCategory.ATTACK);
        }
    }
}
