package pwcg.gui.rofmap.brief;

import java.util.HashMap;
import java.util.Map;

import pwcg.core.exception.PWCGException;
import pwcg.gui.rofmap.brief.builder.BriefingPlatoonParametersBuilder;
import pwcg.gui.rofmap.brief.model.BriefingAiPlatoon;
import pwcg.gui.rofmap.brief.model.BriefingPlatoonParameters;
import pwcg.gui.rofmap.brief.model.BriefingPlayerPlatoon;
import pwcg.gui.rofmap.brief.model.IBriefingPlatoon;
import pwcg.mission.Mission;
import pwcg.mission.platoon.ITankPlatoon;

public class BriefingMissionPlatoonSetBuilder
{
    public static Map<Integer, IBriefingPlatoon> buildBriefingMissions(Mission mission) throws PWCGException
    {
        Map<Integer, IBriefingPlatoon> briefingMissionPlatoons = new HashMap<>();
        for (ITankPlatoon playerPlatoon : mission.getPlatoons().getPlayerPlatoons())
        {

            BriefingPlatoonParameters briefingPlatoonParameters = buildBriefingPlatoonParameters(playerPlatoon);

            BriefingPlayerPlatoon briefingMissionPlatoon = new BriefingPlayerPlatoon(
                    mission, briefingPlatoonParameters, playerPlatoon.getCompany().getCompanyId(), playerPlatoon.getCompany().determineSide());
            briefingMissionPlatoon.initializeFromMission(playerPlatoon.getCompany());

            briefingMissionPlatoons.put(playerPlatoon.getCompany().getCompanyId(), briefingMissionPlatoon);
        }

        for (ITankPlatoon platoon : mission.getPlatoons().getAiPlatoons())
        {

            BriefingPlatoonParameters briefingPlatoonParameters = buildBriefingPlatoonParameters(platoon);
            BriefingAiPlatoon briefingMissionPlatoon = new BriefingAiPlatoon(
                    briefingPlatoonParameters, platoon.getCompany().getCompanyId(), platoon.getCompany().determineSide());
            briefingMissionPlatoons.put(platoon.getCompany().getCompanyId(), briefingMissionPlatoon);
        }

        return briefingMissionPlatoons;
    }

    private static BriefingPlatoonParameters buildBriefingPlatoonParameters(ITankPlatoon platoon) throws PWCGException
    {
        BriefingPlatoonParametersBuilder briefingPlatoonParametersBuilder = new BriefingPlatoonParametersBuilder(platoon);
        BriefingPlatoonParameters briefingPlatoonParameters = briefingPlatoonParametersBuilder.buildBriefParametersContext();
        return briefingPlatoonParameters;
    }
}
