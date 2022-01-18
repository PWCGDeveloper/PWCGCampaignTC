package pwcg.mission.battle;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.unit.ITankPlatoon;
import pwcg.mission.unit.PlatoonMissionType;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PlayerMissionTypeCoopTest
{
    private static Map<CompanyTestProfile, Campaign> campaigns = new HashMap<>();

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        
        Campaign germanEastCampaign = CampaignCache.makeCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);
        Campaign germanWestCampaign = CampaignCache.makeCampaign(CompanyTestProfile.PANZER_LEHR_PROFILE);
        Campaign americanCampaign = CampaignCache.makeCampaign(CompanyTestProfile.THIRD_DIVISION_PROFILE);
        Campaign britishCampaign = CampaignCache.makeCampaign(CompanyTestProfile.SEVENTH_DIVISION_PROFILE);
        Campaign russianCampaign = CampaignCache.makeCampaign(CompanyTestProfile.TANK_DIVISION_147_PROFILE);
        
        campaigns.put(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE, germanEastCampaign);
        campaigns.put(CompanyTestProfile.PANZER_LEHR_PROFILE, germanWestCampaign);
        campaigns.put(CompanyTestProfile.THIRD_DIVISION_PROFILE, americanCampaign);
        campaigns.put(CompanyTestProfile.SEVENTH_DIVISION_PROFILE, britishCampaign);
        campaigns.put(CompanyTestProfile.TANK_DIVISION_147_PROFILE, russianCampaign);
    }

    @Test
    public void patrolFlightTest() throws PWCGException
    {
        for (CompanyTestProfile profile: campaigns.keySet()) 
        {
        	Campaign campaign = campaigns.get(profile);
            PWCGContext.getInstance().setCampaign(campaign);
            Mission mission = generateMission(campaign, PlatoonMissionType.ASSAULT);
            verifyEnemyFlights(campaign, mission);
        }
    }
 
    private Mission generateMission(Campaign campaign, PlatoonMissionType missionType) throws PWCGException
    {
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeTestMissionFromMissionType(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        mission.finalizeMission();
        return mission;
    }
    
    private int verifyEnemyFlights(Campaign campaign,Mission mission) throws PWCGException 
    {
        Side enemySide = mission.getPlatoons().getPlayerUnits().get(0).getCompany().determineEnemySide();
        
        boolean enemyUnitFound = false;
        int numEnemyFlights = 0;
        for (ITankPlatoon unit: mission.getPlatoons().getAiUnits())
        {
            if(unit.getCompany().determineSide() == enemySide)
            {
                enemyUnitFound = true;
                ++numEnemyFlights;
            }
        }
        
        if (!enemyUnitFound)
        {
            System.out.println("!!!!!No Enemy flights found for campaign " + campaign.getCampaignData().getName());
        }
        else
        {
            System.out.println("Enemy flights found is " + numEnemyFlights + " for campaign " + campaign.getCampaignData().getName());
        }

        assert(enemyUnitFound);
        return numEnemyFlights;
    }
}
