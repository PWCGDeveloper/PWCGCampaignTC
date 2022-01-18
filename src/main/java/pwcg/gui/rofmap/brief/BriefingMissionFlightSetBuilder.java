package pwcg.gui.rofmap.brief;

import java.util.HashMap;
import java.util.Map;

import pwcg.core.exception.PWCGException;
import pwcg.gui.rofmap.brief.builder.BriefingUnitParametersBuilder;
import pwcg.gui.rofmap.brief.model.BriefingUnit;
import pwcg.gui.rofmap.brief.model.BriefingUnitParameters;
import pwcg.mission.Mission;
import pwcg.mission.unit.ITankPlatoon;

public class BriefingMissionFlightSetBuilder
{
    public static Map<Integer, BriefingUnit> buildBriefingMissions(Mission mission) throws PWCGException
    {
        Map<Integer, BriefingUnit> briefingMissionFlights = new HashMap<>();
        for (ITankPlatoon playerPlatoon : mission.getPlatoons().getPlayerUnits())
        {
            
            BriefingUnitParameters briefingFlightParameters = buildBriefingFlightParameters(playerPlatoon);

            BriefingUnit briefingMissionFlight = new BriefingUnit(mission, briefingFlightParameters, playerPlatoon.getCompany().getCompanyId());
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
