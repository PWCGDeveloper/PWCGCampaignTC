package pwcg.gui.rofmap.brief;

import java.util.HashMap;
import java.util.Map;

import pwcg.core.exception.PWCGException;
import pwcg.gui.rofmap.brief.builder.BriefingUnitParametersBuilder;
import pwcg.gui.rofmap.brief.model.BriefingPlatoon;
import pwcg.gui.rofmap.brief.model.BriefingUnitParameters;
import pwcg.mission.Mission;
import pwcg.mission.platoon.ITankPlatoon;

public class BriefingMissionFlightSetBuilder
{
    public static Map<Integer, BriefingPlatoon> buildBriefingMissions(Mission mission) throws PWCGException
    {
        Map<Integer, BriefingPlatoon> briefingMissionFlights = new HashMap<>();
        for (ITankPlatoon playerPlatoon : mission.getPlatoons().getPlayerPlatoons())
        {
            
            BriefingUnitParameters briefingFlightParameters = buildBriefingFlightParameters(playerPlatoon);

            BriefingPlatoon briefingMissionFlight = new BriefingPlatoon(mission, briefingFlightParameters, playerPlatoon.getCompany().getCompanyId());
            briefingMissionFlight.initializeFromMission(playerPlatoon.getCompany());

            briefingMissionFlights.put(playerPlatoon.getCompany().getCompanyId(), briefingMissionFlight);
        }
        
        return briefingMissionFlights;
    }
    
    private static BriefingUnitParameters buildBriefingFlightParameters(ITankPlatoon playerPlatoon) throws PWCGException
    {     
        BriefingUnitParametersBuilder briefingFlightParametersBuilder = new BriefingUnitParametersBuilder(playerPlatoon);
        BriefingUnitParameters briefingFlightParameters = briefingFlightParametersBuilder.buildBriefParametersContext();
        return briefingFlightParameters;
    }
}
