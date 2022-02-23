package pwcg.mission.battle;

import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.target.FrontSegmentDefinition;
import pwcg.mission.utils.MissionInformationUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class KubanInvasionTest
{
    @Test
    public void germanKerchInvasionTest() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_KUBAN_PROFILE);
        campaign.setDate(DateUtils.getDateYYYYMMDD("19431101"));
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        mission.finalizeMission();

        Assertions.assertTrue (mission.getSkirmish() != null);
        Assertions.assertTrue (mission.getSkirmish().getSkirmishName().equals("Kerch Amphibious Assault"));
        for (FrontSegmentDefinition assaultDefinition : mission.getBattleManager().getMissionAssaultDefinitions())
        {
            Assertions.assertTrue (assaultDefinition.getAssaultingCountry().getCountry() == Country.RUSSIA);

        }

        boolean diveBombFlightFound = MissionInformationUtils.verifyFlightTypeInMission(mission, FlightTypes.GROUND_ATTACK, Side.AXIS);
        boolean groundAttackFlightFound = MissionInformationUtils.verifyFlightTypeInMission(mission, FlightTypes.LOW_ALT_BOMB, Side.ALLIED);
        Assertions.assertTrue (diveBombFlightFound || groundAttackFlightFound);
    }

    @Test
    public void russianKerchInvasionTest() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(CompanyTestProfile.TANK_DIVISION_147_PROFILE_KUBAN);
        campaign.setDate(DateUtils.getDateYYYYMMDD("19431101"));
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        mission.finalizeMission();

        Assertions.assertTrue (mission.getSkirmish() != null);
        Assertions.assertTrue (mission.getSkirmish().getSkirmishName().equals("Kerch Amphibious Assault"));
        for (FrontSegmentDefinition assaultDefinition : mission.getBattleManager().getMissionAssaultDefinitions())
        {
            Assertions.assertTrue (assaultDefinition.getAssaultingCountry().getCountry() == Country.RUSSIA);
        }

        boolean diveBombFlightFound = MissionInformationUtils.verifyFlightTypeInMission(mission, FlightTypes.GROUND_ATTACK, Side.AXIS);
        boolean groundAttackFlightFound = MissionInformationUtils.verifyFlightTypeInMission(mission, FlightTypes.LOW_ALT_BOMB, Side.ALLIED);
        Assertions.assertTrue (diveBombFlightFound || groundAttackFlightFound);
    }

    @Test
    public void hasDiveBombEltigenInvasionTest() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_KUBAN_PROFILE);
        campaign.setDate(DateUtils.getDateYYYYMMDD("19431110"));
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        mission.finalizeMission();

        Assertions.assertTrue (mission.getSkirmish() != null);
        Assertions.assertTrue (mission.getSkirmish().getSkirmishName().equals("Eltigen Amphibious Assault"));
        for (FrontSegmentDefinition assaultDefinition : mission.getBattleManager().getMissionAssaultDefinitions())
        {
            Assertions.assertTrue (assaultDefinition.getAssaultingCountry().getCountry() == Country.RUSSIA);

        }

        boolean diveBombFlightFound = MissionInformationUtils.verifyFlightTypeInMission(mission, FlightTypes.GROUND_ATTACK, Side.AXIS);
        boolean groundAttackFlightFound = MissionInformationUtils.verifyFlightTypeInMission(mission, FlightTypes.LOW_ALT_BOMB, Side.ALLIED);
        Assertions.assertTrue (diveBombFlightFound || groundAttackFlightFound);
    }

    @Test
    public void doesNotHaveSkirmishAfterTest() throws PWCGException
    {
        noSkirmish(DateUtils.getDateYYYYMMDD("19431112"));
    }

    private void noSkirmish(Date date) throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);
        campaign.setDate(date);
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        mission.finalizeMission();

        Assertions.assertTrue (mission.getSkirmish() == null);        
    }
}
