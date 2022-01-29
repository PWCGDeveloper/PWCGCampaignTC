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
import pwcg.campaign.company.Company;
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
    private LogTank playerTankVictor = new LogTank(1);
    private LogTank aiMemberTankVictor1 = new LogTank(2);
    private LogTank aiMemberTankVictor2 = new LogTank(3);
    private LogTank nonMemberTankVictor = new LogTank(4);

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

        playerTankVictor.setCompanyId(CompanyTestProfile.THIRD_DIVISION_PROFILE.getCompanyId());
        playerTankVictor.setCrewMemberSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        playerTankVictor.setTankSerialNumber(SerialNumber.TANK_STARTING_SERIAL_NUMBER + 100);

        aiMemberTankVictor1.setCompanyId(CompanyTestProfile.THIRD_DIVISION_PROFILE.getCompanyId());
        aiMemberTankVictor1.setCrewMemberSerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 101);
        aiMemberTankVictor1.setTankSerialNumber(SerialNumber.TANK_STARTING_SERIAL_NUMBER + 101);

        aiMemberTankVictor2.setCompanyId(CompanyTestProfile.THIRD_DIVISION_PROFILE.getCompanyId());
        aiMemberTankVictor2.setCrewMemberSerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 102);
        aiMemberTankVictor2.setTankSerialNumber(SerialNumber.TANK_STARTING_SERIAL_NUMBER + 102);

        nonMemberTankVictor.setCompanyId(Company.AI);
        nonMemberTankVictor.setCrewMemberSerialNumber(SerialNumber.NO_SERIAL_NUMBER);
        nonMemberTankVictor.setTankSerialNumber(SerialNumber.NO_SERIAL_NUMBER);

        Mockito.when(evaluationData.getCrewMembersInMission()).thenReturn(crewMemberStatusList);
        Mockito.when(evaluationData.getVictoryResults()).thenReturn(firmVictories);   
        
        createCampaignMembersInMission();
        Mockito.when(evaluationData.getTankInMissionBySerialNumber(playerTankVictor.getCrewMemberSerialNumber())).thenReturn(playerTankVictor);   
        Mockito.when(evaluationData.getTankInMissionBySerialNumber(aiMemberTankVictor1.getCrewMemberSerialNumber())).thenReturn(aiMemberTankVictor1);   
        Mockito.when(evaluationData.getTankInMissionBySerialNumber(aiMemberTankVictor2.getCrewMemberSerialNumber())).thenReturn(aiMemberTankVictor2);   
    }

    @Test
    public void testMixedToVerifyDataTransfer() throws PWCGException
    {
        addPlayerDeclarations();
        
        createVictory(playerTankVictor);
        createVictory(aiMemberTankVictor1);
        createVictory(aiMemberTankVictor2);
        createVictory(aiMemberTankVictor2);
        createVictory(nonMemberTankVictor);
        
        AARContext aarContext = new AARContext(campaign);
        aarContext.setMissionEvaluationData(evaluationData);
        
        AARInMissionResultGenerator coordinatorInMission = new AARInMissionResultGenerator(campaign, aarContext);
        coordinatorInMission.generateInMissionResults(playerDeclarations);
        
        assert(aarContext.getPersonnelLosses().getPersonnelKilled().size() == 1);
        assert(aarContext.getPersonnelLosses().getPersonnelCaptured().size() == 1);
        assert(aarContext.getPersonnelLosses().getPersonnelMaimed().size() == 1);
        assert(aarContext.getPersonnelLosses().getPersonnelWounded().size() == 1);
        assert(aarContext.getPersonnelLosses().getAcesKilled(campaign).size() == 0);

        List<Victory> ai1CrewMemberVictories = aarContext.getPersonnelAcheivements().getVictoryAwardsForCrewMember(aiMemberTankVictor1.getCrewMemberSerialNumber());
        List<Victory> ai2CrewMemberVictories = aarContext.getPersonnelAcheivements().getVictoryAwardsForCrewMember(aiMemberTankVictor2.getCrewMemberSerialNumber());
        List<Victory> playerVictories = aarContext.getPersonnelAcheivements().getVictoryAwardsForCrewMember(playerTankVictor.getCrewMemberSerialNumber());
        List<ClaimDeniedEvent> playerClaimsDenied = aarContext.getPersonnelAcheivements().getPlayerClaimsDenied();
        Assertions.assertTrue (ai1CrewMemberVictories.size() == 1);
        Assertions.assertTrue (ai2CrewMemberVictories.size() == 2);
        Assertions.assertTrue (playerVictories.size() == 1);
        Assertions.assertTrue (playerClaimsDenied.size() == 1);
    }
    
    private void addPlayerDeclarations() throws PWCGException
    {
        for (int i = 0; i < 2; ++i)
        {
            PlayerVictoryDeclaration declaration = new PlayerVictoryDeclaration();
            declaration.setTankType("pziv-g");
            playerDeclarationSet.addDeclaration(declaration);
        }
        
        CrewMember playerInPlatoon = campaign.findReferencePlayer();
        playerDeclarations.put(playerInPlatoon.getSerialNumber(), playerDeclarationSet);
    }

    private void createVictory(LogTank victor)
    {
        LogTank victim = new LogTank(3);
        victim.setVehicleType("pziv-g");
        victim.setCountry(new BoSCountry(Country.GERMANY));
        victim.setCompanyId(Company.AI);
        
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
        playerTankVictor.setCrewMemberSerialNumber(playerInPlatoon.getSerialNumber());
        playerTankVictor.setCountry(new BoSCountry(Country.USA));
        playerTankVictor.setCrewMemberSerialNumber(playerInPlatoon.getSerialNumber());
                
        sergeantInPlatoon = CampaignPersonnelTestHelper.getCrewMemberByRank(campaign, "Sergeant");
        addCompanyCrewMember(sergeantInPlatoon.getSerialNumber(), CrewMemberStatus.STATUS_ACTIVE);
        aiMemberTankVictor1.setCountry(new BoSCountry(Country.USA));

        corporalInPlatoon = CampaignPersonnelTestHelper.getCrewMemberByRank(campaign, "Corporal");
        addCompanyCrewMember(corporalInPlatoon.getSerialNumber(), CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED);
        
        secondLtInPlatoon = CampaignPersonnelTestHelper.getCrewMemberByRank(campaign, "2nd Lieutenant");
        addCompanyCrewMember(secondLtInPlatoon.getSerialNumber(), CrewMemberStatus.STATUS_KIA);
        
        firstLtInPlatoon = CampaignPersonnelTestHelper.getCrewMemberByRank(campaign, "1st Lieutenant");
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
