package pwcg.aar.campaign.update;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.io.json.CampaignEquipmentIOJson;
import pwcg.campaign.resupply.InitialCompanyEquipper;
import pwcg.campaign.resupply.depot.EquipmentWeightCalculator;
import pwcg.campaign.tank.Equipment;
import pwcg.core.exception.PWCGException;

public class CampaignUpdateNewCompanyEquipper
{
    private Campaign campaign;
    private List<Integer> companysEquipped = new ArrayList<>();
    
    public CampaignUpdateNewCompanyEquipper (Campaign campaign) 
    {
        this.campaign = campaign;
    }

    public List<Integer> equipNewCompanys() throws PWCGException
    {
        CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        for (Company company : companyManager.getActiveCompanies(campaign.getDate()))
        {
            if (campaign.getEquipmentManager().getEquipmentForCompany(company.getCompanyId()) == null)
            {
                EquipmentWeightCalculator equipmentWeightCalculator = new EquipmentWeightCalculator(campaign.getDate());
                InitialCompanyEquipper equipmentStaffer = new InitialCompanyEquipper(campaign, company, equipmentWeightCalculator);
                Equipment companyEquipment = equipmentStaffer.generateEquipment();
                campaign.getEquipmentManager().addEquipmentForCompany(company.getCompanyId(), companyEquipment);
                companysEquipped.add(company.getCompanyId());
                CampaignEquipmentIOJson.writeEquipmentForCompany(campaign, company.getCompanyId());
            }
        }
        
        return companysEquipped;
    }
}
