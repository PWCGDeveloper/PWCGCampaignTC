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
    
    public void addTankToDepot(EquippedTank equippedTank) throws PWCGException
    {
        equipment.addTankToDepot(equippedTank);
    }
    
    public EquippedTank removeBestTankFromDepot(List<String> activeArchTypes)
    {
        return equipment.removeBestEquippedFromDepot(activeArchTypes); 
    }

    public EquippedTank removeEquippedTankFromDepot(int tankSerialNumber)
    {
        return equipment.removeEquippedTank(tankSerialNumber);
    }

    public EquippedTank getTankFromDepot(int tankSerialNumber)
    {
        return equipment.getEquippedTank(tankSerialNumber);
    }

    public List<EquippedTank> getDepotAircraftForRole(PwcgRoleCategory roleCategory) throws PWCGException
    {
        List<EquippedTank> tanksInDepotForRole = new ArrayList<>();
        for (EquippedTank equippedTank : equipment.getAvailableDepotTanks().values())
        {
            if (equippedTank.determinePrimaryRoleCategory() == roleCategory)
            {
                tanksInDepotForRole.add(equippedTank);
            }
        }
        List<EquippedTank> sortedDepotForRole = TankSorter.sortEquippedTanksByGoodness(tanksInDepotForRole);
        return sortedDepotForRole;
    }

    public List<EquippedTank> getAllTanksInDepot() throws PWCGException
    {
        List<EquippedTank> allTanksInDepot = new ArrayList<>();
        for (EquippedTank equippedTank : equipment.getAvailableDepotTanks().values())
        {
            allTanksInDepot.add(equippedTank);
        }
        return allTanksInDepot;
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
        List<EquippedTank> sortedTanks = getTanksForFromDepotBestToWorst(equipment.getAvailableDepotTanks());
        for (EquippedTank depotTank : sortedTanks)
        {
            if (isUpgradeTank(depotTank, equippedTank))
            {
                EquipmentUpgradeRecord upgradeRecord = new EquipmentUpgradeRecord(depotTank, equippedTank);
                return upgradeRecord;
            }
        }
        return null;
    }
    
    private boolean isUpgradeTank(EquippedTank depotTank, EquippedTank equippedTank)
    {
        if (!(depotTank.getArchType().equals(equippedTank.getArchType())))
        {
            return false;
        }
        
        if (!(depotTank.getGoodness() > equippedTank.getGoodness()))
        {
            return false;
        }
        
        if (equippedTank.isEquipmentRequest())
        {
            return false;
        }

        return true;
    }

    private List<EquippedTank> getTanksForFromDepotBestToWorst(Map<Integer, EquippedTank> tanksForCompany) throws PWCGException
    {
        List<EquippedTank> sortedTanks = TankSorter.sortEquippedTanksByGoodness(new ArrayList<EquippedTank>(tanksForCompany.values()));
        return sortedTanks;
    }
}
