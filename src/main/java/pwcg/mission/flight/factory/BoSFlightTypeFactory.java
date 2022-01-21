package pwcg.mission.flight.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.company.Company;
import pwcg.campaign.tank.PwcgRole;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.core.utils.WeightedOddsCalculator;
import pwcg.mission.flight.FlightTypes;

public class BoSFlightTypeFactory implements IFlightTypeFactory
{
    private Campaign campaign;
    private List<Integer> weightedOdds = new ArrayList<>();
    private Map<Integer, FlightTypes> flightTypesByIndex = new HashMap<>();

    public BoSFlightTypeFactory (Campaign campaign) 
    {
        this.campaign = campaign;
    }

    @Override
    public FlightTypes getFlightType(Company company, boolean isPlayerFlight, PwcgRole missionRole) throws PWCGException
    {
        FlightTypes flightType = null;
        if (missionRole == PwcgRole.ROLE_DIVE_BOMB)
        {
            flightType = getDiveBomberFlightType();
        }
        else if (missionRole == PwcgRole.ROLE_BOMBER)
        {
            flightType = getBomberFlightType(company);
        }
        else if (missionRole == PwcgRole.ROLE_FIGHTER)
        {
            flightType = getFighterFlightType(company, isPlayerFlight);
        }
        else if (missionRole == PwcgRole.ROLE_ATTACK)
        {
            flightType = getAttackFlightType(company, isPlayerFlight);
        }
        else if (missionRole == PwcgRole.ROLE_TRANSPORT)
        {
            flightType = getTransportFlightType(company);
        }
        else
        {
            throw new PWCGMissionGenerationException("No valid role for company: " + company.determineDisplayName(campaign.getDate()));
        }

        return flightType;
    }

    private FlightTypes getFighterFlightType(Company company, boolean isPlayerFlight) throws PWCGException
    {
        int currentIndex = 0;
        if (company.determineCompanyCountry(campaign.getDate()).getSideNoNeutral() == Side.ALLIED)
        {
            currentIndex =  addItemToWeightedList(ConfigItemKeys.AlliedLowAltPatrolMissionKey, FlightTypes.LOW_ALT_PATROL, currentIndex);
            currentIndex =  addItemToWeightedList(ConfigItemKeys.AlliedLowAltCapMissionKey, FlightTypes.LOW_ALT_CAP, currentIndex);
        }
        else
        {
            currentIndex =  addItemToWeightedList(ConfigItemKeys.AxisLowAltPatrolMissionKey, FlightTypes.LOW_ALT_PATROL, currentIndex);
            currentIndex =  addItemToWeightedList(ConfigItemKeys.AxisLowAltCapMissionKey, FlightTypes.LOW_ALT_CAP, currentIndex);
        }
        
        int selectedIndex = WeightedOddsCalculator.calculateWeightedodds(weightedOdds);
        FlightTypes flightType = flightTypesByIndex.get(selectedIndex);

        return flightType;
    }    
    
    private FlightTypes getTransportFlightType(Company company) throws PWCGException
    {
        int currentIndex = 0;
        if (company.determineCompanyCountry(campaign.getDate()).getSideNoNeutral() == Side.ALLIED)
        {
            currentIndex =  addItemToWeightedList(ConfigItemKeys.AlliedCargoDropKey, FlightTypes.CARGO_DROP, currentIndex);
            currentIndex =  addItemToWeightedList(ConfigItemKeys.AlliedParachuteDropKey, FlightTypes.PARATROOP_DROP, currentIndex);
        }
        else
        {
            currentIndex =  addItemToWeightedList(ConfigItemKeys.AxisCargoDropKey, FlightTypes.CARGO_DROP, currentIndex);
            currentIndex =  addItemToWeightedList(ConfigItemKeys.AxisParachuteDropKey, FlightTypes.PARATROOP_DROP, currentIndex);
        }
        
        int selectedIndex = WeightedOddsCalculator.calculateWeightedodds(weightedOdds);
        FlightTypes flightType = flightTypesByIndex.get(selectedIndex);

        return flightType;
    }

    private FlightTypes getAttackFlightType(Company company, boolean isPlayerFlight) throws PWCGException 
    {
        if (!isPlayerFlight)
        {
            return FlightTypes.GROUND_ATTACK;
        }
        
        int currentIndex = 0;
        if (company.determineCompanyCountry(campaign.getDate()).getSideNoNeutral() == Side.ALLIED)
        {
            currentIndex =  addItemToWeightedList(ConfigItemKeys.AlliedGroundAttackKey, FlightTypes.GROUND_ATTACK, currentIndex);
        }
        else
        {
            currentIndex =  addItemToWeightedList(ConfigItemKeys.AxisGroundAttackKey, FlightTypes.GROUND_ATTACK, currentIndex);
        }
        
        int selectedIndex = WeightedOddsCalculator.calculateWeightedodds(weightedOdds);
        FlightTypes flightType = flightTypesByIndex.get(selectedIndex);

        return flightType;
    }
    
    private FlightTypes getBomberFlightType(Company company) throws PWCGException 
    {
        int currentIndex = 0;
        if (company.determineCompanyCountry(campaign.getDate()).getSideNoNeutral() == Side.ALLIED)
        {
            currentIndex =  addItemToWeightedList(ConfigItemKeys.AlliedLowAltBombingMissionKey, FlightTypes.LOW_ALT_BOMB, currentIndex);
        }
        else
        {
            currentIndex =  addItemToWeightedList(ConfigItemKeys.AxisLowAltBombingMissionKey, FlightTypes.LOW_ALT_BOMB, currentIndex);
        }
        
        int selectedIndex = WeightedOddsCalculator.calculateWeightedodds(weightedOdds);
        FlightTypes flightType = flightTypesByIndex.get(selectedIndex);

        return flightType;
    }

    private FlightTypes getDiveBomberFlightType() 
                        throws PWCGException 
    {
        FlightTypes flightType = FlightTypes.DIVE_BOMB;
        return flightType;
    }

    private int addItemToWeightedList(String configKey, FlightTypes flightType, int currentIndex) throws PWCGException
    {
        int oddsOfMission = campaign.getCampaignConfigManager().getIntConfigParam(configKey);
        weightedOdds.add(oddsOfMission);
        flightTypesByIndex.put(currentIndex, flightType);
        return ++currentIndex;
    }
}
