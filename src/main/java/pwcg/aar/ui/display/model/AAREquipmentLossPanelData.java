package pwcg.aar.ui.display.model;

import java.util.HashMap;
import java.util.Map;

import pwcg.aar.ui.events.model.TankStatusEvent;

public class AAREquipmentLossPanelData
{
    private Map<Integer, TankStatusEvent> equipmentLost = new HashMap<>();

    public Map<Integer, TankStatusEvent> getEquipmentLost()
    {
        return equipmentLost;
    }

    public void setEquipmentLost(Map<Integer, TankStatusEvent> equipmentLost)
    {
        this.equipmentLost = equipmentLost;
    }

    public void merge(AAREquipmentLossPanelData equipmentLossPanelData)
    {
        equipmentLost.putAll(equipmentLossPanelData.getEquipmentLost());
    }

}
