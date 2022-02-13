package pwcg.gui.rofmap.brief.builder;

import java.util.Map;

import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;
import pwcg.gui.rofmap.brief.BriefingMissionFlightSetBuilder;
import pwcg.gui.rofmap.brief.model.BriefingData;
import pwcg.gui.rofmap.brief.model.BriefingPlatoon;
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
        Map<Integer, BriefingPlatoon> briefingMissionFlights = buildBriefingMissions();

        BriefingData briefingData = new BriefingData(mission, briefingMissionFlights);

        int initialCompanyId = getInitialSelectedCompany();
        briefingData.changeSelectedUnit(initialCompanyId);
        
        String missionTIme = getTime();
        briefingData.setMissionTime(missionTIme);
        
        return briefingData;
    }
    
    private String getTime() throws PWCGException
    {
        return mission.getMissionOptions().getMissionTime().getMissionTime();
    }

    private Map<Integer, BriefingPlatoon> buildBriefingMissions() throws PWCGException
    {
        Map<Integer, BriefingPlatoon> briefingMissionFlights = BriefingMissionFlightSetBuilder.buildBriefingMissions(mission);
        return briefingMissionFlights;
    }

    private int getInitialSelectedCompany() throws PWCGException
    {
        CrewMember referencePlayer = mission.getCampaign().findReferencePlayer();
        return referencePlayer.getCompanyId();
    }
}
