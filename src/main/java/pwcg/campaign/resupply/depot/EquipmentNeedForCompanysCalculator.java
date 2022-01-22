package pwcg.campaign.resupply.depot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.resupply.CompanyNeedFactory;
import pwcg.campaign.resupply.ICompanyNeed;
import pwcg.campaign.tank.TankArchType;
import pwcg.core.exception.PWCGException;

public class EquipmentNeedForCompanysCalculator
{
    private Campaign campaign;
    private CompanyNeedFactory companyNeedFactory;
    
    public EquipmentNeedForCompanysCalculator (Campaign campaign, CompanyNeedFactory companyNeedFactory)
    {
        this.campaign = campaign;
        this.companyNeedFactory = companyNeedFactory;
    }

    public Map<String, Integer> getAircraftNeedByArchType(List<Company> companysForService) throws PWCGException
    {
        Map<String, Integer> aircraftNeedByArchType = new HashMap<>();

        for (Company company : companysForService)
        {
            ICompanyNeed companyResupplyNeed = companyNeedFactory.buildCompanyNeed(campaign, company);
            companyResupplyNeed.determineResupplyNeeded();                
            for (int i = 0; i < companyResupplyNeed.getNumNeeded(); ++i)
            {
                List<TankArchType> currentAircraftArchTypes = company.determineCurrentTankArchTypes(campaign.getDate());
                for (TankArchType planeArchType : currentAircraftArchTypes)
                {
                    if (!aircraftNeedByArchType.containsKey(planeArchType.getTankArchTypeName()))
                    {
                        aircraftNeedByArchType.put(planeArchType.getTankArchTypeName(), 0);
                    }
                    
                    int numNeededForArchType = aircraftNeedByArchType.get(planeArchType.getTankArchTypeName());
                    aircraftNeedByArchType.put(planeArchType.getTankArchTypeName(), numNeededForArchType+1);
                }
            }
        }
        
        return aircraftNeedByArchType;
    }
}
