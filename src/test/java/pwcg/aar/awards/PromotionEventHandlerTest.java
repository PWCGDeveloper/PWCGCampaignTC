package pwcg.aar.awards;

import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.outofmission.phase2.awards.PromotionEventHandler;
import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberVictories;
import pwcg.campaign.promotion.PromotionArbitrator;
import pwcg.campaign.tank.PwcgRoleCategory;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PromotionEventHandlerTest
{
    private Campaign campaign;
    
    @Mock private CrewMember crewMember;
    @Mock private Company company;
    @Mock private CrewMemberVictories companyMemberVictories;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        
        campaign = CampaignCache.makeCampaign(CompanyTestProfile.THIRD_DIVISION_PROFILE);
    }

    @BeforeEach
    public void setupTest() throws PWCGException
    {        
        Mockito.when(crewMember.getCrewMemberVictories()).thenReturn(companyMemberVictories);
    }

    @Test
    public void promoteCorporalToSergent () throws PWCGException
    {     
        Mockito.when(crewMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerCompanies().get(0).determineServiceForCompany(campaign.getDate()));
        Mockito.when(crewMember.getBattlesFought()).thenReturn(30);
        Mockito.when(companyMemberVictories.getTankVictoryCount()).thenReturn(1);
        Mockito.when(crewMember.getRank()).thenReturn("Corporal");
        Mockito.when(crewMember.determineCompany()).thenReturn(company);
        Mockito.when(company.determineCompanyPrimaryRoleCategory(ArgumentMatchers.<Date>any())).thenReturn(PwcgRoleCategory.MAIN_BATTLE_TANK);

        String promotion = PromotionEventHandler.promoteNonHistoricalCrewMembers(campaign, crewMember);

        Assertions.assertTrue (promotion.equals("Sergeant"));
    }

    @Test
    public void promoteCorporalToSergentFailedDueToDifferentRole () throws PWCGException
    {     
        Mockito.when(crewMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerCompanies().get(0).determineServiceForCompany(campaign.getDate()));
        Mockito.when(crewMember.getBattlesFought()).thenReturn(10);
        Mockito.when(crewMember.getRank()).thenReturn("Corporal");
        Mockito.when(crewMember.determineCompany()).thenReturn(company);
        Mockito.when(company.determineCompanyPrimaryRoleCategory(ArgumentMatchers.<Date>any())).thenReturn(PwcgRoleCategory.SELF_PROPELLED_AAA);

        String promotion = PromotionEventHandler.promoteNonHistoricalCrewMembers(campaign, crewMember);

        Assertions.assertTrue (promotion.equals(PromotionArbitrator.NO_PROMOTION));
    }

}
