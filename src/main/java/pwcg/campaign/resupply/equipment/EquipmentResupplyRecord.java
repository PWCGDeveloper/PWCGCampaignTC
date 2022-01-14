package pwcg.campaign.resupply.equipment;

import pwcg.campaign.tank.EquippedTank;

public class EquipmentResupplyRecord
{
    private EquippedTank equippedTank;
    private int transferTo;

    public EquipmentResupplyRecord(EquippedTank equippedTank, int transferTo)
    {
        this.equippedTank  = equippedTank;
        this.transferTo  = transferTo;
    }
    
    public EquippedTank getEquippedPlane()
    {
        return equippedTank;
    }

    public int getTransferTo()
    {
        return transferTo;
    }
}
