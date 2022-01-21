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
            EquipmentWeightCalculator equipmentWeightCalculator = createPlaneCalculator(company);            
            makeReplacementPlanesForCompany(equipmentWeightCalculator);
        }
        return equipment;
    }

    private EquipmentWeightCalculator createPlaneCalculator(Company company) throws PWCGException
    {
        List<TankTypeInformation> planeTypesForCompany = company.determineCurrentAircraftList(campaign.getDate());
        EquipmentWeightCalculator equipmentWeightCalculator = new EquipmentWeightCalculator(campaign.getDate());
        equipmentWeightCalculator.determinePlaneWeightsForPlanes(planeTypesForCompany);
        return equipmentWeightCalculator;
    }

    private void makeReplacementPlanesForCompany(EquipmentWeightCalculator equipmentWeightCalculator) throws PWCGException
    {
        int numPlanes = service.getDailyEquipmentReplacementRate(campaign.getDate()) / EquipmentDepot.NUM_POINTS_PER_PLANE;
        if (numPlanes < 1)
        {
            numPlanes = 1;
        }
        
        for (int i = 0; i < numPlanes; ++i)
        {
            String planeTypeName = equipmentWeightCalculator.getTankTypeFromWeight();
            EquippedTank equippedTank = TankEquipmentFactory.makeTankForDepot(campaign, planeTypeName);
            equipment.addEPlaneToDepot(equippedTank);
        }
    }
}
