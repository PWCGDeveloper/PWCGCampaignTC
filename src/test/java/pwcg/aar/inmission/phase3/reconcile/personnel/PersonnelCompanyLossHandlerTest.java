package pwcg.aar.inmission.phase3.reconcile.personnel;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.data.AARPersonnelLosses;
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
public class PersonnelCompanyLossHandlerTest
{
    private Campaign campaign;
    private List<LogCrewMember> crewMemberStatusList;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        
        campaign = CampaignCache.makeCampaign(CompanyTestProfile.THIRD_DIVISION_PROFILE);
    }

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        crewMemberStatusList = new ArrayList<>();
    }

    private void addCrewMember(Integer serialNumber, int status)
    {
        LogCrewMember companyCrewMember = new LogCrewMember();
        companyCrewMember.setSerialNumber(serialNumber);
        companyCrewMember.setStatus(status);
        crewMemberStatusList.add(companyCrewMember);
    }

    @Test
    public void testEverybodyKilled() throws PWCGException
    {
        CrewMember playerInFlight = campaign.findReferencePlayer();
        addCrewMember(playerInFlight.getSerialNumber(), CrewMemberStatus.STATUS_KIA);

        CrewMember SergeantInFlight = CampaignPersonnelTestHelper.getCrewMemberByRank(campaign, "Sergeant");
        addCrewMember(SergeantInFlight.getSerialNumber(), CrewMemberStatus.STATUS_KIA);

        CrewMember corporalInFlight = CampaignPersonnelTestHelper.getCrewMemberByRank(campaign, "Corporal");
        addCrewMember(corporalInFlight.getSerialNumber(), CrewMemberStatus.STATUS_KIA);

        CrewMember sltInFlight = CampaignPersonnelTestHelper.getCrewMemberByRank(campaign, "2nd Lieutenant");
        addCrewMember(sltInFlight.getSerialNumber(), CrewMemberStatus.STATUS_KIA);

        PersonnelLossHandler crewMemberLossInMissionHandler = new PersonnelLossHandler(campaign);
        AARPersonnelLosses personnelLosses = crewMemberLossInMissionHandler.crewMembersShotDown(crewMemberStatusList);

        Assertions.assertTrue (personnelLosses.getPersonnelKilled().size() == 4);
        Assertions.assertTrue (personnelLosses.getPersonnelCaptured().size() == 0);
        Assertions.assertTrue (personnelLosses.getPersonnelMaimed().size() == 0);
    }

    @Test
    public void testMixedStatusWithMaimed() throws PWCGException
    {
        CrewMember playerInFlight = campaign.findReferencePlayer();
        addCrewMember(playerInFlight.getSerialNumber(), CrewMemberStatus.STATUS_WOUNDED);

        CrewMember sergentInFlight = CampaignPersonnelTestHelper.getCrewMemberByRank(campaign, "Sergeant");
        addCrewMember(sergentInFlight.getSerialNumber(), CrewMemberStatus.STATUS_WOUNDED);

        CrewMember corporalInFlight = CampaignPersonnelTestHelper.getCrewMemberByRank(campaign, "Corporal");
        addCrewMember(corporalInFlight.getSerialNumber(), CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED);

        CrewMember sltInFlight = CampaignPersonnelTestHelper.getCrewMemberByRank(campaign, "2nd Lieutenant");
        addCrewMember(sltInFlight.getSerialNumber(), CrewMemberStatus.STATUS_KIA);

        PersonnelLossHandler crewMemberLossInMissionHandler = new PersonnelLossHandler(campaign);
        AARPersonnelLosses personnelLosses = crewMemberLossInMissionHandler.crewMembersShotDown(crewMemberStatusList);

        Assertions.assertTrue (personnelLosses.getPersonnelKilled().size() == 1);
        Assertions.assertTrue (personnelLosses.getPersonnelCaptured().size() == 0);
        Assertions.assertTrue (personnelLosses.getPersonnelMaimed().size() == 1);
        Assertions.assertTrue (personnelLosses.getPersonnelWounded().size() == 2);
    }

    @Test
    public void testMixedStatusWithCaptured() throws PWCGException
    {
        CrewMember playerInFlight = campaign.findReferencePlayer();
        addCrewMember(playerInFlight.getSerialNumber(), CrewMemberStatus.STATUS_CAPTURED);

        CrewMember sergentInFlight = CampaignPersonnelTestHelper.getCrewMemberByRank(campaign, "Sergeant");
        addCrewMember(sergentInFlight.getSerialNumber(), CrewMemberStatus.STATUS_ACTIVE);

        CrewMember corporalInFlight = CampaignPersonnelTestHelper.getCrewMemberByRank(campaign, "Corporal");
        addCrewMember(corporalInFlight.getSerialNumber(), CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED);

        CrewMember sltInFlight = CampaignPersonnelTestHelper.getCrewMemberByRank(campaign, "2nd Lieutenant");
        addCrewMember(sltInFlight.getSerialNumber(), CrewMemberStatus.STATUS_CAPTURED);

        PersonnelLossHandler crewMemberLossInMissionHandler = new PersonnelLossHandler(campaign);
        AARPersonnelLosses personnelLosses = crewMemberLossInMissionHandler.crewMembersShotDown(crewMemberStatusList);

        Assertions.assertTrue (personnelLosses.getPersonnelKilled().size() == 0);
        Assertions.assertTrue (personnelLosses.getPersonnelCaptured().size() == 2);
        Assertions.assertTrue (personnelLosses.getPersonnelMaimed().size() == 1);
    }

}
