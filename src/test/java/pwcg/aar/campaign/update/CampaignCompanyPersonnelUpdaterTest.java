package pwcg.aar.campaign.update;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.AARPersonnelLosses;
import pwcg.aar.data.CampaignUpdateData;
import pwcg.aar.outofmission.phase4.ElapsedTIme.OutOfMissionCommandChangeHandler;
import pwcg.aar.tabulate.campaignupdate.AARCampaignUpdateTabulator;
import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.campaign.personnel.PersonnelReplacementsService;
import pwcg.campaign.resupply.personnel.TransferRecord;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;
import pwcg.testutils.CrewMemberPicker;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CampaignCompanyPersonnelUpdaterTest
{
    private Map<Integer, CrewMember> squadMembersKilled = new HashMap<>();
    private Map<Integer, CrewMember> squadMembersCaptured = new HashMap<>();
    private Map<Integer, CrewMember> squadMembersMaimed = new HashMap<>();
    private Map<Integer, CrewMember> squadMembersWounded = new HashMap<>();
    private Map<Integer, CrewMember> acesKilled = new HashMap<>();

    private Campaign campaign;
    private static AARContext aarContext;

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        campaign = CampaignCache.makeCampaign(CompanyTestProfile.THIRD_DIVISION_PROFILE);
        aarContext = new AARContext(campaign);
        aarContext.setNewDate(DateUtils.advanceTimeDays(campaign.getDate(), 3));

        squadMembersKilled.clear();
        squadMembersMaimed.clear();
        squadMembersCaptured.clear();
        squadMembersWounded.clear();
        acesKilled.clear();
    }

    @AfterEach
    public void reset() throws PWCGException
    {
        CompanyPersonnel personnel = campaign.getPersonnelManager().getCompanyPersonnel(CompanyTestProfile.THIRD_DIVISION_PROFILE.getCompanyId());
        for (CrewMember crewMember : personnel.getCrewMembersWithAces().getCrewMemberList())
        {
            crewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_ACTIVE, null, null);
        }
    }

    @Test
    public void testCrewMemberKilled() throws PWCGException
    {
        CrewMember deadCrewMember = CrewMemberPicker.pickNonAceCrewMember(campaign, CompanyTestProfile.THIRD_DIVISION_PROFILE.getCompanyId());
        deadCrewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_KIA, campaign.getDate(), null);
        squadMembersKilled.put(deadCrewMember.getSerialNumber(), deadCrewMember);

        AARCampaignUpdateTabulator campaignUpdateTabulator = new AARCampaignUpdateTabulator(campaign, aarContext);
        CampaignUpdateData campaignUpdateData = campaignUpdateTabulator.tabulateAARForCampaignUpdate();

        for (CrewMember crewMember : squadMembersKilled.values())
        {
            campaignUpdateData.getPersonnelLosses().addPersonnelKilled(crewMember);
        }

        PersonnelUpdater personellUpdater = new PersonnelUpdater(campaign, campaignUpdateData);
        personellUpdater.personnelUpdates();

        CrewMember companyMemberAfterUpdate = campaign.getPersonnelManager().getAnyCampaignMember(deadCrewMember.getSerialNumber());
        Assertions.assertTrue(companyMemberAfterUpdate.getCrewMemberActiveStatus() == CrewMemberStatus.STATUS_KIA);
    }

    @Test
    public void testCrewMemberCaptured() throws PWCGException
    {
        CrewMember capturedCrewMember = CrewMemberPicker.pickNonAceCrewMember(campaign, CompanyTestProfile.THIRD_DIVISION_PROFILE.getCompanyId());
        capturedCrewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_CAPTURED, campaign.getDate(), null);
        squadMembersCaptured.put(capturedCrewMember.getSerialNumber(), capturedCrewMember);

        AARCampaignUpdateTabulator campaignUpdateTabulator = new AARCampaignUpdateTabulator(campaign, aarContext);
        CampaignUpdateData campaignUpdateData = campaignUpdateTabulator.tabulateAARForCampaignUpdate();

        for (CrewMember crewMember : squadMembersCaptured.values())
        {
            campaignUpdateData.getPersonnelLosses().addPersonnelCaptured(crewMember);
        }

        PersonnelUpdater personellUpdater = new PersonnelUpdater(campaign, campaignUpdateData);
        personellUpdater.personnelUpdates();

        CrewMember companyMemberAfterUpdate = campaign.getPersonnelManager().getAnyCampaignMember(capturedCrewMember.getSerialNumber());
        Assertions.assertTrue(companyMemberAfterUpdate.getCrewMemberActiveStatus() == CrewMemberStatus.STATUS_CAPTURED);
    }

    @Test
    public void testCrewMemberMaimed() throws PWCGException
    {
        CrewMember maimedCrewMember = CrewMemberPicker.pickNonAceCrewMember(campaign, CompanyTestProfile.THIRD_DIVISION_PROFILE.getCompanyId());
        Date recoveryDate = DateUtils.advanceTimeDays(campaign.getDate(), 21);
        maimedCrewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED, campaign.getDate(), recoveryDate);
        squadMembersMaimed.put(maimedCrewMember.getSerialNumber(), maimedCrewMember);

        AARCampaignUpdateTabulator campaignUpdateTabulator = new AARCampaignUpdateTabulator(campaign, aarContext);
        CampaignUpdateData campaignUpdateData = campaignUpdateTabulator.tabulateAARForCampaignUpdate();

        for (CrewMember crewMember : squadMembersMaimed.values())
        {
            campaignUpdateData.getPersonnelLosses().addPersonnelMaimed(crewMember);
        }

        PersonnelUpdater personellUpdater = new PersonnelUpdater(campaign, campaignUpdateData);
        personellUpdater.personnelUpdates();

        CrewMember companyMemberAfterUpdate = campaign.getPersonnelManager().getAnyCampaignMember(maimedCrewMember.getSerialNumber());
        Assertions.assertTrue (companyMemberAfterUpdate.getCrewMemberActiveStatus() == CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED);
    }

    @Test
    public void testPlayerMemberMaimed() throws PWCGException
    {
        CrewMember maimedPlayer = CrewMemberPicker.pickPlayerCrewMember(campaign, CompanyTestProfile.THIRD_DIVISION_PROFILE.getCompanyId());
        squadMembersMaimed.put(maimedPlayer.getSerialNumber(), maimedPlayer);

        AARCampaignUpdateTabulator campaignUpdateTabulator = new AARCampaignUpdateTabulator(campaign, aarContext);
        CampaignUpdateData campaignUpdateData = campaignUpdateTabulator.tabulateAARForCampaignUpdate();

        for (CrewMember crewMember : squadMembersMaimed.values())
        {
            campaignUpdateData.getPersonnelLosses().addPersonnelMaimed(crewMember);
        }

        PersonnelUpdater personellUpdater = new PersonnelUpdater(campaign, campaignUpdateData);
        personellUpdater.personnelUpdates();

        CrewMember companyMemberAfterUpdate = campaign.getPersonnelManager().getAnyCampaignMember(maimedPlayer.getSerialNumber());
        Assertions.assertTrue (companyMemberAfterUpdate.getCrewMemberActiveStatus() == CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED);
        Assertions.assertTrue (maimedPlayer.getRecoveryDate().after(campaign.getDate()));
    }

    @Test
    public void testPlayerBecomesCommander() throws PWCGException
    {
        CrewMember commander = null;
        commander = getAiCommander();
        Assertions.assertTrue (commander != null);

        CrewMember player = CrewMemberPicker.pickPlayerCrewMember(campaign, CompanyTestProfile.THIRD_DIVISION_PROFILE.getCompanyId());

        IRankHelper iRank = RankFactory.createRankHelper();
        String commandRank = iRank.getRankByService(0, player.determineService(campaign.getDate()));
        player.setRank(commandRank);

        OutOfMissionCommandChangeHandler commandChangeHandler = new OutOfMissionCommandChangeHandler(campaign);
        AARPersonnelLosses personnelLossesTransferHome = commandChangeHandler.replaceCommanderWithPlayer();

        AARCampaignUpdateTabulator campaignUpdateTabulator = new AARCampaignUpdateTabulator(campaign, aarContext);
        CampaignUpdateData campaignUpdateData = campaignUpdateTabulator.tabulateAARForCampaignUpdate();

        for (CrewMember crewMember : personnelLossesTransferHome.getPersonnelTransferredHome().values())
        {
            aarContext.getPersonnelLosses().addPersonnelTransferredHome(crewMember);
            ;
        }

        PersonnelUpdater personellUpdater = new PersonnelUpdater(campaign, campaignUpdateData);
        personellUpdater.personnelUpdates();

        CrewMember commanderAfterUpdate = campaign.getPersonnelManager().getAnyCampaignMember(commander.getSerialNumber());
        Assertions.assertTrue (commanderAfterUpdate.getCrewMemberActiveStatus() == CrewMemberStatus.STATUS_TRANSFERRED);
        Assertions.assertTrue (commanderAfterUpdate.getInactiveDate().equals(campaign.getDate()));

        CrewMember activeCommander = getAiCommander();
        Assertions.assertTrue (activeCommander == null);
    }

    private CrewMember getAiCommander() throws PWCGException
    {
        CrewMember commander = null;
        CompanyPersonnel crewMembers = campaign.getPersonnelManager().getCompanyPersonnel(CompanyTestProfile.THIRD_DIVISION_PROFILE.getCompanyId());
        CrewMembers activeCrewMembers = CrewMemberFilter
                .filterActiveAIAndPlayerAndAces(crewMembers.getCrewMembers().getCrewMemberCollection(), campaign.getDate());
        for (CrewMember crewMember : activeCrewMembers.getCrewMemberList())
        {
            if (crewMember.determineIsCrewMemberCommander(campaign) && !crewMember.isPlayer())
            {
                commander = crewMember;
            }
        }
        return commander;
    }

    @Test
    public void testWoundedCrewMemberHealed() throws PWCGException
    {
        CrewMember woundedCrewMember = CrewMemberPicker.pickNonAceCrewMember(campaign, CompanyTestProfile.THIRD_DIVISION_PROFILE.getCompanyId());
        squadMembersWounded.put(woundedCrewMember.getSerialNumber(), woundedCrewMember);

        AARCampaignUpdateTabulator campaignUpdateTabulator = new AARCampaignUpdateTabulator(campaign, aarContext);
        CampaignUpdateData campaignUpdateData = campaignUpdateTabulator.tabulateAARForCampaignUpdate();

        for (CrewMember crewMember : squadMembersWounded.values())
        {
            campaignUpdateData.getPersonnelLosses().addPersonnelWounded(crewMember);
        }

        PersonnelUpdater personellUpdater = new PersonnelUpdater(campaign, campaignUpdateData);
        personellUpdater.personnelUpdates();

        CrewMember companyMemberAfterUpdate = campaign.getPersonnelManager().getAnyCampaignMember(woundedCrewMember.getSerialNumber());
        Assertions.assertTrue (companyMemberAfterUpdate.getCrewMemberActiveStatus() == CrewMemberStatus.STATUS_WOUNDED);
        Assertions.assertTrue (woundedCrewMember.getRecoveryDate().after(campaign.getDate()));
    }

    @Test
    public void testCrewMemberTransferred() throws PWCGException
    {
        ArmedService armedService = PWCGContext.getInstance().getCompanyManager().getCompany(CompanyTestProfile.THIRD_DIVISION_PROFILE.getCompanyId())
                .determineServiceForCompany(campaign.getDate());
        PersonnelReplacementsService serviceReplacements = campaign.getPersonnelManager().getPersonnelReplacementsService(armedService.getServiceId());
        CrewMember transferredCrewMember = serviceReplacements.findReplacement();

        TransferRecord transferRecord = new TransferRecord(transferredCrewMember, CompanyTestProfile.THIRD_DIVISION_PROFILE.getCompanyId(),
                CompanyTestProfile.TANK_DIVISION_147_PROFILE.getCompanyId());

        AARCampaignUpdateTabulator campaignUpdateTabulator = new AARCampaignUpdateTabulator(campaign, aarContext);
        CampaignUpdateData campaignUpdateData = campaignUpdateTabulator.tabulateAARForCampaignUpdate();

        campaignUpdateData.getResupplyData().getCompanyTransferData().addTransferRecord(transferRecord);

        PersonnelUpdater personellUpdater = new PersonnelUpdater(campaign, campaignUpdateData);
        personellUpdater.personnelUpdates();

        CrewMember companyMemberAfterUpdate = campaign.getPersonnelManager().getAnyCampaignMember(transferredCrewMember.getSerialNumber());
        Assertions.assertTrue(companyMemberAfterUpdate.getCrewMemberActiveStatus() == CrewMemberStatus.STATUS_ACTIVE);

        Assertions.assertTrue(companyMemberAfterUpdate.getCompanyId() != CompanyTestProfile.THIRD_DIVISION_PROFILE.getCompanyId());
    }
}
