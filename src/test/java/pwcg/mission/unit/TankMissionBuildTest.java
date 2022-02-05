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
        
        double width = (easternBoundary - westernBoundary) / 1000;
        double height = (northernBoundary - southernBoundary) / 1000;
        double area = width* height;
 	}
}
