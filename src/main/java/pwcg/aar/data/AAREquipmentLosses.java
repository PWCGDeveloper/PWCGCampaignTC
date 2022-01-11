package pwcg.aar.data;

import java.util.HashMap;
import java.util.Map;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTank;

public class AAREquipmentLosses
{
    private Map<Integer, LogTank> planesDestroyed = new HashMap<>();

    public void merge(AAREquipmentLosses equipmentEvents)
    {
        planesDestroyed.putAll(equipmentEvents.getPlanesDestroyed());
    }

    public void addPlaneDestroyed(LogTank shotDownPlane)
    {
        this.planesDestroyed.put(shotDownPlane.getTankSerialNumber(), shotDownPlane);
    }

    public Map<Integer, LogTank> getPlanesDestroyed()
    {
        return planesDestroyed;
    }
    
    
}
