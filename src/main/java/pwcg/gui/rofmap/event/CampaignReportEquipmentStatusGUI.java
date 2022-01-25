package pwcg.gui.rofmap.event;

import pwcg.aar.ui.events.model.TankStatusEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.tank.TankStatus;
import pwcg.core.exception.PWCGException;

public class CampaignReportEquipmentStatusGUI extends AARDocumentIconPanel
{
	private static final long serialVersionUID = 1L;
    private TankStatusEvent equipmentStatusEvent;
    private Campaign campaign;
	
	public CampaignReportEquipmentStatusGUI(Campaign campaign, TankStatusEvent crewMemberLostEvent) throws PWCGException
	{
		super();
        this.campaign = campaign;
        this.equipmentStatusEvent = crewMemberLostEvent;
        makePanel();        
	}

    protected String getHeaderText() throws PWCGException
    {
        String header = "";
        if (equipmentStatusEvent.getTankStatus() == TankStatus.STATUS_DESTROYED)
        {
            header = "Notification of Aircraft Loss";
        }
        if (equipmentStatusEvent.getTankStatus() == TankStatus.STATUS_DEPOT)
        {
            header = "Notification of Aircraft Resupply";
        }
        if (equipmentStatusEvent.getTankStatus() == TankStatus.STATUS_REMOVED_FROM_SERVICE)
        {
            header = "Notification of Aircraft Removal From Service";
        }
        
        return header;
    }

    protected String getBodyText() throws PWCGException
    {
        String planeEventText = "";
        if (equipmentStatusEvent.getTankStatus() == TankStatus.STATUS_DESTROYED)
        {
            planeEventText = equipmentStatusEvent.getTankLostText(campaign);
        }
        if (equipmentStatusEvent.getTankStatus() == TankStatus.STATUS_DEPOT)
        {
            planeEventText = equipmentStatusEvent.getTankAddedToDepotText(campaign);
        }
        if (equipmentStatusEvent.getTankStatus() == TankStatus.STATUS_REMOVED_FROM_SERVICE)
        {
            planeEventText = equipmentStatusEvent.getTankWithdrawnFromServiceText(campaign);
        }
        
        
        return planeEventText;
    }

    public Campaign getCampaign()
    {
        return campaign;
    }

    @Override
    public void finished()
    {
    }

    @Override
    protected String getFooterImagePath() throws PWCGException
    {
        return "";
    }
}
