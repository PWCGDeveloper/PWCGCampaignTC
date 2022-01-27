package pwcg.campaign.resupply.depot;

import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.tank.Equipment;
import pwcg.campaign.tank.EquippedTank;
import pwcg.campaign.tank.TankEquipmentFactory;
import pwcg.campaign.tank.TankTypeInformation;
import pwcg.core.exception.PWCGException;

public class EquipmentDepotInitializer
{
    private Campaign campaign;
    private ArmedService service;
    private Equipment equipment = new Equipment();

    public EquipmentDepotInitializer(Campaign campaign, ArmedService service) 
    {
        this.campaign = campaign;
        this.service = service;
    }

    public Equipment createReplacementPoolForService() throws PWCGException
    {
        List<Company> activeCompanysForService = PWCGContext.getInstance().getCompanyManager().getActiveCompaniesForService(campaign.getDate(), service);
        for (Company company : activeCompanysForService)
        {
            EquipmentWeightCalculator equipmentWeightCalculator = createTankCalculator(company);            
            makeReplacementTanksForCompany(equipmentWeightCalculator);
        }
        return equipment;
    }

    private EquipmentWeightCalculator createTankCalculator(Company company) throws PWCGException
    {
        List<TankTypeInformation> tankTypesForCompany = company.determineCurrentTankList(campaign.getDate());
        EquipmentWeightCalculator equipmentWeightCalculator = new EquipmentWeightCalculator(campaign.getDate());
        equipmentWeightCalculator.determineTankWeightsForTanks(tankTypesForCompany);
        return equipmentWeightCalculator;
    }

    private void makeReplacementTanksForCompany(EquipmentWeightCalculator equipmentWeightCalculator) throws PWCGException
    {
        int numReplacementPoints = service.getDailyEquipmentReplacementRate(campaign.getDate());
        int numTanks = numReplacementPoints / EquipmentDepot.NUM_POINTS_PER_PLANE;
        if (numTanks < 1)
        {
            numTanks = 1;
        }
        
        for (int i = 0; i < numTanks; ++i)
        {
            String tankTypeName = equipmentWeightCalculator.getTankTypeFromWeight();
            EquippedTank equippedTank = TankEquipmentFactory.makeTankForDepot(campaign, tankTypeName, service.getCountry().getCountry());
            equipment.addTankToDepot(equippedTank);
        }
    }
}
