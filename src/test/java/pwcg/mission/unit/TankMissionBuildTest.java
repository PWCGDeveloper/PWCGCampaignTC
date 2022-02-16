package pwcg.mission.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.platoon.ITankPlatoon;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TankMissionBuildTest
{
    private Campaign campaign;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {

        campaign = CampaignCache.makeCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);
    }

    @Test
    public void tankPlatoonMissionCreateAndWriteTest() throws PWCGException
    {
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        mission.finalizeMission();
        mission.write();
        ITankPlatoon unit = mission.getPlatoons().getPlayerPlatoons().get(0);
        Assertions.assertNotNull(unit);

        findTankBoundary(mission);
        findInfantryBoundary(mission);
    }

    private void findTankBoundary(Mission mission)
    {
        double westernBoundary = 1000000000.0;
        double easternBoundary = 0.0;
        double southernBoundary = 1000000000.0;
        double northernBoundary = 0.0;
        for(ITankPlatoon platoon : mission.getPlatoons().getPlatoons())
        {
            Coordinate vehilePosition = platoon.getLeadVehicle().getPosition();
            if (vehilePosition.getZPos() < westernBoundary)
            {
                westernBoundary = vehilePosition.getZPos();
            }
            if (vehilePosition.getZPos() > easternBoundary)
            {
                easternBoundary = vehilePosition.getZPos();
            }
            if (vehilePosition.getXPos() < southernBoundary)
            {
                southernBoundary = vehilePosition.getXPos();
            }
            if (vehilePosition.getXPos() > northernBoundary)
            {
                northernBoundary = vehilePosition.getXPos();
            }
        }

        double tankWidth = (easternBoundary - westernBoundary) / 1000;
        double tankHeight = (northernBoundary - southernBoundary) / 1000;
        double tankArea = tankWidth* tankHeight;
        Assertions.assertTrue(tankArea > 10);
        Assertions.assertTrue(tankArea < 100);
    }

    private void findInfantryBoundary(Mission mission) throws PWCGException
    {
        double westernBoundary = 1000000000.0;
        double easternBoundary = 0.0;
        double southernBoundary = 1000000000.0;
        double northernBoundary = 0.0;
        for(IGroundUnit unit : mission.getGroundUnitBuilder().getAssault().getGroundUnits())
        {
            Coordinate unitPosition = unit.getPosition();
            if (unitPosition.getZPos() < westernBoundary)
            {
                westernBoundary = unitPosition.getZPos();
            }
            if (unitPosition.getZPos() > easternBoundary)
            {
                easternBoundary = unitPosition.getZPos();
            }
            if (unitPosition.getXPos() < southernBoundary)
            {
                southernBoundary = unitPosition.getXPos();
            }
            if (unitPosition.getXPos() > northernBoundary)
            {
                northernBoundary = unitPosition.getXPos();
            }
        }

        double infantryWidth = (easternBoundary - westernBoundary) / 1000;
        double infantryHeight = (northernBoundary - southernBoundary) / 1000;
        double infantryArea = infantryWidth* infantryHeight;
        Assertions.assertTrue(infantryArea > 20);
        Assertions.assertTrue(infantryArea < 200);
    }
}
