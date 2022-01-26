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
import pwcg.product.bos.country.TCServiceManager;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PromotionEventHandlerWehrmachtTest
{
    private Campaign campaign;
    
    @Mock private ArmedService service;
    @Mock private Company company;
    @Mock private CrewMember crewMember;
    @Mock private CrewMemberVictories companyMemberVictories;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        
        campaign = CampaignCache.makeCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);
    }

    @BeforeEach
    public void setupTest() throws PWCGException
    {        
        Mockito.when(crewMember.determineCompany()).thenReturn(company);
        Mockito.when(company.determineCompanyPrimaryRoleCategory(Mockito.any())).thenReturn(PwcgRoleCategory.MAIN_BATTLE_TANK);
        Mockito.when(company.getService()).thenReturn(TCServiceManager.WEHRMACHT);
    }

    @Test
    public void promoteOberFeldwebel () throws PWCGException
    {     
        Mockito.when(companyMemberVictories.getTankVictoryCount()).thenReturn(1);
        Mockito.when(crewMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerCompanies().get(0).determineServiceForCompany(campaign.getDate()));
        Mockito.when(crewMember.getBattlesFought()).thenReturn(30);
        Mockito.when(crewMember.getRank()).thenReturn("Gefreiter");
        Mockito.when(crewMember.getCrewMemberVictories()).thenReturn(companyMemberVictories);

        String promotion = PromotionEventHandler.promoteNonHistoricalCrewMembers(campaign, crewMember);

        Assertions.assertTrue (promotion.equals("Oberfeldwebel"));
    }

    @Test
    public void promoteLeutnant () throws PWCGException
    {     
        Mockito.when(companyMemberVictories.getTankVictoryCount()).thenReturn(5);
        Mockito.when(crewMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerCompanies().get(0).determineServiceForCompany(campaign.getDate()));
        Mockito.when(crewMember.getBattlesFought()).thenReturn(60);
        Mockito.when(crewMember.getRank()).thenReturn("Oberfeldwebel");
        Mockito.when(crewMember.getCrewMemberVictories()).thenReturn(companyMemberVictories);

        String promotion = PromotionEventHandler.promoteNonHistoricalCrewMembers(campaign, crewMember);

        Assertions.assertTrue (promotion.equals("Leutnant"));
    }

    @Test
    public void promoteOberleutnant () throws PWCGException
    {     
        Mockito.when(companyMemberVictories.getTankVictoryCount()).thenReturn(10);
        Mockito.when(crewMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerCompanies().get(0).determineServiceForCompany(campaign.getDate()));
        Mockito.when(crewMember.getBattlesFought()).thenReturn(110);
        Mockito.when(crewMember.getRank()).thenReturn("Leutnant");
        Mockito.when(crewMember.getCrewMemberVictories()).thenReturn(companyMemberVictories);

        String promotion = PromotionEventHandler.promoteNonHistoricalCrewMembers(campaign, crewMember);

        Assertions.assertTrue (promotion.equals("Oberleutnant"));
    }

    @Test
    public void promoteHauptmann () throws PWCGException
    {     
        Mockito.when(companyMemberVictories.getTankVictoryCount()).thenReturn(15);
        Mockito.when(crewMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerCompanies().get(0).determineServiceForCompany(campaign.getDate()));
        Mockito.when(crewMember.getBattlesFought()).thenReturn(150);
        Mockito.when(crewMember.getRank()).thenReturn("Oberleutnant");
        Mockito.when(crewMember.getCompanyId()).thenReturn(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId());
        Mockito.when(crewMember.isPlayer()).thenReturn(true);
        Mockito.when(crewMember.getCrewMemberVictories()).thenReturn(companyMemberVictories);

        String promotion = PromotionEventHandler.promoteNonHistoricalCrewMembers(campaign, crewMember);

        Assertions.assertTrue (promotion.equals("Hauptmann"));
    }

    @Test
    public void promoteHauptmannFailNotPlayer () throws PWCGException
    {     
        Mockito.when(crewMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerCompanies().get(0).determineServiceForCompany(campaign.getDate()));
        Mockito.when(crewMember.getRank()).thenReturn("Oberleutnant");
        Mockito.when(crewMember.isPlayer()).thenReturn(false);

        String promotion = PromotionEventHandler.promoteNonHistoricalCrewMembers(campaign, crewMember);

        Assertions.assertTrue (promotion.equals(PromotionArbitrator.NO_PROMOTION));
    }

    @Test
    public void promoteHauptmannFailNotEnoughMissions () throws PWCGException
    {     
        Mockito.when(crewMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerCompanies().get(0).determineServiceForCompany(campaign.getDate()));
        Mockito.when(crewMember.getBattlesFought()).thenReturn(99);
        Mockito.when(crewMember.getRank()).thenReturn("Oberleutnant");
        Mockito.when(crewMember.isPlayer()).thenReturn(true);
        Mockito.when(crewMember.getCrewMemberVictories()).thenReturn(companyMemberVictories);

        String promotion = PromotionEventHandler.promoteNonHistoricalCrewMembers(campaign, crewMember);

        Assertions.assertTrue (promotion.equals(PromotionArbitrator.NO_PROMOTION));
    }

    @Test
    public void promoteMajorFailNotEnoughVictories () throws PWCGException
    {     
        Mockito.when(crewMember.determineService(ArgumentMatchers.<Date>any())).thenReturn(campaign.determinePlayerCompanies().get(0).determineServiceForCompany(campaign.getDate()));
        Mockito.when(crewMember.getBattlesFought()).thenReturn(150);
        Mockito.when(crewMember.getRank()).thenReturn("Oberleutnant");
        Mockito.when(crewMember.isPlayer()).thenReturn(true);
        Mockito.when(crewMember.getCrewMemberVictories()).thenReturn(companyMemberVictories);

        String promotion = PromotionEventHandler.promoteNonHistoricalCrewMembers(campaign, crewMember);

        Assertions.assertTrue (promotion.equals(PromotionArbitrator.NO_PROMOTION));
    }

}
