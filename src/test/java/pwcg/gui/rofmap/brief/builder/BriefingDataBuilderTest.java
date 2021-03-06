package pwcg.gui.rofmap.brief.builder;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.gui.rofmap.brief.model.BriefingData;
import pwcg.gui.rofmap.brief.model.BriefingMapPoint;
import pwcg.gui.rofmap.brief.model.BriefingPlatoonParameters;
import pwcg.gui.rofmap.brief.model.IBriefingPlatoon;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.platoon.ITankPlatoon;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BriefingDataBuilderTest
{
    private Campaign campaign;
    private static Mission mission;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {

        campaign = CampaignCache.makeCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);

        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        mission = missionGenerator.makeTestMissionFromMissionType(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
    }

    @Test
    public void testBriefingBuilder() throws PWCGException
    {
        BriefingDataBuilder briefingDataBuilder = new BriefingDataBuilder(mission);
        BriefingData briefingData = briefingDataBuilder.buildBriefingData();
        assert(briefingData != null);
        assert(briefingData.getActiveBriefingMapPlatoon() != null);
        assert(briefingData.getActivePlayerBriefingPlatoon().getBriefingAssignmentData().getCrews().size() > 0);

        ITankPlatoon unit = briefingData.getSelectedPlayerPlatoon();
        Assertions.assertTrue (unit.getCompany().getCompanyId() == CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId());

        IBriefingPlatoon briefingPlatoon = briefingData.getActiveBriefingMapPlatoon();
        Assertions.assertTrue (briefingPlatoon.getCompanyId() == CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId());

        BriefingPlatoonParameters briefingPlatoonParameters = briefingPlatoon.getBriefingPlatoonParameters();
        List<BriefingMapPoint>  briefingMapMapPoints = briefingPlatoonParameters.getBriefingMapMapPoints();
        for (BriefingMapPoint briefingMapMapPoint : briefingMapMapPoints)
        {
            assert(briefingMapMapPoint.getDesc() != null);
            assert(!briefingMapMapPoint.getDesc().isEmpty());
        }

    }
}
