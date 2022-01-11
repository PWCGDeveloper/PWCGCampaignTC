package pwcg.campaign.company;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.company.CompanyReducer;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CompanyReducerTest
{    
    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        
    }

    @Test
    public void anomaliesRemoved() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(CompanyTestProfile.JG_52_PROFILE_STALINGRAD);
        CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        List<Company> allCompanys = companyManager.getAllCompanies();
        List<Company> companysWithoutAnomalies = CompanyReducer.reduceToNoAnomalies(allCompanys, campaign.getDate());
        
        assert(companysWithoutAnomalies.size() > 30);
        for (Company company : companysWithoutAnomalies)
        {
            Assertions.assertTrue (company.getCompanyId() != 20115021);
            Assertions.assertTrue (company.getCompanyId() != 20111051);
        }
    }

    @Test
    public void jg51NotAnAnomaly() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(CompanyTestProfile.REGIMENT_11_PROFILE);        
        CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        List<Company> allCompanys = companyManager.getAllCompanies();
        List<Company> companysWithoutAnomalies = CompanyReducer.reduceToNoAnomalies(allCompanys, campaign.getDate());
        
        boolean jg51Found = false;
        
        assert(companysWithoutAnomalies.size() > 30);
        for (Company company : companysWithoutAnomalies)
        {
            Assertions.assertTrue (company.getCompanyId() != 20115021);
            
            if (company.getCompanyId() != 20111051)
            {
                jg51Found = true;
            }
        }
        
        Assertions.assertTrue (jg51Found);
    }
    

    @Test
    public void noAnomaliesRemoved() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(CompanyTestProfile.REGIMENT_11_PROFILE);        
        CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        List<Company> allCompanys = companyManager.getAllCompanies();
        List<Company> companysWithoutAnomalies = CompanyReducer.reduceToNoAnomalies(allCompanys, campaign.getDate());
        
        boolean gruppo21Found = false;
        boolean jg51Found = false;
        
        assert(companysWithoutAnomalies.size() > 30);
        for (Company company : companysWithoutAnomalies)
        {            
            if (company.getCompanyId() != 20115021)
            {
                gruppo21Found = true;
            }
            
            if (company.getCompanyId() != 20111051)
            {
                jg51Found = true;
            }
        }
        
        Assertions.assertTrue (gruppo21Found);
        Assertions.assertTrue (jg51Found);
    }
}
