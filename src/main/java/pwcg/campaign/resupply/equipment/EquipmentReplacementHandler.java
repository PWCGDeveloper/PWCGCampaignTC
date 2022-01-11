package pwcg.campaign.resupply.equipment;

import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.resupply.CompanyNeedFactory.CompanyNeedType;
import pwcg.campaign.resupply.ICompanyNeed;
import pwcg.campaign.resupply.ResupplyNeedBuilder;
import pwcg.campaign.resupply.ServiceResupplyNeed;
import pwcg.campaign.resupply.depot.EquipmentDepot;
import pwcg.campaign.tank.EquippedTank;
import pwcg.core.exception.PWCGException;

public class EquipmentReplacementHandler
{
    private Campaign campaign;
    private ResupplyNeedBuilder equipmentNeedBuilder;

    private EquipmentResupplyData equipmentResupplyData = new EquipmentResupplyData();
    
    public EquipmentReplacementHandler(Campaign campaign, ResupplyNeedBuilder equipmentNeedBuilder)
    {
        this.campaign = campaign;
        this.equipmentNeedBuilder = equipmentNeedBuilder;
    }
    
    public EquipmentResupplyData resupplyForLosses(ArmedService armedService) throws PWCGException
    {
        ServiceResupplyNeed serviceResupplyNeed = equipmentNeedBuilder.determineNeedForService(CompanyNeedType.EQUIPMENT);
        EquipmentDepot equipmentDepo =  campaign.getEquipmentManager().getEquipmentDepotForService(armedService.getServiceId());
        replaceForService(serviceResupplyNeed, equipmentDepo);
        return equipmentResupplyData;
    }


    private void replaceForService(ServiceResupplyNeed serviceResupplyNeed, EquipmentDepot equipmentDepo) throws PWCGException
    {
        while (serviceResupplyNeed.hasNeedyCompany())
        {
            ICompanyNeed selectedCompanyNeed = serviceResupplyNeed.chooseNeedyCompany();
            if (selectedCompanyNeed == null)
            {
                break;
            }

            Company company = PWCGContext.getInstance().getCompanyManager().getCompany(selectedCompanyNeed.getCompanyId());
            List<String> activeArchTypes = company.getActiveArchTypes(campaign.getDate());
            
            EquippedTank replacement = equipmentDepo.removeBestPlaneFromDepot(activeArchTypes);        
            if (replacement != null)
            {
                EquipmentResupplyRecord equipmentResupplyRecord = new EquipmentResupplyRecord(replacement, selectedCompanyNeed.getCompanyId());
                equipmentResupplyData.addEquipmentResupplyRecord(equipmentResupplyRecord);
                serviceResupplyNeed.noteResupply(selectedCompanyNeed);
            }
            else
            {
                serviceResupplyNeed.removeNeedyCompany(selectedCompanyNeed);
            }
        }        
    }
}
