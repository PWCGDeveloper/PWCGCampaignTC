package pwcg.campaign.resupply;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.resupply.equipment.CompanyEquipmentNeed;
import pwcg.campaign.resupply.personnel.CompanyPersonnelNeed;

public class CompanyNeedFactory
{
    public enum CompanyNeedType
    {
        PERSONNEL,
        EQUIPMENT
    };
    
    CompanyNeedType companyNeedType = CompanyNeedType.PERSONNEL;
    
    public CompanyNeedFactory(CompanyNeedType companyNeedType)
    {
        this.companyNeedType = companyNeedType;
    }
    
    public ICompanyNeed buildCompanyNeed(Campaign campaign, Company company)
    {
        if (companyNeedType == CompanyNeedType.PERSONNEL)
        {
            return new CompanyPersonnelNeed(campaign, company);
        }
        else
        {
            return new CompanyEquipmentNeed(campaign, company);
        }
    }
}
