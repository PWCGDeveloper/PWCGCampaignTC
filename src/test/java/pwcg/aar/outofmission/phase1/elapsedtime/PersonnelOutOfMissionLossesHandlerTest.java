package pwcg.aar.outofmission.phase1.elapsedtime;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.AARPersonnelLosses;
import pwcg.aar.data.CampaignUpdateData;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTank;
import pwcg.aar.prelim.AARPreliminaryData;
import pwcg.aar.prelim.CampaignMembersOutOfMissionFinder;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PersonnelOutOfMissionLossesHandlerTest
{
    private Campaign campaign;

    @Mock private AARContext aarContext;
    @Mock private CampaignUpdateData campaignUpdateData;
    @Mock private AARPreliminaryData preliminaryData;
    @Mock private CrewMembers crewMembers;

    
    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        
        campaign = CampaignCache.makeCampaign(CompanyTestProfile.THIRD_DIVISION_PROFILE);     
    }

    @BeforeEach
    public void setupTest() throws PWCGException
    {        
        Mockito.when(aarContext.getNewDate()).thenReturn(DateUtils.getDateYYYYMMDD(CompanyTestProfile.THIRD_DIVISION_PROFILE.getDateString()));
    }

    @Test
    public void testAcesKilled () throws PWCGException
    {     
        try (MockedStatic<CampaignMembersOutOfMissionFinder> mocked = Mockito.mockStatic(CampaignMembersOutOfMissionFinder.class)) 
        {
            mocked.when(() -> CampaignMembersOutOfMissionFinder.getAllCampaignMembersNotInMission(Mockito.any(), Mockito.any())).thenReturn(crewMembers);
            mocked.when(() -> CampaignMembersOutOfMissionFinder.getActiveCampaignMembersNotInMission(Mockito.any(), Mockito.any())).thenReturn(crewMembers);

            OutOfMissionLossHandler outOfMissionLossesHandler = new OutOfMissionLossHandler(campaign, aarContext);
            outOfMissionLossesHandler.lossesOutOfMission(new HashMap<Integer, CrewMember>(), new HashMap<Integer, LogTank>());
            AARPersonnelLosses lossesInMissionDataTotal = outOfMissionLossesHandler.getOutOfMissionPersonnelLosses();
            Assertions.assertTrue (lossesInMissionDataTotal.getAcesKilled(campaign).size() == 0);
        }
    }

    @Test
    public void testAiKilled () throws PWCGException
    {     
        try (MockedStatic<CampaignMembersOutOfMissionFinder> mocked = Mockito.mockStatic(CampaignMembersOutOfMissionFinder.class)) 
        {
            mocked.when(() -> CampaignMembersOutOfMissionFinder.getAllCampaignMembersNotInMission(Mockito.any(), Mockito.any())).thenReturn(crewMembers);
            mocked.when(() -> CampaignMembersOutOfMissionFinder.getActiveCampaignMembersNotInMission(Mockito.any(), Mockito.any())).thenReturn(crewMembers);

            Map<Integer, CrewMember> aiKilled = new HashMap<>();
            Map<Integer, CrewMember> aiMaimed = new HashMap<>();
            Map<Integer, CrewMember> aiCaptured = new HashMap<>();
            Map<Integer, CrewMember> aiWounded = new HashMap<>();
    
            OutOfMissionLossHandler outOfMissionLossesHandler = new OutOfMissionLossHandler(campaign, aarContext);
            CrewMembers allAiCampaignMembers = CrewMemberFilter.filterActiveAI(campaign.getPersonnelManager().getAllCampaignMembers(), campaign.getDate());
            outOfMissionLossesHandler.lossesOutOfMission(allAiCampaignMembers.getCrewMemberCollection(), new HashMap<Integer, LogTank>());
    
            AARPersonnelLosses lossesInMissionDataTotal = outOfMissionLossesHandler.getOutOfMissionPersonnelLosses();
            aiKilled.putAll(lossesInMissionDataTotal.getPersonnelKilled());
            aiMaimed.putAll(lossesInMissionDataTotal.getPersonnelCaptured());
            aiCaptured.putAll(lossesInMissionDataTotal.getPersonnelMaimed());
            aiWounded.putAll(lossesInMissionDataTotal.getPersonnelWounded());
            
            Assertions.assertTrue (aiKilled.size() > 0);
            Assertions.assertTrue (aiMaimed.size() > 0);
            Assertions.assertTrue (aiCaptured.size() > 0);
            Assertions.assertTrue (aiWounded.size() > 0);
        }
    }
}
