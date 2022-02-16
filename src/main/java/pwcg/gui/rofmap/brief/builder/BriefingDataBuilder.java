package pwcg.gui.rofmap.brief.builder;

import java.util.Map;

import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;
import pwcg.gui.rofmap.brief.BriefingMissionPlatoonSetBuilder;
import pwcg.gui.rofmap.brief.model.BriefingData;
import pwcg.gui.rofmap.brief.model.IBriefingPlatoon;
import pwcg.mission.Mission;

public class BriefingDataBuilder
{
    private Mission mission;

    public BriefingDataBuilder(Mission mission)
    {
        this.mission = mission;
    }

    public BriefingData buildBriefingData() throws PWCGException
    {
        Map<Integer, IBriefingPlatoon> briefingMissionPlatoons = buildBriefingMissions();

        BriefingData briefingData = new BriefingData(mission, briefingMissionPlatoons);

        int initialCompanyId = getInitialSelectedCompany();
        briefingData.changeSelectedPlayerPlatoon(initialCompanyId);
        briefingData.setSelectedMapEditPlatoon(initialCompanyId);

        String missionTIme = getTime();
        briefingData.setMissionTime(missionTIme);

        return briefingData;
    }

    private String getTime() throws PWCGException
    {
        return mission.getMissionOptions().getMissionTime().getMissionTime();
    }

    private Map<Integer, IBriefingPlatoon> buildBriefingMissions() throws PWCGException
    {
        Map<Integer, IBriefingPlatoon> briefingMissionFlights = BriefingMissionPlatoonSetBuilder.buildBriefingMissions(mission);
        return briefingMissionFlights;
    }

    private int getInitialSelectedCompany() throws PWCGException
    {
        CrewMember referencePlayer = mission.getCampaign().findReferencePlayer();
        return referencePlayer.getCompanyId();
    }
}
