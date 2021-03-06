package pwcg.aar.inmission.phase3.reconcile.victories.singleplayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.aar.inmission.phase2.logeval.AARMissionEvaluationData;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTank;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.aar.inmission.phase3.reconcile.victories.common.ConfirmedVictories;
import pwcg.aar.inmission.phase3.reconcile.victories.common.VictorySorter;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignData;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.context.Country;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.core.exception.PWCGException;
import pwcg.product.bos.country.BoSCountry;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PlayerDeclarationResolutionFirmVictoryTest
{
    @Mock private Campaign campaign;
    @Mock private CampaignData campaignData;
    @Mock private CampaignPersonnelManager personnelManager;
    @Mock private PlayerVictoryDeclaration mockPlayerDeclaration;
    @Mock private AARMissionEvaluationData evaluationData;
    @Mock private VictorySorter victorySorter;
    @Mock private CrewMembers playerMembers;
    @Mock private CrewMember player;
    @Mock private CrewMember ai;
    
    private Map<Integer, PlayerDeclarations> playerDeclarations = new HashMap<>();
    private PlayerDeclarations playerDeclarationSet;

    private List<LogVictory> firmVictories = new ArrayList<>();        
    private List<LogVictory> emptyList = new ArrayList<>();        
    private List<CrewMember> players = new ArrayList<>();

    private LogTank playerVictor = new LogTank(1);
    private LogTank aiVictor = new LogTank(2);
    
    @BeforeEach
    public void setupTest() throws PWCGException
    {
        
        
        firmVictories.clear();
        
        Mockito.when(campaign.getPersonnelManager()).thenReturn(personnelManager);

        playerVictor.setCrewMemberSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        playerVictor.setCountry(new BoSCountry(Country.USA));
        
        aiVictor.setCrewMemberSerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1);
        aiVictor.setCountry(new BoSCountry(Country.USA));
        
        createMocks();
    }
    
    private void createMocks() throws PWCGException
    {
        players = new ArrayList<>();
        players.add(player);

        Mockito.when(victorySorter.getFirmTankVictories()).thenReturn(firmVictories);
        Mockito.when(victorySorter.getFirmPlaneVictories()).thenReturn(emptyList);
        Mockito.when(victorySorter.getFuzzyTankVictories()).thenReturn(emptyList);
        Mockito.when(campaign.getPersonnelManager()).thenReturn(personnelManager);

        Mockito.when(personnelManager.getAnyCampaignMember(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER)).thenReturn(player);
        Mockito.when(personnelManager.getAnyCampaignMember(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1)).thenReturn(ai);

        Mockito.when(evaluationData.getTankInMissionBySerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER)).thenReturn(playerVictor);

        Mockito.when(player.isPlayer()).thenReturn(true);
        Mockito.when(player.getCountry()).thenReturn(Country.USA);
        Mockito.when(player.getSerialNumber()).thenReturn(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
    }
    
    @Test
    public void testPlayerFirmVictoryAward () throws PWCGException
    {   
        
        createPlayerDeclarations(1);
        createVictory(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1001);
        createVictory(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1002);
        createVictory(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1003);

        PlayerDeclarationResolution declarationResolution = new PlayerDeclarationResolution(campaign, evaluationData, victorySorter, playerDeclarations);
        ConfirmedVictories confirmedPlayerVictories = declarationResolution.determinePlayerResultsWithClaims();
        
        Assertions.assertTrue (confirmedPlayerVictories.getConfirmedVictories().size() == 1);
    }
    
    @Test
    public void testPlayerMultipleFirmVictoryAwards () throws PWCGException
    {   
        
        createPlayerDeclarations(2);
        createVictory(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1001);
        createVictory(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1002);
        createVictory(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1003);

        PlayerDeclarationResolution declarationResolution = new PlayerDeclarationResolution(campaign, evaluationData, victorySorter, playerDeclarations);
        ConfirmedVictories confirmedPlayerVictories = declarationResolution.determinePlayerResultsWithClaims();
        
        Assertions.assertTrue (confirmedPlayerVictories.getConfirmedVictories().size() == 2);
    }
    
    @Test
    public void testPlayerOneAwardForTwoDeclarationsButOnlyOneVictory () throws PWCGException
    {   
        
        createPlayerDeclarations(2);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1001);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1002);
        createVictory(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1003);

        PlayerDeclarationResolution declarationResolution = new PlayerDeclarationResolution(campaign, evaluationData, victorySorter, playerDeclarations);
        ConfirmedVictories confirmedPlayerVictories = declarationResolution.determinePlayerResultsWithClaims();
        
        Assertions.assertTrue (confirmedPlayerVictories.getConfirmedVictories().size() == 1);
    }
    
    @Test
    public void testPlayerZeroAwardForTwoDeclarationsButNoVictories () throws PWCGException
    {   
        createPlayerDeclarations(2);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1001);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1002);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1003);

        PlayerDeclarationResolution declarationResolution = new PlayerDeclarationResolution(campaign, evaluationData, victorySorter, playerDeclarations);
        ConfirmedVictories confirmedPlayerVictories = declarationResolution.determinePlayerResultsWithClaims();
        
        Assertions.assertTrue (confirmedPlayerVictories.getConfirmedVictories().size() == 0);
    }
    
    @Test
    public void testPlayerMoreDeclarationsThanVictories () throws PWCGException
    {   
        createPlayerDeclarations(2);
        PlayerDeclarationResolution declarationResolution = new PlayerDeclarationResolution(campaign, evaluationData, victorySorter, playerDeclarations);
        ConfirmedVictories confirmedPlayerVictories = declarationResolution.determinePlayerResultsWithClaims();
        
        Assertions.assertTrue (confirmedPlayerVictories.getConfirmedVictories().size() == 0);
    }
    
    @Test
    public void testNoFriendlyVictories () throws PWCGException
    {   
        
        createPlayerDeclarations(1);
        createFriendlyVictory(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1001);

        PlayerDeclarationResolution declarationResolution = new PlayerDeclarationResolution(campaign, evaluationData, victorySorter, playerDeclarations);
        ConfirmedVictories confirmedPlayerVictories = declarationResolution.determinePlayerResultsWithClaims();
        
        Assertions.assertTrue (confirmedPlayerVictories.getConfirmedVictories().size() == 0);
    }


    private void createVictory(Integer victorSerialNumber, Integer victimSerialNumber)
    {        
        LogTank victim = new LogTank(3);
        victim.setCrewMemberSerialNumber(victimSerialNumber);
        victim.setVehicleType("pziv-g");
        victim.setCountry(new BoSCountry(Country.GERMANY));

        LogTank victor = new LogTank(4);
        victor.setVehicleType("m4a2");
        victor.setCrewMemberSerialNumber(victorSerialNumber);
        victor.setCountry(new BoSCountry(Country.USA));

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictor(victor);
        resultVictory.setVictim(victim);
        
        firmVictories.add(resultVictory);
    }

    private void createFriendlyVictory(Integer victorSerialNumber, Integer victimSerialNumber)
    {        
        LogTank victim = new LogTank(3);
        victim.setCrewMemberSerialNumber(victimSerialNumber);
        victim.setVehicleType("m4a2");
        victim.setCountry(new BoSCountry(Country.USA));

        LogTank victor = new LogTank(4);
        victor.setVehicleType("m4a2");
        victor.setCrewMemberSerialNumber(victorSerialNumber);
        victor.setCountry(new BoSCountry(Country.USA));

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictor(victor);
        resultVictory.setVictim(victim);
        
        firmVictories.add(resultVictory);
    }

    private void createPlayerDeclarations(int numDeclarations) throws PWCGException
    {
        playerDeclarationSet = new PlayerDeclarations();
        for (int i = 0; i < numDeclarations; ++i)
        {
            PlayerVictoryDeclaration declaration = new PlayerVictoryDeclaration();
            declaration.confirmDeclaration(true, "pziv-g");
            playerDeclarationSet.addDeclaration(declaration);
        }        
        
        playerDeclarations.clear();
        playerDeclarations.put(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, playerDeclarationSet);
    }

}
