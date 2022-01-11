package pwcg.campaign.resupply;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.resupply.CompanyNeedFactory.CompanyNeedType;
import pwcg.core.exception.PWCGException;

public class ResupplyNeedBuilder
{
    private Campaign campaign;
    private ArmedService service;

    public ResupplyNeedBuilder (Campaign campaign, ArmedService service)
    {
        this.campaign = campaign;
        this.service = service;
    }
    
    public ServiceResupplyNeed determineNeedForService(CompanyNeedType need) throws PWCGException
    {
        CompanyNeedFactory companyNeedFactory = new CompanyNeedFactory(need);
        ServiceResupplyNeed serviceTransferNeed = new ServiceResupplyNeed(campaign, service.getServiceId(), companyNeedFactory);
        serviceTransferNeed.determineResupplyNeed();
        return serviceTransferNeed;
    }
}
