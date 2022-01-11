package pwcg.aar.campaign.update;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.personnel.InitialCompanyStaffer;
import pwcg.core.exception.PWCGException;

public class CampaignUpdateNewCompanyStaffer
{
    private Campaign campaign;
    private List<Integer> companysAdded = new ArrayList<>();
    
    public CampaignUpdateNewCompanyStaffer (Campaign campaign) 
    {
        this.campaign = campaign;
    }

    public List<Integer> staffNewCompanies() throws PWCGException
    {
        CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        List<Company> companysToStaff = companyManager.getActiveCompanies(campaign.getDate());
        for (Company company : companysToStaff)
        {
            if (campaign.getPersonnelManager().getCompanyPersonnel(company.getCompanyId()) == null)
            {
                InitialCompanyStaffer companyStaffer = new InitialCompanyStaffer(campaign, company);
                CompanyPersonnel companyPersonnel = companyStaffer.generatePersonnel();
                campaign.getPersonnelManager().addPersonnelForCompany(companyPersonnel);
                companysAdded.add(company.getCompanyId());
            }
        }
        
        return companysAdded;
    }
}
