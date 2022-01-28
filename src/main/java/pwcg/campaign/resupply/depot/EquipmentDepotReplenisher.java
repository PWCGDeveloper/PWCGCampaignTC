package pwcg.campaign.resupply.depot;

import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.tank.EquippedTank;
import pwcg.campaign.tank.TankArchType;
import pwcg.campaign.tank.TankEquipmentFactory;
import pwcg.core.exception.PWCGException;

public class EquipmentDepotReplenisher
{
	private Campaign campaign;
	
	public EquipmentDepotReplenisher(Campaign campaign)
	{
		this.campaign = campaign;
	}
	
	public void replenishDepotsForServices () throws PWCGException
	{
        for (Integer serviceId : campaign.getEquipmentManager().getServiceIdsForDepots())
		{
	        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(serviceId);
	        replenishReplacementDepotForService(service);
		}
	}

    private void replenishReplacementDepotForService(ArmedService service) throws PWCGException
    {
        List<Company> companysForService = getCompanysForService(service);
        if (companysForService.size() > 0)
        {
            EquipmentDepot depo = campaign.getEquipmentManager().getEquipmentDepotForService(service.getServiceId());
            addReplacementTanksForService(service, companysForService, depo);
        }
    }

    private List<Company> getCompanysForService(ArmedService service) throws PWCGException 
    {
        CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        return companyManager.getActiveCompaniesForService(campaign.getDate(), service);
    }

    private void addReplacementTanksForService(
            ArmedService service, 
            List<Company> companysForService, 
            EquipmentDepot depot) throws PWCGException
    {
        replaceTanksInDepot(service, companysForService, depot);        
        updateTankReplacementPoints(service, depot);
    }

    private void updateTankReplacementPoints(ArmedService service, EquipmentDepot depot) throws PWCGException
    {
        int newPoints = service.getDailyEquipmentReplacementRate(campaign.getDate());
        int remainingPoints = depot.getEquipmentPoints() % EquipmentDepot.NUM_POINTS_PER_PLANE;
        int updatedEquipmentPoints = newPoints + remainingPoints;
        depot.setEquipmentPoints(updatedEquipmentPoints);
    }

    private void replaceTanksInDepot(ArmedService service, List<Company> companysForService, EquipmentDepot equipmentDepot) throws PWCGException
    {
        EquipmentReplacementCalculator equipmentReplacementCalculator = new EquipmentReplacementCalculator(campaign);
        equipmentReplacementCalculator.createArchTypeForReplacementTank(companysForService);

        int numTanks = equipmentDepot.getEquipmentPoints() / EquipmentDepot.NUM_POINTS_PER_PLANE;
        for (int i = 0; i < numTanks; ++i)
        {
            TankArchType tankArchType = getArchTypeForReplacement(equipmentReplacementCalculator);
            replacePlaneByArchType(service, equipmentDepot, tankArchType);
        }
    }

    private void replacePlaneByArchType(ArmedService service, EquipmentDepot equipmentDepot, TankArchType tankArchType) throws PWCGException
    {
        String tankTypeName = EquipmentReplacementUtils.getTypeForReplacement(campaign.getDate(), tankArchType);
        EquippedTank equippedTank = TankEquipmentFactory.makeTankForDepot(campaign, tankTypeName, service.getCountry().getCountry());
        equipmentDepot.addTankToDepot(equippedTank);
        equipmentDepot.setLastReplacementDate(campaign.getDate());
    }

    private TankArchType getArchTypeForReplacement(EquipmentReplacementCalculator equipmentReplacementCalculator) throws PWCGException
    {
        String archTypeForReplacementTank = "";
        if (equipmentReplacementCalculator.hasMoreForReplacement()) 
        {
            archTypeForReplacementTank = equipmentReplacementCalculator.chooseArchTypeForReplacementByNeed();
        }
        else
        {
            archTypeForReplacementTank = equipmentReplacementCalculator.chooseArchTypeForReplacementByUsage();
        }

        TankArchType tankArchType = PWCGContext.getInstance().getPlayerTankTypeFactory().getTankArchType(archTypeForReplacementTank);
        return tankArchType;
    }
}
