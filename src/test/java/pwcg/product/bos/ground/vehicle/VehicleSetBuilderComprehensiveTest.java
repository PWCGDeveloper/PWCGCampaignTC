package pwcg.product.bos.ground.vehicle;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.MissionHumanParticipants;
import pwcg.mission.ground.vehicle.VehicleSetBuilderComprehensive;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VehicleSetBuilderComprehensiveTest
{
    private Campaign campaign;    

    @BeforeAll 
    public void setupSuite() throws PWCGException
    {
        
        campaign = CampaignCache.makeCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);
        campaign.setDate(DateUtils.getDateYYYYMMDD("19411103"));
    }

    @Test
    public void testVehicleCreation() throws PWCGException
    {
        VehicleSetBuilderComprehensive vehicleSetBuilder = new VehicleSetBuilderComprehensive();
        vehicleSetBuilder.makeOneOfEachType();
        Assertions.assertTrue (vehicleSetBuilder.getAllVehicles().size() > 0);
    }

    @Test
    public void patrolFlightTest() throws PWCGException
    {
        MissionHumanParticipants participatingPlayers = TestMissionBuilderUtility.buildTestParticipatingHumans(campaign);
    	MissionGenerator missionGenerator = new MissionGenerator(campaign);
    	Mission mission = missionGenerator.makeTestMissionFromMissionType(participatingPlayers);
        mission.generateAllGroundUnitTypesForTest();
        mission.finalizeMission();
    }

}
