package pwcg.mission.flight.factory;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.tank.PwcgRole;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.mission.flight.FlightTypes;

public class BoSFlightTypeCoopFactory implements IFlightTypeFactory
{
    private Campaign campaign;
    public BoSFlightTypeCoopFactory(Campaign campaign)
    {
        this.campaign = campaign;
    }

    @Override
    public FlightTypes getFlightType(Company company, boolean isPlayerFlight, PwcgRole missionRole) throws PWCGException
    {
        if (missionRole == PwcgRole.ROLE_DIVE_BOMB)
        {
            return getDiveBomberFlightType();
        }
        else if (missionRole == PwcgRole.ROLE_BOMBER)
        {
            return getBomberFlightType(company);
        }
        else if (missionRole == PwcgRole.ROLE_FIGHTER)
        {
            return getFighterFlightType(company, isPlayerFlight);
        }
        else if (missionRole == PwcgRole.ROLE_ATTACK)
        {
            return getAttackFlightType();
        }
        else if (missionRole == PwcgRole.ROLE_TRANSPORT)
        {
            return getTransportFlightType(company);
        }
        else
        {
            throw new PWCGMissionGenerationException("No valid role for company: " + company.determineDisplayName(campaign.getDate()));
        }
    }

    private FlightTypes getFighterFlightType(Company company, boolean isPlayerFlight) throws PWCGException
    {
        return FlightTypes.LOW_ALT_CAP;
    }

    private FlightTypes getTransportFlightType(Company company) throws PWCGException
    {
        return FlightTypes.CARGO_DROP;
    }

    private FlightTypes getAttackFlightType() throws PWCGException
    {
        return FlightTypes.GROUND_ATTACK;
    }

    private FlightTypes getBomberFlightType(Company company) throws PWCGException 
    {
        return FlightTypes.LOW_ALT_BOMB;
    }

    private FlightTypes getDiveBomberFlightType() throws PWCGException
    {
        return FlightTypes.DIVE_BOMB;
    }
}
