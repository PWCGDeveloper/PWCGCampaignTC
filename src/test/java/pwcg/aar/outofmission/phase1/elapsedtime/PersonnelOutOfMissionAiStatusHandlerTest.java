package pwcg.aar.outofmission.phase1.elapsedtime;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.data.AARPersonnelLosses;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PersonnelOutOfMissionAiStatusHandlerTest
{
    private Campaign campaign;
    
    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        
        campaign = CampaignCache.makeCampaign(CompanyTestProfile.THIRD_DIVISION_PROFILE);
    }

    @Test
    public void testPersonnelLossesOutOfMission () throws PWCGException
    {     
        PersonnelOutOfMissionStatusHandler personnelLossOutOfMissionHandler = new PersonnelOutOfMissionStatusHandler();
        Map<Integer, CrewMember> campaignMembers = campaign.getPersonnelManager().getActiveCampaignMembers();
        CrewMembers crewMembersInMissionOtherThanPlayer = CrewMemberFilter.filterActiveAI(campaignMembers, campaign.getDate());
        personnelLossOutOfMissionHandler.determineFateOfCrewMembers(crewMembersInMissionOtherThanPlayer.getCrewMemberCollection());
        
        Map<Integer, CrewMember> aiKilled = new HashMap<>();
        Map<Integer, CrewMember> aiMaimed = new HashMap<>();
        Map<Integer, CrewMember> aiCaptured = new HashMap<>();

        for (int i = 0; i < 10; ++i)
        {
            AARPersonnelLosses outOfMissionPersonnelLosses = personnelLossOutOfMissionHandler.determineFateOfCrewMembers(campaignMembers);
            aiKilled.putAll(outOfMissionPersonnelLosses.getPersonnelKilled());
            aiMaimed.putAll(outOfMissionPersonnelLosses.getPersonnelMaimed());
            aiCaptured.putAll(outOfMissionPersonnelLosses.getPersonnelCaptured());
        }
        
        Assertions.assertTrue (aiKilled.size() > 0);
        Assertions.assertTrue (aiMaimed.size() > 0);
        Assertions.assertTrue (aiCaptured.size() > 0);
    }

}
