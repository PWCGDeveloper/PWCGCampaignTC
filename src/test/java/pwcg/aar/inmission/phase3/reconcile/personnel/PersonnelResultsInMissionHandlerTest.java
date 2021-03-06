package pwcg.aar.inmission.phase3.reconcile.personnel;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.inmission.phase2.logeval.AARMissionEvaluationData;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogCrewMember;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CampaignPersonnelTestHelper;
import pwcg.testutils.CompanyTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PersonnelResultsInMissionHandlerTest
{
    private Campaign campaign;
    private List<LogCrewMember> crewMemberStatusList;
    
    @Mock
    private AARMissionEvaluationData evaluationData;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        
        campaign = CampaignCache.makeCampaign(CompanyTestProfile.THIRD_DIVISION_PROFILE);
    }

    @BeforeEach
    public void setupTest() throws PWCGException
    {        
        crewMemberStatusList = new ArrayList<>();
        Mockito.when(evaluationData.getCrewMembersInMission()).thenReturn(crewMemberStatusList);
    }

    @Test
    public void testMixedStatusWithMaimed() throws PWCGException
    {
        createCrewMembersInMission();
        
        PersonnelLossesInMissionHandler inMissionHandler = new PersonnelLossesInMissionHandler(campaign, evaluationData);
        inMissionHandler.personellChanges();
        
        assert(inMissionHandler.personellChanges().getPersonnelKilled().size() == 1);
        assert(inMissionHandler.personellChanges().getPersonnelCaptured().size() == 1);
        assert(inMissionHandler.personellChanges().getPersonnelMaimed().size() == 1);
        assert(inMissionHandler.personellChanges().getPersonnelWounded().size() == 2);
    }

    private void createCrewMembersInMission() throws PWCGException
    {
        CrewMember playerInFlight = campaign.findReferencePlayer();
        addCompanyCrewMember(playerInFlight.getSerialNumber(), CrewMemberStatus.STATUS_WOUNDED);

        CrewMember sergentInFlight = CampaignPersonnelTestHelper.getCrewMemberByRank(campaign, "Sergeant");
        addCompanyCrewMember(sergentInFlight.getSerialNumber(), CrewMemberStatus.STATUS_WOUNDED);

        CrewMember corporalInFlight = CampaignPersonnelTestHelper.getCrewMemberByRank(campaign, "Corporal");
        addCompanyCrewMember(corporalInFlight.getSerialNumber(), CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED);
        
        CrewMember sltInFlight = CampaignPersonnelTestHelper.getCrewMemberByRank(campaign, "2nd Lieutenant");
        addCompanyCrewMember(sltInFlight.getSerialNumber(), CrewMemberStatus.STATUS_KIA);
        
        CrewMember ltInFlight = CampaignPersonnelTestHelper.getCrewMemberByRank(campaign, "1st Lieutenant");
        addCompanyCrewMember(ltInFlight.getSerialNumber(), CrewMemberStatus.STATUS_CAPTURED);
    }
    
    private void addCompanyCrewMember(int serialNumber, int status)
    {
        LogCrewMember companyCrewMember = new LogCrewMember();
        companyCrewMember.setSerialNumber(serialNumber);
        companyCrewMember.setStatus(status);
        crewMemberStatusList.add(companyCrewMember);
    }
}
