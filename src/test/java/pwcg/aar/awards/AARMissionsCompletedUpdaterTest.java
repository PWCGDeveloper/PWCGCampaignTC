package pwcg.aar.awards;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.AARPersonnelAcheivements;
import pwcg.aar.outofmission.phase1.elapsedtime.AARMissionsCompletedUpdater;
import pwcg.aar.prelim.AARPreliminaryData;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AARMissionsCompletedUpdaterTest
{
    private Campaign campaign;
    private AARPersonnelAcheivements personnelAcheivements = new AARPersonnelAcheivements();
    
    @Mock
    private AARContext aarContext;

    @Mock
    private AARPreliminaryData preliminaryData;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        
        campaign = CampaignCache.makeCampaign(CompanyTestProfile.THIRD_DIVISION_PROFILE);

        personnelAcheivements = new AARPersonnelAcheivements();
    }

    @Test
    public void testMissionsCompletedIncreased() throws PWCGException
    {
        Mockito.when(aarContext.getPersonnelAcheivements()).thenReturn(personnelAcheivements);
        Mockito.when(aarContext.getPreliminaryData()).thenReturn(preliminaryData);        

        CrewMembers nonPlayerCrewMembers = CrewMemberFilter.filterActiveAINoWounded(campaign.getPersonnelManager().getAllCampaignMembers(), campaign.getDate());

        CrewMembers campaignMembersInMission = new CrewMembers();
        campaignMembersInMission.addToCrewMemberCollection(nonPlayerCrewMembers.getCrewMemberList().get(1));
        campaignMembersInMission.addToCrewMemberCollection(nonPlayerCrewMembers.getCrewMemberList().get(2));
        campaignMembersInMission.addToCrewMemberCollection(nonPlayerCrewMembers.getCrewMemberList().get(3));

        Mockito.when(preliminaryData.getCampaignMembersInMission()).thenReturn(campaignMembersInMission);
        
        int missionsCompletedBefore1 = nonPlayerCrewMembers.getCrewMemberList().get(1).getBattlesFought();
        int missionsCompletedBefore2 = nonPlayerCrewMembers.getCrewMemberList().get(2).getBattlesFought();
        int missionsCompletedBefore3 = nonPlayerCrewMembers.getCrewMemberList().get(3).getBattlesFought();
        
        AARMissionsCompletedUpdater missionsCompleted = new AARMissionsCompletedUpdater(campaign, aarContext);
        missionsCompleted.updateMissionsCompleted();

        Map<Integer, Integer> updatedMissionsFLown = personnelAcheivements.getMissionsCompleted();
        
        assert(updatedMissionsFLown.containsKey(nonPlayerCrewMembers.getCrewMemberList().get(1).getSerialNumber()));
        assert(updatedMissionsFLown.containsKey(nonPlayerCrewMembers.getCrewMemberList().get(2).getSerialNumber()));
        assert(updatedMissionsFLown.containsKey(nonPlayerCrewMembers.getCrewMemberList().get(3).getSerialNumber()));
        
        int missionsCompletedAfter1 = updatedMissionsFLown.get(nonPlayerCrewMembers.getCrewMemberList().get(1).getSerialNumber());
        int missionsCompletedAfter2 = updatedMissionsFLown.get(nonPlayerCrewMembers.getCrewMemberList().get(2).getSerialNumber());
        int missionsCompletedAfter3 = updatedMissionsFLown.get(nonPlayerCrewMembers.getCrewMemberList().get(3).getSerialNumber());

        Assertions.assertTrue ((missionsCompletedAfter1 - missionsCompletedBefore1) == 1);
        Assertions.assertTrue ((missionsCompletedAfter2 - missionsCompletedBefore2) == 1);
        Assertions.assertTrue ((missionsCompletedAfter3 - missionsCompletedBefore3) == 1);
    }

}
