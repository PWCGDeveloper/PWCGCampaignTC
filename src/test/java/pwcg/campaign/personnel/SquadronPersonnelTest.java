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
public class SquadronPersonnelTest {

    private Campaign campaign;    
    
    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        
        campaign = CampaignCache.makeCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);
    }

    @Test
    public void isHumanSquadronTest() throws PWCGException
    {
        CompanyPersonnel squadronpersonnel = campaign.getPersonnelManager().getCompanyPersonnel(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId());
        assert(squadronpersonnel.isPlayerCompany());
    }

    @Test
    public void isNotHumanSquadronTest() throws PWCGException
    {
        CompanyPersonnel squadronpersonnel = campaign.getPersonnelManager().getCompanyPersonnel(20111052);
        assert(squadronpersonnel.isPlayerCompany() == false);
    }
}
