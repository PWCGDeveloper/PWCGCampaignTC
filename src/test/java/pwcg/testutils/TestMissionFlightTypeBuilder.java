package pwcg.testutils;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.core.exception.PWCGException;
import pwcg.mission.MissionCompanyFlightTypes;
import pwcg.mission.flight.FlightTypes;

public class TestMissionFlightTypeBuilder
{
    public static MissionCompanyFlightTypes buildFlightType (Campaign campaign, FlightTypes playerFlightType) throws PWCGException
    {
        Company company = campaign.findReferenceCompany();
        MissionCompanyFlightTypes playerFlightTypes = new MissionCompanyFlightTypes();
        playerFlightTypes.add(company, playerFlightType);
        return playerFlightTypes;
    }
}
