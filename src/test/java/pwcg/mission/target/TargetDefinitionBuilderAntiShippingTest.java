package pwcg.mission.target;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
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
public class TargetDefinitionBuilderAntiShippingTest
{
    @Test
    public void antiShippingTargetTypeTest() throws PWCGException
    {
        
        Campaign campaign = CampaignCache.makeCampaign(CompanyTestProfile.STG77_KUBAN_PROFILE);

        Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(CompanyTestProfile.STG77_KUBAN_PROFILE.getCompanyId());
        
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

        TargetDefinitionBuilder targetDefinitionBuilder = new TargetDefinitionBuilder(playerFlight.getFlightInformation()); 
        TargetDefinition targetDefinition =  targetDefinitionBuilder.buildTargetDefinition();
        assert(targetDefinition.getTargetType() == TargetType.TARGET_SHIPPING);
    }
}
