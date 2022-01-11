package pwcg.campaign.resupply.depot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.resupply.CompanyNeedFactory.CompanyNeedType;
import pwcg.campaign.resupply.ICompanyNeed;
import pwcg.campaign.resupply.ResupplyNeedBuilder;
import pwcg.campaign.resupply.ServiceResupplyNeed;
import pwcg.core.exception.PWCGException;

public class EquipmentReplacementWeightByNeed
{
    private Campaign campaign;

    public EquipmentReplacementWeightByNeed (Campaign campaign)
    {
        this.campaign = campaign;
    }

    public Map<String, Integer> getAircraftNeedByArchType(List<Company> companysForService) throws PWCGException
    {
        if (companysForService.size() == 0)
        {
            return new HashMap<>();
        }
        
        ArmedService service = companysForService.get(0).determineServiceForCompany(campaign.getDate());
        ServiceResupplyNeed resupplyNeed = determineCompanyNeeds(service);        
        Map<String, Integer> aircraftNeedByArchType = determineAircraftNeedByArchType(resupplyNeed);
        return aircraftNeedByArchType;
    }

    private ServiceResupplyNeed determineCompanyNeeds(ArmedService service) throws PWCGException
    {
        ResupplyNeedBuilder needBuilder = new ResupplyNeedBuilder(campaign, service);
        ServiceResupplyNeed resupplyNeed = needBuilder.determineNeedForService(CompanyNeedType.EQUIPMENT);
        return resupplyNeed;
    }

    private Map<String, Integer> determineAircraftNeedByArchType(ServiceResupplyNeed resupplyNeed) throws PWCGException
    {
        Map<String, Integer> aircraftNeedByArchType = new HashMap<>();
        for (int companyId: resupplyNeed.getCompanyNeeds().keySet())
        {
            ICompanyNeed companyNeed = resupplyNeed.getCompanyNeeds().get(companyId);
            if (companyNeed.getNumNeeded() > 0)
            {
                Company company = PWCGContext.getInstance().getCompanyManager().getCompany(companyId);
                List<String> asrchTypesForCompany = company.getActiveArchTypes(campaign.getDate());
                
                for (String archType : asrchTypesForCompany)
                {
                    if (!aircraftNeedByArchType.containsKey(archType))
                    {
                        aircraftNeedByArchType.put(archType, 0);
                    }
                    
                    int numNeededForArchType = aircraftNeedByArchType.get(archType);
                    numNeededForArchType += companyNeed.getNumNeeded();
                    aircraftNeedByArchType.put(archType, numNeededForArchType);                    
                }
            }
        }
        return aircraftNeedByArchType;
    }
}
