package pwcg.campaign.resupply.depot;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyViability;
import pwcg.campaign.tank.TankArchType;
import pwcg.core.exception.PWCGException;

public class EquipmentReplacementWeightUsage
{
    private Date campaignDate;

    public EquipmentReplacementWeightUsage (Date campaignDate)
    {
        this.campaignDate = campaignDate;
    }

    public Map<String, Integer> getTankUsageByArchType(List<Company> companysForService) throws PWCGException
    {
        Map<String, Integer> aircraftUsageByArchType = new HashMap<>();
        
        for (Company company : companysForService)
        {
            if (CompanyViability.isCompanyActive(company, campaignDate))
            {
                List<TankArchType> currentAircraftArchTypes = company.determineCurrentTankArchTypes(campaignDate);
                for (TankArchType planeArchType : currentAircraftArchTypes)
                {
                    if (excludeFromWeightedList(planeArchType.getTankArchTypeName()))
                    {
                        continue;
                    }
                    
                    if (!aircraftUsageByArchType.containsKey(planeArchType.getTankArchTypeName()))
                    {
                        aircraftUsageByArchType.put(planeArchType.getTankArchTypeName(), Integer.valueOf(0)); 
                    }
                    
                    Integer numUsagesOfArchtype = aircraftUsageByArchType.get(planeArchType.getTankArchTypeName());
                    ++numUsagesOfArchtype;
                    aircraftUsageByArchType.put(planeArchType.getTankArchTypeName(), numUsagesOfArchtype);
                }
            }
        }
        
        return aircraftUsageByArchType;
    }
    
    private boolean excludeFromWeightedList(String archType)
    {
        if (archType.equalsIgnoreCase("ju52"))
        {
            return true;
        }
        return false;
    }
}
