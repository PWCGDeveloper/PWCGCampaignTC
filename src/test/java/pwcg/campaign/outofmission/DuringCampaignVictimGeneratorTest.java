package pwcg.campaign.outofmission;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.personnel.EnemyCompanyFinder;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DuringCampaignVictimGeneratorTest
{
    private Campaign campaign;    
    
    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        
        campaign = CampaignCache.makeCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);
    }

    @Test
    public void testVictimGeneration () throws PWCGException
    {               
        Company victorCompany = PWCGContext.getInstance().getCompanyManager().getCompany(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId());        
        EnemyCompanyFinder enemyCompanyFinder = new EnemyCompanyFinder(campaign);
        Company victimCompany = enemyCompanyFinder.getEnemyForOutOfMission(victorCompany, campaign.getDate());

        DuringCampaignAirVictimGenerator  victimGenerator = new DuringCampaignAirVictimGenerator(campaign, victimCompany);
        Side victorSide = victorCompany.determineCompanyCountry(campaign.getDate()).getSide();

        CrewMember victim = victimGenerator.generateVictimAiCrew();
        Company victimCompanyFromVictim = victim.determineCompany();
        Side victimSide = victimCompany.determineCompanyCountry(campaign.getDate()).getSide();
        
        assert(victimSide != victorSide);
        assert(victimCompany.getCompanyId() == victimCompanyFromVictim.getCompanyId());

    }

    @Test
    public void testNotFromPlayerCompany () throws PWCGException
    {               
        Company company = PWCGContext.getInstance().getCompanyManager().getCompany(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId());
        DuringCampaignAirVictimGenerator  victimGenerator = new DuringCampaignAirVictimGenerator(campaign, company);
        CrewMember victim = victimGenerator.generateVictimAiCrew();
        assert(victim != null);
    }

}
