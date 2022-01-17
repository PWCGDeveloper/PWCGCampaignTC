package pwcg.mission;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;

@ExtendWith(MockitoExtension.class)
public class MissionCenterDistanceCalculatorTest
{    
    public MissionCenterDistanceCalculatorTest() throws PWCGException
    {
        
    }
    
    @Test
    public void verifySmallerDistanceToFront () throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);
        
        MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();
        CrewMembers players = campaign.getPersonnelManager().getAllActivePlayers();
        for (CrewMember player : players.getCrewMemberList())
        {
            participatingPlayers.addCrewMember(player);
        }
        
        MissionCenterDistanceCalculator missionCenterDistanceCalculator = new MissionCenterDistanceCalculator(campaign);
        int maxDistanceForMissionCenter = missionCenterDistanceCalculator.determineMaxDistanceForMissionCenter();

        int missionCenterMinDistanceFromBase = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.MissionBoxMinDistanceFromBaseKey) * 1000;
        int missionCenterMaxDistanceFromBase = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.MissionBoxMaxDistanceFromBaseKey) * 1000;
        assert(maxDistanceForMissionCenter >= missionCenterMinDistanceFromBase);
        assert(maxDistanceForMissionCenter <= missionCenterMaxDistanceFromBase);
    }
}
