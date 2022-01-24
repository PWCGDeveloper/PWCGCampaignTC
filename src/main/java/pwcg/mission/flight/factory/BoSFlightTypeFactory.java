package pwcg.mission.flight.factory;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.tank.PwcgRole;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.mission.flight.FlightTypes;

public class BoSFlightTypeFactory implements IFlightTypeFactory
{
    private Campaign campaign;

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
        return FlightTypes.LOW_ALT_CAP;
    }    
    
    private FlightTypes getTransportFlightType(Company company) throws PWCGException
    {
        return FlightTypes.CARGO_DROP;
    }

    private FlightTypes getAttackFlightType(Company company, boolean isPlayerFlight) throws PWCGException 
    {
        return FlightTypes.GROUND_ATTACK;
    }
    
    private FlightTypes getBomberFlightType(Company company) throws PWCGException 
    {
        return FlightTypes.LOW_ALT_BOMB;
    }

    private FlightTypes getDiveBomberFlightType() 
                        throws PWCGException 
    {
        return FlightTypes.DIVE_BOMB;
    }
}
