package pwcg.gui.rofmap.brief;

import java.util.HashMap;
import java.util.Map;

import pwcg.core.exception.PWCGException;
import pwcg.gui.rofmap.brief.builder.BriefingUnitParametersBuilder;
import pwcg.gui.rofmap.brief.model.BriefingUnit;
import pwcg.gui.rofmap.brief.model.BriefingUnitParameters;
import pwcg.mission.Mission;
import pwcg.mission.unit.IPlayerUnit;

public class BriefingMissionFlightSetBuilder
{
    public static Map<Integer, BriefingUnit> buildBriefingMissions(Mission mission) throws PWCGException
    {
        Map<Integer, BriefingUnit> briefingMissionFlights = new HashMap<>();
        for (IPlayerUnit playerUnit : mission.getUnits().getPlayerUnits())
        {
            
            BriefingUnitParameters briefingFlightParameters = buildBriefingFlightParameters(playerUnit);

            BriefingUnit briefingMissionFlight = new BriefingUnit(mission, briefingFlightParameters, playerUnit.getCompany().getCompanyId());
            briefingMissionFlight.initializeFromMission(playerUnit.getCompany());

            briefingMissionFlights.put(playerUnit.getCompany().getCompanyId(), briefingMissionFlight);
        }
        
        return briefingMissionFlights;
    }
    
    private static BriefingUnitParameters buildBriefingFlightParameters(IPlayerUnit playerUnit) throws PWCGException
    {     
        BriefingUnitParametersBuilder briefingFlightParametersBuilder = new BriefingUnitParametersBuilder(playerUnit);
        BriefingUnitParameters briefingFlightParameters = briefingFlightParametersBuilder.buildBriefParametersContext();
        return briefingFlightParameters;
    }
}
