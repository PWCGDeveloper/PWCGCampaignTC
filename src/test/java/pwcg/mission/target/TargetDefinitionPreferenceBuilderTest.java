package pwcg.mission.target;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyRolePeriod;
import pwcg.campaign.company.CompanyRoleSet;
import pwcg.campaign.company.SquadronRoleWeight;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.plane.PwcgRole;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.flight.IFlight;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TargetDefinitionPreferenceBuilderTest
{
    private Campaign campaign;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        
        campaign = CampaignCache.makeCampaign(CompanyTestProfile.JG_26_PROFILE_WEST);
    }
    
    @Test
    public void tankBustTargetTypeTest() throws PWCGException
    {
        Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(CompanyTestProfile.JG_26_PROFILE_WEST.getCompanyId());
        
        SquadronRoleWeight squadronRoleWeight = new SquadronRoleWeight();
        squadronRoleWeight.setRole(PwcgRole.ROLE_TANK_BUSTER);
        squadronRoleWeight.setWeight(100);
        
        CompanyRolePeriod squadronRolePeriod = new CompanyRolePeriod();
        squadronRolePeriod.setStartDate(DateUtils.getDateYYYYMMDD("19400101"));
        squadronRolePeriod.setWeightedRoles(Arrays.asList(squadronRoleWeight));

        CompanyRoleSet squadronRoleSet = squadron.getSquadronRoles();
        squadronRoleSet.overrideRolesForTest(Arrays.asList(squadronRolePeriod));

        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        
        IFlight playerFlight = mission.getFlights().getUnits().get(0);

        TargetDefinitionPreferenceBuilder targetDefinitionPreferenceBuilder = new TargetDefinitionPreferenceBuilder(playerFlight.getFlightInformation());
        List<TargetType> shuffledTargetTypes = targetDefinitionPreferenceBuilder.getTargetPreferences();
        assert(shuffledTargetTypes.get(0) == TargetType.TARGET_ARMOR);
    }
    
    @Test
    public void trainBustTargetTypeTest() throws PWCGException
    {
        Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(CompanyTestProfile.JG_26_PROFILE_WEST.getCompanyId());
        
        SquadronRoleWeight squadronRoleWeight = new SquadronRoleWeight();
        squadronRoleWeight.setRole(PwcgRole.ROLE_TRAIN_BUSTER);
        squadronRoleWeight.setWeight(100);
        
        CompanyRolePeriod squadronRolePeriod = new CompanyRolePeriod();
        squadronRolePeriod.setStartDate(DateUtils.getDateYYYYMMDD("19400101"));
        squadronRolePeriod.setWeightedRoles(Arrays.asList(squadronRoleWeight));

        CompanyRoleSet squadronRoleSet = squadron.getSquadronRoles();
        squadronRoleSet.overrideRolesForTest(Arrays.asList(squadronRolePeriod));

        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        
        IFlight playerFlight = mission.getFlights().getUnits().get(0);

        TargetDefinitionPreferenceBuilder targetDefinitionPreferenceBuilder = new TargetDefinitionPreferenceBuilder(playerFlight.getFlightInformation());
        List<TargetType> shuffledTargetTypes = targetDefinitionPreferenceBuilder.getTargetPreferences();
        assert(shuffledTargetTypes.get(0) == TargetType.TARGET_TRAIN);
    }
    
    @Test
    public void shippingTargetTypeTest() throws PWCGException
    {
        Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(CompanyTestProfile.JG_26_PROFILE_WEST.getCompanyId());
        
        SquadronRoleWeight squadronRoleWeight = new SquadronRoleWeight();
        squadronRoleWeight.setRole(PwcgRole.ROLE_ANTI_SHIPPING);
        squadronRoleWeight.setWeight(100);
        
        CompanyRolePeriod squadronRolePeriod = new CompanyRolePeriod();
        squadronRolePeriod.setStartDate(DateUtils.getDateYYYYMMDD("19400101"));
        squadronRolePeriod.setWeightedRoles(Arrays.asList(squadronRoleWeight));

        CompanyRoleSet squadronRoleSet = squadron.getSquadronRoles();
        squadronRoleSet.overrideRolesForTest(Arrays.asList(squadronRolePeriod));

        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        
        IFlight playerFlight = mission.getFlights().getUnits().get(0);

        TargetDefinitionPreferenceBuilder targetDefinitionPreferenceBuilder = new TargetDefinitionPreferenceBuilder(playerFlight.getFlightInformation());
        List<TargetType> shuffledTargetTypes = targetDefinitionPreferenceBuilder.getTargetPreferences();
        assert(shuffledTargetTypes.get(0) == TargetType.TARGET_SHIPPING);
    }

}
