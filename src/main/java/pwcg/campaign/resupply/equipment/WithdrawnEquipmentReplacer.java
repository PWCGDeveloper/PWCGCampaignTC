package pwcg.campaign.resupply.equipment;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.resupply.depot.EquipmentReplacementUtils;
import pwcg.campaign.tank.Equipment;
import pwcg.campaign.tank.EquippedTank;
import pwcg.campaign.tank.TankArchType;
import pwcg.campaign.tank.TankEquipmentFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.core.utils.RandomNumberGenerator;

public class WithdrawnEquipmentReplacer
{
    private Campaign campaign;
    private Equipment equipment;
    private Company company;
    
    public WithdrawnEquipmentReplacer(Campaign campaign, Equipment equipment, Company company)
    {
        this.campaign = campaign;
        this.equipment = equipment;
        this.company = company;
    }
    
    public int replaceWithdrawnEquipment() throws PWCGException
    {
        int planesRemoved = removeWithdrawnPlanes();
        int planesAdded = replaceWithNewPlanes(planesRemoved);
        return planesAdded;
    }

    private int removeWithdrawnPlanes()
    {
        int planesRemoved = 0;
        for (EquippedTank plane: equipment.getActiveEquippedTanks().values())
        {
            if (isWithdrawPlane(plane))
            {
                equipment.deactivateEquippedTankFromCompany(plane.getSerialNumber(), campaign.getDate());
                ++planesRemoved;
            }
        }

        return planesRemoved;
    }
    
    private boolean isWithdrawPlane(EquippedTank plane)
    {
        if (campaign.getDate().before(plane.getWithdrawal()))
        {
            return false;
        }
        
        if (plane.isEquipmentRequest())
        {
            return false;
        }
        
        return true;
    }
    
    private int replaceWithNewPlanes(int planesRemoved) throws PWCGException
    {
        int numberOfPlanesToAdd = calculatePlanesNeeded(planesRemoved);
        for (int i = 0; i < numberOfPlanesToAdd; ++i)
        {
            String planeTypeName = determineTankType();
            if (!planeTypeName.isEmpty())
            {
                addPlaneToCompany(planeTypeName);
            }
        }
        
        return numberOfPlanesToAdd;
    }

    private int calculatePlanesNeeded(int planesRemoved)
    {
        int minNeeded = Company.MIN_REEQUIPMENT_SIZE - equipment.getActiveEquippedTanks().size();
        int numNeeded = planesRemoved;
        if (minNeeded > planesRemoved)
        {
            numNeeded = minNeeded;
        }
        
        return numNeeded;
    }

    private String determineTankType() throws PWCGException
    {
        String planeArchTypeName = chooseArchTypeForCompany();
        if (!planeArchTypeName.isEmpty())
        {
            TankArchType planeArchType = PWCGContext.getInstance().getTankTypeFactory().getTankArchType(planeArchTypeName);
            String planeTypeName = EquipmentReplacementUtils.getTypeForReplacement(campaign.getDate(), planeArchType);
            return planeTypeName;
        }
        else
        {
            return "";
        }
    }
    
    private String chooseArchTypeForCompany() throws PWCGException
    {
        List<String> archTypes = determineAvailableArchTypes();
        if (archTypes.size() > 0)
        {
            int index = RandomNumberGenerator.getRandom(archTypes.size());
            return archTypes.get(index);
        }
        else
        {
            return "";
        }
    }

    private List<String> determineAvailableArchTypes() throws PWCGException
    {
        List<String> availableArchTypes = new ArrayList<>();
        for (String planeArchTypeName : equipment.getArchTypes())
        {
            TankArchType planeArchType = PWCGContext.getInstance().getTankTypeFactory().getTankArchType(planeArchTypeName);
            String planeTypeName = EquipmentReplacementUtils.getTypeForReplacement(campaign.getDate(), planeArchType);
            if (planeTypeName != null && !planeTypeName.isEmpty())
            {
                availableArchTypes.add(planeArchTypeName);
            }
            else
            {
                PWCGLogger.log(LogLevel.DEBUG, "");
            }
        }
        return availableArchTypes;
    }

    private void addPlaneToCompany(String planeTypeName) throws PWCGException
    {
        EquippedTank equippedPlane = TankEquipmentFactory.makeTankForCompany(campaign, planeTypeName, company.getCompanyId());
        equipment.addEquippedTankToCompany(campaign, company.getCompanyId(), equippedPlane);
    }
}
