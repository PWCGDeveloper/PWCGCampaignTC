package pwcg.mission.battle;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.mcu.group.virtual.VirtualWaypointPackageValidator;
import pwcg.mission.platoon.ITankPlatoon;
import pwcg.mission.platoon.tank.TankMcu;
import pwcg.mission.target.TargetCategory;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetType;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PlayerMissionTypeAssaultTest
{
    private Campaign campaign;    
    
    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        
        campaign = CampaignCache.makeCampaign(CompanyTestProfile.TANK_DIVISION_147_PROFILE);
    }

    @Test
    public void assaultMissionTypeTest() throws PWCGException
    {
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeTestMissionFromMissionType(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        ITankPlatoon unit = mission.getPlatoons().getPlayerPlatoons().get(0);
        mission.finalizeMission();

        for (TankMcu tank : unit.getPlatoonTanks().getTanks())
        {
            assert(tank.getTankPayload().getSelectedPayloadId() == 0);
        }
        
        VirtualWaypointPackageValidator virtualWaypointPackageValidator = new VirtualWaypointPackageValidator(mission);
        virtualWaypointPackageValidator.validate();
    }

    public void validateTargetDefinition(TargetDefinition targetDefinition)
    {
        Assertions.assertTrue (targetDefinition.getCountry() != null);
        Assertions.assertTrue (targetDefinition.getTargetCategory() != TargetCategory.TARGET_CATEGORY_NONE);
        Assertions.assertTrue (targetDefinition.getTargetType() != TargetType.TARGET_NONE);
    }
}
