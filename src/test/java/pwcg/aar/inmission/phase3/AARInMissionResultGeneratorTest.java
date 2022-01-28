package pwcg.aar.inmission.phase3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.aar.data.AARContext;
import pwcg.aar.inmission.phase2.logeval.AARMissionEvaluationData;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogCrewMember;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTank;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.PlayerDeclarations;
import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.PlayerVictoryDeclaration;
import pwcg.aar.prelim.AARPreliminaryData;
import pwcg.aar.prelim.PwcgMissionData;
import pwcg.aar.ui.events.model.ClaimDeniedEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.Country;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.crewmember.Victory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.logfiles.LogFileSet;
import pwcg.product.bos.country.BoSCountry;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CampaignPersonnelTestHelper;
import pwcg.testutils.CompanyTestProfile;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AARInMissionResultGeneratorTest
{
    private Campaign campaign;
    private List<LogCrewMember> crewMemberStatusList;
    private List<LogVictory> firmVictories;        
    private CrewMember corporalInPlatoon;
    private CrewMember sergeantInPlatoon;
    private CrewMember secondLtInPlatoon;
    private CrewMember firstLtInPlatoon;
    private LogTank playerPlaneVictor = new LogTank(1);
    private LogTank aiPlaneVictor = new LogTank(2);
    private LogTank wernerVossPlaneVictor = new LogTank(3);
    private LogTank gerogesGuynemerPlaneVictor = new LogTank(4);

    @Mock private AARMissionEvaluationData evaluationData;
    @Mock private LogFileSet missionLogFileSet;
    @Mock private AARPreliminaryData preliminaryData;
    @Mock private PwcgMissionData pwcgMissionData;

    private Map<Integer, PlayerDeclarations> playerDeclarations = new HashMap<>();
    private PlayerDeclarations playerDeclarationSet;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        
        campaign = CampaignCache.makeCampaign(CompanyTestProfile.THIRD_DIVISION_PROFILE);
        
    }

    @BeforeEach
    public void setupTest() throws PWCGException
    {        
        crewMemberStatusList = new ArrayList<>();
        firmVictories = new ArrayList<>();
        playerDeclarationSet = new PlayerDeclarations();

        playerPlaneVictor.setCompanyId(CompanyTestProfile.THIRD_DIVISION_PROFILE.getCompanyId());
        aiPlaneVictor.setCompanyId(CompanyTestProfile.THIRD_DIVISION_PROFILE.getCompanyId());
        wernerVossPlaneVictor.setCompanyId(401010);
        gerogesGuynemerPlaneVictor.setCompanyId(CompanyTestProfile.THIRD_DIVISION_PROFILE.getCompanyId());
        
        Mockito.when(evaluationData.getCrewMembersInMission()).thenReturn(crewMemberStatusList);
        Mockito.when(evaluationData.getVictoryResults()).thenReturn(firmVictories);   
        
        createCampaignMembersInMission();
        Mockito.when(evaluationData.getTankInMissionBySerialNumber(playerPlaneVictor.getCrewMemberSerialNumber())).thenReturn(playerPlaneVictor);   
        Mockito.when(evaluationData.getTankInMissionBySerialNumber(aiPlaneVictor.getCrewMemberSerialNumber())).thenReturn(aiPlaneVictor);   
    }

    @Test
    public void testMixedToVerifyDataTransfer() throws PWCGException
    {
        addPlayerDeclarations();
        
        createVictory(playerPlaneVictor, SerialNumber.AI_STARTING_SERIAL_NUMBER + 100, SerialNumber.TANK_STARTING_SERIAL_NUMBER + 100);
        createVictory(aiPlaneVictor, SerialNumber.AI_STARTING_SERIAL_NUMBER + 101, SerialNumber.TANK_STARTING_SERIAL_NUMBER + 101);
        createVictory(aiPlaneVictor, SerialNumber.AI_STARTING_SERIAL_NUMBER + 102, SerialNumber.TANK_STARTING_SERIAL_NUMBER + 102);
        
        AARContext aarContext = new AARContext(campaign);
        aarContext.setMissionEvaluationData(evaluationData);
        
        AARInMissionResultGenerator coordinatorInMission = new AARInMissionResultGenerator(campaign, aarContext);
        coordinatorInMission.generateInMissionResults(playerDeclarations);
        
        assert(aarContext.getPersonnelLosses().getPersonnelKilled().size() == 3);
        assert(aarContext.getPersonnelLosses().getPersonnelCaptured().size() == 1);
        assert(aarContext.getPersonnelLosses().getPersonnelMaimed().size() == 1);
        assert(aarContext.getPersonnelLosses().getPersonnelWounded().size() == 2);
        assert(aarContext.getPersonnelLosses().getAcesKilled(campaign).size() == 2);

        List<Victory> aiCrewMemberVictories = aarContext.getPersonnelAcheivements().getVictoryAwardsForCrewMember(aiPlaneVictor.getCrewMemberSerialNumber());
        List<Victory> playerVictories = aarContext.getPersonnelAcheivements().getVictoryAwardsForCrewMember(playerPlaneVictor.getCrewMemberSerialNumber());
        List<ClaimDeniedEvent> playerClaimsDenied = aarContext.getPersonnelAcheivements().getPlayerClaimsDenied();
        Assertions.assertTrue (aiCrewMemberVictories.size() == 3);
        Assertions.assertTrue (playerVictories.size() == 2);
        Assertions.assertTrue (playerClaimsDenied.size() == 2);
    }
    
    private void addPlayerDeclarations() throws PWCGException
    {
        for (int i = 0; i < 4; ++i)
        {
            PlayerVictoryDeclaration declaration = new PlayerVictoryDeclaration();
            declaration.setTankType("pziv-g");
            playerDeclarationSet.addDeclaration(declaration);
        }
        
        CrewMember playerInPlatoon = campaign.findReferencePlayer();
        playerDeclarations.put(playerInPlatoon.getSerialNumber(), playerDeclarationSet);
    }

    private void createVictory(LogTank victor, Integer crewMemberSerialNumber, Integer tankSerialNumber)
    {
        LogTank victim = new LogTank(3);
        victim.setVehicleType("pziv-g");
        victim.setCountry(new BoSCountry(Country.GERMANY));

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setLocation(new Coordinate(100.0, 0.0, 100.0));
        resultVictory.setVictor(victor);
        resultVictory.setVictim(victim);
        firmVictories.add(resultVictory);
    }

    private void createCampaignMembersInMission() throws PWCGException
    {        
        CrewMember playerInPlatoon = campaign.findReferencePlayer();
        addCompanyCrewMember(playerInPlatoon.getSerialNumber(), CrewMemberStatus.STATUS_WOUNDED);
        playerPlaneVictor.setCrewMemberSerialNumber(playerInPlatoon.getSerialNumber());
        playerPlaneVictor.setCountry(new BoSCountry(Country.FRANCE));
        playerPlaneVictor.setCrewMemberSerialNumber(playerInPlatoon.getSerialNumber());
                
        sergeantInPlatoon = CampaignPersonnelTestHelper.getCrewMemberByRank(campaign, "Sergent");
        aiPlaneVictor.setCountry(new BoSCountry(Country.BRITAIN));

        corporalInPlatoon = CampaignPersonnelTestHelper.getCrewMemberByRank(campaign, "Corporal");
        addCompanyCrewMember(corporalInPlatoon.getSerialNumber(), CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED);
        
        secondLtInPlatoon = CampaignPersonnelTestHelper.getCrewMemberByRank(campaign, "2nd Lieutenant");
        addCompanyCrewMember(secondLtInPlatoon.getSerialNumber(), CrewMemberStatus.STATUS_KIA);
        
        firstLtInPlatoon = CampaignPersonnelTestHelper.getCrewMemberByRank(campaign, "Lieutenant");
        addCompanyCrewMember(firstLtInPlatoon.getSerialNumber(), CrewMemberStatus.STATUS_CAPTURED);
    }
    
    private void addCompanyCrewMember(int serialNumber, int status)
    {
        LogCrewMember companyCrewMember = new LogCrewMember();
        companyCrewMember.setSerialNumber(serialNumber);
        companyCrewMember.setStatus(status);
        crewMemberStatusList.add(companyCrewMember);
    }
}
