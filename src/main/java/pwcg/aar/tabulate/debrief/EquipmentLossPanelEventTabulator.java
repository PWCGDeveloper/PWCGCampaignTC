package pwcg.aar.tabulate.debrief;

import java.util.Map;

import pwcg.aar.data.AARContext;
import pwcg.aar.ui.display.model.AAREquipmentLossPanelData;
import pwcg.aar.ui.events.TankStatusEventGenerator;
import pwcg.aar.ui.events.model.TankStatusEvent;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class EquipmentLossPanelEventTabulator
{
    private Campaign campaign;
    private AARContext aarContext;

    private AAREquipmentLossPanelData equipmentLossPanelData = new AAREquipmentLossPanelData();

    public EquipmentLossPanelEventTabulator (Campaign campaign,AARContext aarContext)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
    }
        
    public AAREquipmentLossPanelData tabulateForAAREquipmentLossPanel() throws PWCGException
    {
        TankStatusEventGenerator tankLossEventGenerator = new TankStatusEventGenerator(campaign);
        Map<Integer, TankStatusEvent> allTanksLost = tankLossEventGenerator.createTankLossEvents(aarContext.getEquipmentLosses());
        equipmentLossPanelData.setEquipmentLost(allTanksLost);
                
        return equipmentLossPanelData;
    }
}
