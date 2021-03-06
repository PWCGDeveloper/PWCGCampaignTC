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
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogUnknown;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.aar.inmission.phase3.reconcile.victories.common.ConfirmedVictories;
import pwcg.aar.inmission.phase3.reconcile.victories.common.VictorySorter;
import pwcg.aar.prelim.AARPreliminaryData;
import pwcg.aar.prelim.PwcgMissionDataEvaluator;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.Country;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CompanyTestProfile;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AiDeclarationResolutionRandomTest
{
    @Mock private List<LogVictory> confirmedAiVictories = new ArrayList<LogVictory>();
    @Mock private AARMissionEvaluationData evaluationData;
    @Mock private Campaign campaign;
    @Mock private Company company;
    @Mock private CampaignPersonnelManager personnelManager;
    @Mock private AARContext aarContext;
    @Mock private AARPreliminaryData preliminaryData;
    @Mock private PwcgMissionDataEvaluator pwcgMissionDataEvaluator;
    @Mock private VictorySorter victorySorter;
    @Mock private CrewMember player;
    @Mock private Company playerCompany;
    @Mock private CrewMember aiSquadMember;
            
    private CrewMembers campaignMembersInmission = new CrewMembers();

    private List<LogVictory> randomVictories = new ArrayList<>();        
    private List<LogVictory> emptyList = new ArrayList<>();        
    private List<CrewMember> players = new ArrayList<>();

    private LogTank playerVictor = new LogTank(1);
    private LogTank aiVictor = new LogTank(2);

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        
        

        randomVictories.clear();
        campaignMembersInmission.clear();

        playerVictor.setCrewMemberSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);        
        aiVictor.setCrewMemberSerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1);

        players = new ArrayList<>();
        players.add(player);

        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1000, UnknownVictoryAssignments.RANDOM_ASSIGNMENT);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1001, UnknownVictoryAssignments.UNKNOWN_ASSIGNMENT);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1002, UnknownVictoryAssignments.RANDOM_ASSIGNMENT);
        
        Mockito.when(victorySorter.getFirmTankVictories()).thenReturn(emptyList);
        Mockito.when(victorySorter.getFirmPlaneVictories()).thenReturn(emptyList);
        Mockito.when(victorySorter.getFuzzyTankVictories()).thenReturn(emptyList);
        Mockito.when(victorySorter.getAllUnconfirmed()).thenReturn(randomVictories);
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420801"));

        Mockito.when(player.getSerialNumber()).thenReturn(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        Mockito.when(aiSquadMember.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1);

        ICountry victorCountry = CountryFactory.makeCountryByCountry(Country.GERMANY);
        Mockito.when(aiSquadMember.determineCountry(campaign.getDate())).thenReturn(victorCountry);
        
        Mockito.when(aarContext.getMissionEvaluationData()).thenReturn(evaluationData);
        Mockito.when(aarContext.getPreliminaryData()).thenReturn(preliminaryData);
        Mockito.when(preliminaryData.getCampaignMembersInMission()).thenReturn(campaignMembersInmission);
        List<Company> playerCompanysInMission = new ArrayList<>();
        playerCompanysInMission.add(playerCompany);
        Mockito.when(preliminaryData.getPlayerCompanysInMission()).thenReturn(playerCompanysInMission);

        int companyId = CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId();
        Mockito.when(playerCompany.getCompanyId()).thenReturn(companyId);
        playerVictor.setCompanyId(companyId);
        aiVictor.setCompanyId(companyId);
        Mockito.when(aiSquadMember.getCompanyId()).thenReturn(companyId);
    }

    private void createVictory(Integer victimSerialNumber, UnknownVictoryAssignments unknownVictoryAssignment)
    {        
        LogTank victim = new LogTank(3);
        victim.setCrewMemberSerialNumber(victimSerialNumber);
        
        ICountry victimCountry = CountryFactory.makeCountryByCountry(Country.RUSSIA);
        victim.setCountry(victimCountry);
        
        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictim(victim);

        LogUnknown unknownVictor = new LogUnknown();
        unknownVictor.setUnknownVictoryAssignment(unknownVictoryAssignment);
        resultVictory.setVictor(unknownVictor);
        randomVictories.add(resultVictory);
    }
    
    @Test
    public void testAiRandomVictoryAward () throws PWCGException
    {   
        campaignMembersInmission.addToCrewMemberCollection(player);
        campaignMembersInmission.addToCrewMemberCollection(aiSquadMember);


        Mockito.when(evaluationData.getTankInMissionBySerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1)).thenReturn(aiVictor);
        
        AiDeclarationResolver declarationResolution = new AiDeclarationResolver(campaign, aarContext);
        ConfirmedVictories confirmedAiVictories = declarationResolution.determineAiTankResults(victorySorter);
        
        Assertions.assertTrue (confirmedAiVictories.getConfirmedVictories().size() == 1);
    }

    @Test
    public void testAiRandomVictoryAwardFailedBecusePlaneNotFound () throws PWCGException
    {   
        campaignMembersInmission.addToCrewMemberCollection(player);

        Mockito.when(player.getSerialNumber()).thenReturn(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);

        
        AiDeclarationResolver declarationResolution = new AiDeclarationResolver(campaign, aarContext);
        ConfirmedVictories confirmedAiVictories = declarationResolution.determineAiTankResults(victorySorter);
        
        Assertions.assertTrue (confirmedAiVictories.getConfirmedVictories().size() == 0);
    }

}
