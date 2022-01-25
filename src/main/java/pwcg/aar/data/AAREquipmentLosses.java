package pwcg.aar.data;

import java.util.HashMap;
import java.util.Map;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTank;

public class AAREquipmentLosses
{
    private Map<Integer, LogTank> tanksDestroyed = new HashMap<>();

    public void merge(AAREquipmentLosses equipmentEvents)
    {
        tanksDestroyed.putAll(equipmentEvents.getTanksDestroyed());
    }

    public void addTankDestroyed(LogTank tank)
    {
        this.tanksDestroyed.put(tank.getTankSerialNumber(), tank);
    }

    public Map<Integer, LogTank> getTanksDestroyed()
    {
        return tanksDestroyed;
    }
    
    
}
