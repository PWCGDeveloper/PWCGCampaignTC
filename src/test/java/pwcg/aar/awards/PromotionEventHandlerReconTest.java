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
import pwcg.campaign.ArmedService;
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
public class PromotionEventHandlerReconTest
{
    private Campaign campaign;
    
    @Mock private ArmedService service;
    @Mock private Company company;
    @Mock private CrewMember crewMember;
    @Mock private CrewMemberVictories companyMemberVictories;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        
        campaign = CampaignCache.makeCampaign(CompanyTestProfile.THIRD_DIVISION_PROFILE);        
    }

    @BeforeEach
    public void setupTest() throws PWCGException
    {        
        Mockito.when(crewMember.determineCompany()).thenReturn(company);
        Mockito.when(company.determineCompanyPrimaryRoleCategory(Mockito.any())).thenReturn(PwcgRoleCategory.RECON);
        Mockito.when(crewMember.getCrewMemberVictories()).thenReturn(companyMemberVictories);
        Mockito.when(companyMemberVictories.getGroundVictoryPointTotal()).thenReturn(0);
    }

    @Test
    public void promoteCorporalToSergent () throws PWCGException
    {     
        Mockito.when(crewMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerCompanies().get(0).determineServiceForCompany(campaign.getDate()));
        Mockito.when(crewMember.getBattlesFought()).thenReturn(30);
        Mockito.when(crewMember.getRank()).thenReturn("Corporal");

        String promotion = PromotionEventHandler.promoteNonHistoricalCrewMembers(campaign, crewMember);

        Assertions.assertTrue (promotion.equals("Sergent"));
    }

    @Test
    public void promoteSergent () throws PWCGException
    {     
        Mockito.when(crewMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerCompanies().get(0).determineServiceForCompany(campaign.getDate()));
        Mockito.when(crewMember.getBattlesFought()).thenReturn(60);
        Mockito.when(crewMember.getRank()).thenReturn("Sergent");

        String promotion = PromotionEventHandler.promoteNonHistoricalCrewMembers(campaign, crewMember);

        Assertions.assertTrue (promotion.equals("Sous Lieutenant"));
    }

    @Test
    public void promoteSousLieutenant () throws PWCGException
    {     
        Mockito.when(crewMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerCompanies().get(0).determineServiceForCompany(campaign.getDate()));
        Mockito.when(crewMember.getBattlesFought()).thenReturn(110);
        Mockito.when(crewMember.getRank()).thenReturn("Sous Lieutenant");

        String promotion = PromotionEventHandler.promoteNonHistoricalCrewMembers(campaign, crewMember);

        Assertions.assertTrue (promotion.equals("Lieutenant"));
    }

    @Test
    public void promoteCapitaine () throws PWCGException
    {     
        Mockito.when(crewMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerCompanies().get(0).determineServiceForCompany(campaign.getDate()));
        Mockito.when(crewMember.getBattlesFought()).thenReturn(150);
        Mockito.when(crewMember.getRank()).thenReturn("Lieutenant");
        Mockito.when(crewMember.isPlayer()).thenReturn(true);
        Mockito.when(crewMember.getCompanyId()).thenReturn(CompanyTestProfile.THIRD_DIVISION_PROFILE.getCompanyId());

        String promotion = PromotionEventHandler.promoteNonHistoricalCrewMembers(campaign, crewMember);

        Assertions.assertTrue (promotion.equals("Capitaine"));
    }

    @Test
    public void promoteCapitaineFailNotPlayer () throws PWCGException
    {     
        Mockito.when(crewMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerCompanies().get(0).determineServiceForCompany(campaign.getDate()));
        Mockito.when(crewMember.getBattlesFought()).thenReturn(150);
        Mockito.when(crewMember.getRank()).thenReturn("Lieutenant");
        Mockito.when(crewMember.isPlayer()).thenReturn(false);
        Mockito.when(crewMember.getCompanyId()).thenReturn(CompanyTestProfile.THIRD_DIVISION_PROFILE.getCompanyId());

        String promotion = PromotionEventHandler.promoteNonHistoricalCrewMembers(campaign, crewMember);

        Assertions.assertTrue (promotion.equals(PromotionArbitrator.NO_PROMOTION));
    }

    @Test
    public void promoteCapitaineFailNotEnoughMissions () throws PWCGException
    {     
        Mockito.when(crewMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerCompanies().get(0).determineServiceForCompany(campaign.getDate()));
        Mockito.when(crewMember.getBattlesFought()).thenReturn(109);
        Mockito.when(crewMember.getRank()).thenReturn("Lieutenant");

        String promotion = PromotionEventHandler.promoteNonHistoricalCrewMembers(campaign, crewMember);

        Assertions.assertTrue (promotion.equals(PromotionArbitrator.NO_PROMOTION));
    }
}
