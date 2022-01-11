package pwcg.campaign.personnel;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CompanyPersonnelTest {

    private Campaign campaign;    
    
    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        
        campaign = CampaignCache.makeCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);
    }

    @Test
    public void isHumanCompanyTest() throws PWCGException
    {
        CompanyPersonnel companypersonnel = campaign.getPersonnelManager().getCompanyPersonnel(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId());
        assert(companypersonnel.isPlayerCompany());
    }

    @Test
    public void isNotHumanCompanyTest() throws PWCGException
    {
        CompanyPersonnel companypersonnel = campaign.getPersonnelManager().getCompanyPersonnel(20111052);
        assert(companypersonnel.isPlayerCompany() == false);
    }
}
