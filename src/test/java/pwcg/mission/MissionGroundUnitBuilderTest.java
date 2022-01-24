package pwcg.mission;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.mission.ground.builder.MissionBattleBuilder;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MissionGroundUnitBuilderTest
{
    private Campaign campaign;
    
    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        
        campaign = CampaignCache.makeCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);
    }

    @Test
    public void createBattle() throws PWCGException
    {
        MissionHumanParticipants participatingPlayers = TestMissionBuilderUtility.buildTestParticipatingHumans(campaign);
        CoordinateBox missionBorders = CoordinateBox.coordinateBoxFromCenter(new Coordinate(100000.0, 0.0, 100000.0), 75000);
        
        Mission mission = TestMissionBuilderUtility.createTestMission(campaign, participatingPlayers, missionBorders);
        mission.generate();
  
        MissionBattleBuilder battleBuilder = new MissionBattleBuilder(mission);
        GroundUnitCollection battle = battleBuilder.generateBattle();
        Assertions.assertTrue (battle.getGroundUnitsForSide(Side.ALLIED).size() > 0);
        Assertions.assertTrue (battle.getGroundUnitsForSide(Side.AXIS).size() > 0);
    }
}
