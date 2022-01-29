package pwcg.aar.inmission.phase3.reconcile.victories.singleplayer;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.aar.data.AARContext;
import pwcg.aar.inmission.phase2.logeval.AARMissionEvaluationData;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTank;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.aar.inmission.phase3.reconcile.victories.common.ConfirmedVictories;
import pwcg.aar.inmission.phase3.reconcile.victories.common.VictorySorter;
import pwcg.aar.prelim.PwcgMissionDataEvaluator;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignData;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CompanyTestProfile;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AiDeclarationResolutionFirmTest
{
    private static final String PLAYER_NAME = "CrewMember Player";

    @Mock private Campaign campaign;
    @Mock private CampaignPersonnelManager personnelManager;
    @Mock private CrewMembers playerMembers;

    @Mock private List<LogVictory> confirmedAiVictories = new ArrayList<LogVictory>();
    @Mock private AARMissionEvaluationData evaluationData;
    @Mock private CampaignData campaignData;
    @Mock private AARContext aarContext;
    @Mock private PwcgMissionDataEvaluator pwcgMissionDataEvaluator;
    @Mock private VictorySorter victorySorter;
    @Mock private CrewMember player;
    @Mock private CrewMember ai;
        
    private List<LogVictory> firmVictories = new ArrayList<>();        
    private List<LogVictory> emptyList = new ArrayList<>();        
    private List<CrewMember> players = new ArrayList<>();

    private LogTank playerTankVictor = new LogTank(1);
    private LogTank aiMemberTankVictor1 = new LogTank(2);
    
    private int aiCrewmemberSerialNumber = SerialNumber.AI_STARTING_SERIAL_NUMBER + 1;

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        firmVictories.clear();
        
        Mockito.when(aarContext.getMissionEvaluationData()).thenReturn(evaluationData);
        Mockito.when(campaign.getPersonnelManager()).thenReturn(personnelManager);   

        playerTankVictor.setCompanyId(CompanyTestProfile.THIRD_DIVISION_PROFILE.getCompanyId());
        playerTankVictor.setCrewMemberSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        playerTankVictor.setTankSerialNumber(SerialNumber.TANK_STARTING_SERIAL_NUMBER + 1);

        aiMemberTankVictor1.setCompanyId(CompanyTestProfile.THIRD_DIVISION_PROFILE.getCompanyId());
        aiMemberTankVictor1.setCrewMemberSerialNumber(aiCrewmemberSerialNumber);
        aiMemberTankVictor1.setTankSerialNumber(SerialNumber.TANK_STARTING_SERIAL_NUMBER + 2);

        createVictory(playerTankVictor);
        createVictory(aiMemberTankVictor1);
        createVictory(aiMemberTankVictor1);
    }

    private void createVictory(LogTank victor) throws PWCGException
    {        
        LogTank victim = new LogTank(3);
        victim.setTankSerialNumber(SerialNumber.TANK_STARTING_SERIAL_NUMBER + 100);
        
        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictor(victor);
        resultVictory.setVictim(victim);
        firmVictories.add(resultVictory);
        
        players = new ArrayList<>();
        players.add(player);

        Mockito.when(victorySorter.getFirmTankVictories()).thenReturn(firmVictories);
        Mockito.when(victorySorter.getFirmPlaneVictories()).thenReturn(emptyList);
        Mockito.when(victorySorter.getFuzzyTankVictories()).thenReturn(emptyList);
        Mockito.when(victorySorter.getAllUnconfirmed()).thenReturn(emptyList);
        Mockito.when(campaign.getCampaignData()).thenReturn(campaignData);
        Mockito.when(campaign.getPersonnelManager()).thenReturn(personnelManager);
        Mockito.when(campaignData.getName()).thenReturn(PLAYER_NAME);

        Mockito.when(player.isPlayer()).thenReturn(true);
        Mockito.when(player.getSerialNumber()).thenReturn(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        Mockito.when(ai.getSerialNumber()).thenReturn(aiCrewmemberSerialNumber);
    }
    
    @Test
    public void testAiFirmVictoryAward () throws PWCGException
    {   
        Mockito.when(personnelManager.getAnyCampaignMember(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER)).thenReturn(player);
        Mockito.when(personnelManager.getAnyCampaignMember(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER)).thenReturn(player);

        Mockito.when(evaluationData.getTankInMissionBySerialNumber(aiCrewmemberSerialNumber)).thenReturn(aiMemberTankVictor1);
        Mockito.when(personnelManager.getAnyCampaignMember(aiCrewmemberSerialNumber)).thenReturn(ai);
        Mockito.when(personnelManager.getAnyCampaignMember(aiCrewmemberSerialNumber)).thenReturn(ai);
        
        AiDeclarationResolver declarationResolution = new AiDeclarationResolver(campaign, aarContext);
        ConfirmedVictories confirmedAiVictories = declarationResolution.determineAiTankResults(victorySorter);
        
        Assertions.assertTrue (confirmedAiVictories.getConfirmedVictories().size() == 2);
    }
    
    @Test
    public void testAiFirmBalloonAward () throws PWCGException
    {   
        Mockito.when(personnelManager.getAnyCampaignMember(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER)).thenReturn(player);
        Mockito.when(personnelManager.getAnyCampaignMember(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER)).thenReturn(player);

        Mockito.when(evaluationData.getTankInMissionBySerialNumber(aiCrewmemberSerialNumber)).thenReturn(aiMemberTankVictor1);
        Mockito.when(personnelManager.getAnyCampaignMember(aiCrewmemberSerialNumber)).thenReturn(ai);
        Mockito.when(personnelManager.getAnyCampaignMember(aiCrewmemberSerialNumber)).thenReturn(ai);
        
        Mockito.when(victorySorter.getFirmTankVictories()).thenReturn(emptyList);
        Mockito.when(victorySorter.getFirmPlaneVictories()).thenReturn(firmVictories);

        AiDeclarationResolver declarationResolution = new AiDeclarationResolver(campaign, aarContext);
        ConfirmedVictories confirmedAiVictories = declarationResolution.determineAiTankResults(victorySorter);
        
        Assertions.assertTrue (confirmedAiVictories.getConfirmedVictories().size() == 2);
    }

    @Test
    public void testAiFirmVictoryAwardFailedBecusePlaneNotFound () throws PWCGException
    {   
        Mockito.when(player.getSerialNumber()).thenReturn(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        Mockito.when(personnelManager.getAnyCampaignMember(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER)).thenReturn(player);
        Mockito.when(personnelManager.getAnyCampaignMember(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER)).thenReturn(player);

        Mockito.when(personnelManager.getAnyCampaignMember(aiCrewmemberSerialNumber)).thenReturn(null);
        
        AiDeclarationResolver declarationResolution = new AiDeclarationResolver(campaign, aarContext);
        ConfirmedVictories confirmedAiVictories = declarationResolution.determineAiTankResults(victorySorter);
        
        Assertions.assertTrue (confirmedAiVictories.getConfirmedVictories().size() == 0);
    }

}
