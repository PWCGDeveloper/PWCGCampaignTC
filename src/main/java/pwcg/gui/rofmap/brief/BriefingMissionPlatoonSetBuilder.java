package pwcg.gui.rofmap.brief;

import java.util.HashMap;
import java.util.Map;

import pwcg.core.exception.PWCGException;
import pwcg.gui.rofmap.brief.builder.BriefingPlatoonParametersBuilder;
import pwcg.gui.rofmap.brief.model.BriefingPlatoon;
import pwcg.gui.rofmap.brief.model.BriefingPlatoonParameters;
import pwcg.mission.Mission;
import pwcg.mission.platoon.ITankPlatoon;

public class BriefingMissionPlatoonSetBuilder
{
    public static Map<Integer, BriefingPlatoon> buildBriefingMissions(Mission mission) throws PWCGException
    {
        Map<Integer, BriefingPlatoon> briefingMissionPlatoons = new HashMap<>();
        for (ITankPlatoon playerPlatoon : mission.getPlatoons().getPlayerPlatoons())
        {

            BriefingPlatoonParameters briefingPlatoonParameters = buildBriefingPlatoonParameters(playerPlatoon);

            BriefingPlatoon briefingMissionPlatoon = new BriefingPlatoon(mission, briefingPlatoonParameters, playerPlatoon.getCompany().getCompanyId());
            briefingMissionPlatoon.initializeFromMission(playerPlatoon.getCompany());

            briefingMissionPlatoons.put(playerPlatoon.getCompany().getCompanyId(), briefingMissionPlatoon);
        }

        return briefingMissionPlatoons;
    }

    private static BriefingPlatoonParameters buildBriefingPlatoonParameters(ITankPlatoon playerPlatoon) throws PWCGException
    {
        BriefingPlatoonParametersBuilder briefingPlatoonParametersBuilder = new BriefingPlatoonParametersBuilder(playerPlatoon);
        BriefingPlatoonParameters briefingPlatoonParameters = briefingPlatoonParametersBuilder.buildBriefParametersContext();
        return briefingPlatoonParameters;
    }
}
