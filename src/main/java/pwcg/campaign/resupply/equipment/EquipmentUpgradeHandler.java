package pwcg.campaign.resupply.equipment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.resupply.depot.EquipmentDepot;
import pwcg.campaign.resupply.depot.EquipmentUpgradeRecord;
import pwcg.campaign.tank.Equipment;
import pwcg.campaign.tank.EquippedTank;
import pwcg.campaign.tank.TankSorter;
import pwcg.core.exception.PWCGException;

public class EquipmentUpgradeHandler
{
    private Campaign campaign;
    private EquipmentResupplyData equipmentResupplyData = new EquipmentResupplyData();
    
    public EquipmentUpgradeHandler(Campaign campaign)
    {
        this.campaign = campaign;
    }
    
    public EquipmentResupplyData upgradeEquipment(ArmedService armedService) throws PWCGException
    {
        upgradePlayerCompanys(armedService);
        upgradeAiCompanys(armedService);
        return equipmentResupplyData;
    }

    private void upgradePlayerCompanys(ArmedService armedService) throws PWCGException
    {
        CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        for (Company company : companyManager.getActiveCompaniesForService(campaign.getDate(), armedService))
        {
            if (Company.isPlayerCompany(campaign, company.getCompanyId()))
            {
                upgradeEquipment(company);
            }
        }
    }

    private void upgradeAiCompanys(ArmedService armedService) throws PWCGException
    {
        CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        for (Company company : companyManager.getActiveCompaniesForService(campaign.getDate(), armedService))
        {
            upgradeEquipment(company);
        }
    }

    private void upgradeEquipment(Company company) throws PWCGException
    {
        Equipment equipmentForCompany = campaign.getEquipmentManager().getEquipmentForCompany(company.getCompanyId());
        EquipmentDepot equipmentDepot = campaign.getEquipmentManager().getEquipmentDepotForService(company.getService());

        List<EquippedTank> sortedTanks = getTanksForCompanyWorstToBest(equipmentForCompany);
        for (EquippedTank equippedTank : sortedTanks)
        {
            EquipmentUpgradeRecord equipmentUpgrade = equipmentDepot.getUpgrade(equippedTank);
            if (equipmentUpgrade != null)
            {
                EquippedTank replacementPlane = equipmentDepot.removeEquippedTankFromDepot(equipmentUpgrade.getUpgrade().getSerialNumber());
                equipmentForCompany.addEquippedTankToCompany(campaign, company.getCompanyId(), replacementPlane);
                
                EquippedTank replacedPlane = equipmentForCompany.removeEquippedTank(equipmentUpgrade.getReplacedPlane().getSerialNumber());
                equipmentDepot.addTankToDepot(replacedPlane);

                EquipmentResupplyRecord equipmentResupplyRecord = new EquipmentResupplyRecord(replacementPlane, company.getCompanyId());
                equipmentResupplyData.addEquipmentResupplyRecord(equipmentResupplyRecord);
            }
        }        
    }

    private List<EquippedTank> getTanksForCompanyWorstToBest(Equipment equipmentForCompany) throws PWCGException
    {
        Map<Integer, EquippedTank> planesForCompany = equipmentForCompany.getActiveEquippedTanks();
        List<EquippedTank> sortedTanks = TankSorter.sortEquippedTanksByGoodness(new ArrayList<EquippedTank>(planesForCompany.values()));
        Collections.reverse(sortedTanks);
        return sortedTanks;
    }
}
