package pwcg.aar.campaign.update;

import pwcg.aar.data.CampaignUpdateData;
import pwcg.campaign.Campaign;
import pwcg.campaign.resupply.equipment.EquipmentResupplyRecord;
import pwcg.campaign.tank.Equipment;
import pwcg.campaign.tank.EquippedTank;
import pwcg.campaign.tank.TankStatus;
import pwcg.core.exception.PWCGException;

public class EquipmentUpdater 
{
	private Campaign campaign;
    private CampaignUpdateData campaignUpdateData;

	public EquipmentUpdater (Campaign campaign, CampaignUpdateData campaignUpdateData) 
	{
        this.campaign = campaign;
        this.campaignUpdateData = campaignUpdateData;
	}
	
    public void equipmentUpdatesForCompanys() throws PWCGException 
    {
        equipmentRemovals();
        equipmentAdditions();
    }

    private void equipmentRemovals() throws PWCGException
    {
        for (Integer tankSerialNumber : campaignUpdateData.getEquipmentLosses().getPlanesDestroyed().keySet())
        {
            EquippedTank equippedTank = campaign.getEquipmentManager().getAnyTankWithPreference(tankSerialNumber);
            equippedTank.setTankStatus(TankStatus.STATUS_DESTROYED);
            equippedTank.setDateRemovedFromService(campaign.getDate());
        }
    }

    private void equipmentAdditions() throws PWCGException
    {
        for (EquipmentResupplyRecord equipmentResupplyRecord : campaignUpdateData.getResupplyData().getEquipmentResupplyData().getEquipmentResupplied())
        {
            Equipment equipment = campaign.getEquipmentManager().getEquipmentForCompany(equipmentResupplyRecord.getTransferTo());
            EquippedTank replacementPlane = equipmentResupplyRecord.getEquippedPlane();
            replacementPlane.setCompanyId(equipmentResupplyRecord.getTransferTo());
            replacementPlane.setTankStatus(TankStatus.STATUS_DEPLOYED);
            equipment.addEquippedTankToCompany(campaign, equipmentResupplyRecord.getTransferTo(), replacementPlane);
        }
    }
 }
