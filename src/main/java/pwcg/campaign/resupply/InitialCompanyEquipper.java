package pwcg.campaign.resupply;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.resupply.depot.EquipmentWeightCalculator;
import pwcg.campaign.tank.Equipment;
import pwcg.campaign.tank.EquippedTank;
import pwcg.campaign.tank.TankArchType;
import pwcg.campaign.tank.TankEquipmentFactory;
import pwcg.campaign.tank.TankType;
import pwcg.core.exception.PWCGException;

public class InitialCompanyEquipper 
{
    private Campaign campaign;
    private Company company;
    private Equipment equipment = new Equipment();
    private int planesNeeded = Company.COMPANY_EQUIPMENT_SIZE;
    private EquipmentWeightCalculator equipmentWeightCalculator;

	public InitialCompanyEquipper(Campaign campaign, Company company, EquipmentWeightCalculator equipmentWeightCalculator) 
	{
        this.campaign = campaign;
        this.company = company;
        this.equipmentWeightCalculator = equipmentWeightCalculator;
	}

    public Equipment generateEquipment() throws PWCGException 
    {
        determinePlaneWeightsForCompany();
        generatePlanesForCompany();
        return equipment;
    }
    
    private void determinePlaneWeightsForCompany() throws PWCGException
    {
        List<TankArchType> currentAircraftArchTypes = company.determineCurrentAircraftArchTypes(campaign.getDate());
        
        List<TankType> planeTypesInCompany = new ArrayList<>();
        for (TankArchType planeArchType : currentAircraftArchTypes)
        {
            List<TankType> planeTypesForArchType = PWCGContext.getInstance().getTankTypeFactory().createActiveTankTypesForArchType(planeArchType.getTankArchTypeName(), campaign.getDate());
            planeTypesInCompany.addAll(planeTypesForArchType);
        }
        
        equipmentWeightCalculator.determinePlaneWeightsForPlanes(planeTypesInCompany);
    }

    private void generatePlanesForCompany() throws PWCGException 
    {       
        
        for (int i = 0; i < planesNeeded; ++i)
        {
            String planeTypeName = equipmentWeightCalculator.getTankTypeFromWeight();
            
            EquippedTank equippedTank = TankEquipmentFactory.makeTankForCompany(campaign, planeTypeName, company.getCompanyId());
            equipment.addEquippedTankToCompany(campaign, company.getCompanyId(), equippedTank);
        }
    }

}
