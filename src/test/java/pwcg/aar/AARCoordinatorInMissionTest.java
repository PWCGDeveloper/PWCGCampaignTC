package pwcg.aar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.PlayerDeclarations;
import pwcg.aar.prelim.AARPreliminaryData;
import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.plane.CompanyPlaneAssignment;
import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.LogEventData;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AARCoordinatorInMissionTest
{
    private Campaign campaign;    
    private static AARCoordinator aarCoordinator;
    private static ExpectedResults expectedResults;
    private static int playerMissionsFlown = 0;

    private List<Company> companysInMission = new ArrayList<>();
    private Map<Integer, PlayerDeclarations> playerDeclarations;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.MOSCOW_MAP);
        campaign = CampaignCache.makeCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);
        expectedResults = new ExpectedResults(campaign);
        aarCoordinator = AARCoordinator.getInstance();
        aarCoordinator.reset(campaign);
        
        playerMissionsFlown = campaign.getPersonnelManager().getPlayersInMission().getCrewMemberList().get(0).getBattlesFought();
    }

    @Test
    public void runMissionAAR () throws PWCGException
    {
        createArtifacts ();
        aarCoordinator.performMissionAAR(playerDeclarations);
        expectedResults.buildExpectedResultsFromAARContext(aarCoordinator.getAarContext());
        
        AARResultValidator resultValidator = new AARResultValidator(expectedResults);
        resultValidator.validateInMission(playerMissionsFlown, 2);
    }

    public void createArtifacts () throws PWCGException
    {               
        makeCompanysInMission();
        makePreliminary();
        makePlayerDeclarations();
        makeMissionLogEvents();
    }

    private void makeCompanysInMission() throws PWCGException
    {
        CompanyForMissionBuilder companyForMissionBuilder = new CompanyForMissionBuilder(campaign);
        companysInMission = companyForMissionBuilder.makeCompanysInMission();
    }

    private void makePreliminary() throws PWCGException
    {
        TestPreliminaryDataBuilder preliminaryDataBuilder = new TestPreliminaryDataBuilder(campaign, companysInMission);
        AARPreliminaryData preliminaryData = preliminaryDataBuilder.makePreliminaryForTestMission();
        aarCoordinator.getAarContext().setPreliminaryData(preliminaryData);
    }

    private void makePlayerDeclarations() throws PWCGException
    {
        CrewMember player = campaign.getPersonnelManager().getPlayersInMission().getCrewMemberList().get(0);
        PlayerDeclarationsBuilder  declarationsBuilder = new PlayerDeclarationsBuilder();
        playerDeclarations = declarationsBuilder.makePlayerDeclarations(player);
    }

    private void makeMissionLogEvents() throws PWCGException
    {
        MissionLogEventsBuilder missionLogEventsBuilder = new MissionLogEventsBuilder(campaign, 
                aarCoordinator.getAarContext().getPreliminaryData(), expectedResults);
        LogEventData missionLogRawData = missionLogEventsBuilder.makeLogEvents();
        aarCoordinator.getAarContext().setLogEventData(missionLogRawData);
    }
    
    public static CompanyTankAssignment getPlaneForCompany(int CompanyId) throws PWCGException
    {
        Company company = PWCGContext.getInstance().getCompanyManager().getCompany(CompanyId);
        List<CompanyTankAssignment> companyPlaneAssignments = company.getPlaneAssignments();
        return companyPlaneAssignments.get(0);
    }

}
