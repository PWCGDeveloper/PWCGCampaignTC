package pwcg.mission.flight.factory;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.skirmish.Skirmish;
import pwcg.campaign.tank.PwcgRole;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.mission.flight.FlightTypes;

public class SkirmishFlightTypeFactory implements IFlightTypeFactory
{
    private Campaign campaign;
    private Skirmish skirmish;
    private IFlightTypeFactory backupFlightTypeFactory;

    public SkirmishFlightTypeFactory (Campaign campaign, Skirmish skirmish, IFlightTypeFactory backupFlightTypeFactory) 
    {
        this.campaign = campaign;
        this.skirmish = skirmish;
        this.backupFlightTypeFactory = backupFlightTypeFactory;
    }

    @Override
    public FlightTypes getFlightType(Company company, boolean isPlayerFlight, PwcgRole missionRole) throws PWCGException
    {
        missionRole = skirmish.forceRoleConversion(missionRole, company.determineSide());
        
        FlightTypes flightType = FlightTypes.ANY;
        if (skirmish.hasFlighTypeForRole(company, missionRole))
        {
            flightType = skirmish.getFlighTypeForRole(company, missionRole);
            PWCGLogger.log(LogLevel.DEBUG, "Skirmish flight type factory returned: " + flightType + " for role " + missionRole + " Company " + company.determineDisplayName(campaign.getDate()));
        }
        else
        {
            flightType = backupFlightTypeFactory.getFlightType(company, isPlayerFlight, missionRole);
            PWCGLogger.log(LogLevel.DEBUG, "Backup flight type factory returned: " + flightType + " for role " + missionRole + " Company " + company.determineDisplayName(campaign.getDate()));
        }

        return flightType;
    }
}
