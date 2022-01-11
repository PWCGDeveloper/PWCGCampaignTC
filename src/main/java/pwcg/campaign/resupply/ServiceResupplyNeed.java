package pwcg.campaign.resupply;

import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.Campaign;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.core.exception.PWCGException;

public class ServiceResupplyNeed
{
    private Campaign campaign;
    private int serviceId;
    private CompanyNeedFactory companyNeedFactory;
    private Map<Integer, ICompanyNeed> companyNeeds = new TreeMap<>();

    public ServiceResupplyNeed (Campaign campaign, int serviceId, CompanyNeedFactory companyNeedFactory)
    {
        this.campaign = campaign;
        this.serviceId = serviceId;
        this.companyNeedFactory = companyNeedFactory;
    }
    
    public void determineResupplyNeed() throws PWCGException
    {
        for (CompanyPersonnel companyPersonnel : campaign.getPersonnelManager().getAllCompanyPersonnel())
        {
            int serviceIdForCompany = companyPersonnel.getCompany().determineServiceForCompany(campaign.getDate()).getServiceId();
            if (serviceId == serviceIdForCompany)
            {
                ICompanyNeed companyResupplyNeed = companyNeedFactory.buildCompanyNeed(campaign, companyPersonnel.getCompany());
                companyResupplyNeed.determineResupplyNeeded();
                
                if (companyResupplyNeed.needsResupply())
                {
                    companyNeeds.put(companyResupplyNeed.getCompanyId(), companyResupplyNeed);
                }
            }
        }
    }
    
    public boolean hasNeedyCompany()
    {
        if (companyNeeds.isEmpty())
        {
            return false;
        }
        return true;
    }

    public ICompanyNeed chooseNeedyCompany() throws PWCGException
    {        
        ResupplyCompanyChooser resupplyCompanyChooser = new ResupplyCompanyChooser(campaign, companyNeeds);
        return resupplyCompanyChooser.getNeedyCompany();
    }

    public void removeNeedyCompany(ICompanyNeed companyNeed)
    {
        companyNeeds.remove(companyNeed.getCompanyId());
    }

    public void noteResupply(ICompanyNeed companyNeed)
    {
        companyNeed.noteResupply();
        if (!companyNeed.needsResupply())
        {
            removeNeedyCompany(companyNeed);
        }
    }

    public Map<Integer, ICompanyNeed> getCompanyNeeds()
    {
        return companyNeeds;
    }
}
