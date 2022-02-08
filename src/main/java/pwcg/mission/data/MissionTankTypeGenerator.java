package pwcg.mission.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pwcg.mission.Mission;
import pwcg.mission.platoon.ITankPlatoon;
import pwcg.mission.platoon.tank.TankMcu;

public class MissionTankTypeGenerator
{
    private Mission mission;
    private Set<String> tankTypes = new HashSet<>();

    public MissionTankTypeGenerator(Mission mission)
    {
        this.mission = mission;
    }


    public List<String> generateTankTypesInMission()
    {
        for (ITankPlatoon unit : mission.getPlatoons().getPlatoons())
        {
            makeTankEntriesForPlatoon(unit);
        }

        return new ArrayList<>(tankTypes);
    }

    private void makeTankEntriesForPlatoon(ITankPlatoon platoon)
    {
        for (TankMcu tank : platoon.getPlatoonTanks().getTanks())
        {
            tankTypes.add(tank.getName());
        }
    }
}
