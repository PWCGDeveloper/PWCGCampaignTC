package pwcg.mission;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.ground.AAAManager;
import pwcg.mission.ground.MissionBattleBuilder;
import pwcg.mission.ground.MissionTrainBuilder;
import pwcg.mission.ground.MissionTruckConvoyBuilder;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;
import pwcg.testutils.TestMissionFlightTypeBuilder;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MissionGroundUnitBuilderTest
{
    private Campaign campaign;
    
    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        
        campaign = CampaignCache.makeCampaign(CompanyTestProfile.STG77_PROFILE);
    }

    @Test
    public void createBattle() throws PWCGException
    {
        MissionHumanParticipants participatingPlayers = TestMissionBuilderUtility.buildTestParticipatingHumans(campaign);
        CoordinateBox missionBorders = CoordinateBox.coordinateBoxFromCenter(new Coordinate(100000.0, 0.0, 100000.0), 75000);
        
        MissionCompanyFlightTypes playerFlightTypes = TestMissionFlightTypeBuilder.buildFlightType(campaign, FlightTypes.DIVE_BOMB);
        Mission mission = TestMissionBuilderUtility.createTestMission(campaign, participatingPlayers, missionBorders, MissionProfile.DAY_TACTICAL_MISSION);
        mission.generate(playerFlightTypes);
  
        MissionBattleBuilder battleBuilder = new MissionBattleBuilder(mission);
        List<GroundUnitCollection> battles = battleBuilder.generateBattle();

        Assertions.assertTrue (battles.size() < 3);
        for (GroundUnitCollection battle : battles)
        {
            Assertions.assertTrue (battle.getGroundUnitsForSide(Side.ALLIED).size() > 0);
            Assertions.assertTrue (battle.getGroundUnitsForSide(Side.AXIS).size() > 0);
        }
    }

    @Test
    public void createTrucks() throws PWCGException
    {
        MissionHumanParticipants participatingPlayers = TestMissionBuilderUtility.buildTestParticipatingHumans(campaign);
        CoordinateBox missionBorders = CoordinateBox.coordinateBoxFromCenter(new Coordinate(100000.0, 0.0, 100000.0), 75000);

        MissionCompanyFlightTypes playerFlightTypes = TestMissionFlightTypeBuilder.buildFlightType(campaign, FlightTypes.DIVE_BOMB);
        Mission mission = TestMissionBuilderUtility.createTestMission(campaign, participatingPlayers, missionBorders, MissionProfile.DAY_TACTICAL_MISSION);
        mission.generate(playerFlightTypes);

        MissionTruckConvoyBuilder truckConvoyBuilder = new MissionTruckConvoyBuilder(campaign, mission);
        List<GroundUnitCollection> trucks = truckConvoyBuilder.generateMissionTrucks();

        Assertions.assertTrue (trucks.size() >= 2);
        Assertions.assertTrue (trucks.size() <= 12);

        boolean alliedTrainFound = false;
        boolean axisTrainFound = false;
        for (GroundUnitCollection truckUnit : trucks)
        {
            for (IGroundUnit groundUnit : truckUnit.getGroundUnits())
            {
                if (groundUnit.getCountry().getSide() == Side.ALLIED)
                {
                    alliedTrainFound = true;
                }
                else
                {
                    axisTrainFound = true;
                }
                Assertions.assertTrue (groundUnit.getVehicles().size() >= 1);
            }
        }
        Assertions.assertTrue (alliedTrainFound);
        Assertions.assertTrue (axisTrainFound);
    }
}
