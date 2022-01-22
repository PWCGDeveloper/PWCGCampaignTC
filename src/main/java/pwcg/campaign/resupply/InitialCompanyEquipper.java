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
import pwcg.campaign.tank.TankTypeInformation;
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
        determineTankWeightsForCompany();
        generateTanksForCompany();
        return equipment;
    }
    
    private void determineTankWeightsForCompany() throws PWCGException
    {
        List<TankArchType> currentAircraftArchTypes = company.determineCurrentTankArchTypes(campaign.getDate());
        
        List<TankTypeInformation> planeTypesInCompany = new ArrayList<>();
        for (TankArchType planeArchType : currentAircraftArchTypes)
        {
            List<TankTypeInformation> planeTypesForArchType = PWCGContext.getInstance().getPlayerTankTypeFactory().createActiveTankTypesForArchType(planeArchType.getTankArchTypeName(), campaign.getDate());
            planeTypesInCompany.addAll(planeTypesForArchType);
        }
        
        equipmentWeightCalculator.determineTankWeightsForTanks(planeTypesInCompany);
    }

    private void generateTanksForCompany() throws PWCGException 
    {       
        
        for (int i = 0; i < planesNeeded; ++i)
        {
            String planeTypeName = equipmentWeightCalculator.getTankTypeFromWeight();
            
            EquippedTank equippedTank = TankEquipmentFactory.makeTankForCompany(campaign, planeTypeName, company);
            equipment.addEquippedTankToCompany(campaign, company.getCompanyId(), equippedTank);
        }
    }

}
