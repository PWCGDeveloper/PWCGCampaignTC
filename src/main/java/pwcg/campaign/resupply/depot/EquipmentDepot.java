package pwcg.campaign.resupply.depot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import pwcg.campaign.tank.Equipment;
import pwcg.campaign.tank.EquippedTank;
import pwcg.campaign.tank.PwcgRoleCategory;
import pwcg.campaign.tank.TankSorter;
import pwcg.core.exception.PWCGException;

public class EquipmentDepot
{
    public static final int NUM_POINTS_PER_PLANE = 10;
    
    private Equipment equipment = new Equipment();
    private int equipmentPoints;
    private Date lastReplacementDate;

    public void setEquippment(Equipment equippedTanks)
    {
        this.equipment = equippedTanks;
    }

    public int getDepotSize()
    {
        return equipment.getAvailableDepotTanks().size();
    }
    
    public void addPlaneToDepot(EquippedTank equippedTank) throws PWCGException
    {
        equipment.addEPlaneToDepot(equippedTank);
    }
    
    public EquippedTank removeBestPlaneFromDepot(List<String> activeArchTypes)
    {
        return equipment.removeBestEquippedFromDepot(activeArchTypes); 
    }

    public EquippedTank removeEquippedPlaneFromDepot(int tankSerialNumber)
    {
        return equipment.removeEquippedTank(tankSerialNumber);
    }

    public EquippedTank getPlaneFromDepot(int tankSerialNumber)
    {
        return equipment.getEquippedTank(tankSerialNumber);
    }

    public List<EquippedTank> getDepotAircraftForRole(PwcgRoleCategory roleCategory) throws PWCGException
    {
        List<EquippedTank> planesInDepotForRole = new ArrayList<>();
        for (EquippedTank equippedTank : equipment.getAvailableDepotTanks().values())
        {
            if (equippedTank.determinePrimaryRoleCategory() == roleCategory)
            {
                planesInDepotForRole.add(equippedTank);
            }
        }
        List<EquippedTank> sortedDepotForRole = TankSorter.sortEquippedTanksByGoodness(planesInDepotForRole);
        return sortedDepotForRole;
    }

    public List<EquippedTank> getAllPlanesInDepot() throws PWCGException
    {
        List<EquippedTank> allPlanesInDepot = new ArrayList<>();
        for (EquippedTank equippedTank : equipment.getAvailableDepotTanks().values())
        {
            allPlanesInDepot.add(equippedTank);
        }
        return allPlanesInDepot;
    }

    public EquippedTank getAnyTankInDepot(int tankSerialNumber) throws PWCGException
    {
        return equipment.getEquippedTank(tankSerialNumber);
    }

    public int getEquipmentPoints()
    {
        return equipmentPoints;
    }

    public void setEquipmentPoints(int equipmentPoints)
    {
        this.equipmentPoints = equipmentPoints;
    }

    public Date getLastReplacementDate()
    {
        return lastReplacementDate;
    }

    public void setLastReplacementDate(Date lastReplacementDate)
    {
        this.lastReplacementDate = lastReplacementDate;
    }

    public EquipmentUpgradeRecord getUpgrade(EquippedTank equippedTank) throws PWCGException
    {
        List<EquippedTank> sortedPlanes = getPlanesForFromDepotBestToWorst(equipment.getAvailableDepotTanks());
        for (EquippedTank depotPlane : sortedPlanes)
        {
            if (isUpgradePlane(depotPlane, equippedTank))
            {
                EquipmentUpgradeRecord upgradeRecord = new EquipmentUpgradeRecord(depotPlane, equippedTank);
                return upgradeRecord;
            }
        }
        return null;
    }
    
    private boolean isUpgradePlane(EquippedTank depotPlane, EquippedTank equippedTank)
    {
        if (!(depotPlane.getArchType().equals(equippedTank.getArchType())))
        {
            return false;
        }
        
        if (!(depotPlane.getGoodness() > equippedTank.getGoodness()))
        {
            return false;
        }
        
        if (equippedTank.isEquipmentRequest())
        {
            return false;
        }

        return true;
    }

    private List<EquippedTank> getPlanesForFromDepotBestToWorst(Map<Integer, EquippedTank> planesForCompany) throws PWCGException
    {
        List<EquippedTank> sortedPlanes = TankSorter.sortEquippedTanksByGoodness(new ArrayList<EquippedTank>(planesForCompany.values()));
        return sortedPlanes;
    }
}
