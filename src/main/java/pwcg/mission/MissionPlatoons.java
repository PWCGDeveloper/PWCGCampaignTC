package pwcg.mission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.mission.platoon.ITankPlatoon;
import pwcg.mission.platoon.PlayerTankPlatoon;
import pwcg.mission.platoon.tank.TankMcu;

public class MissionPlatoons
{
    private Map<Integer, ITankPlatoon> tankPlatoons = new HashMap<>();
    private Mission mission;
    private Campaign campaign;

    MissionPlatoons(Mission mission)
    {
        this.mission = mission;
        this.campaign = mission.getCampaign();
    }
    
    public void addPlatoon(ITankPlatoon platoon)
    {
        tankPlatoons.put(platoon.getIndex(), platoon);
    }

    public List<Integer> determinePlayerVehicleIds() throws PWCGException
    {
        List<Integer> playersInMission = new ArrayList<>();
        for (ITankPlatoon unit : tankPlatoons.values())
        {
            for (TankMcu tank : unit.getUnitTanks().getPlayerTanks())
            {
                if (tank.getTankCommander().isPlayer())
                {
                    playersInMission.add(tank.getLinkTrId());
                }
            }
        }
        return playersInMission;
    }

    public List<ITankPlatoon> getPlayerUnits() throws PWCGException
    {
        List<ITankPlatoon> playerPlatoons = new ArrayList<>();
        for (ITankPlatoon unit : tankPlatoons.values())
        {
            for (TankMcu tank : unit.getUnitTanks().getPlayerTanks())
            {
                if (tank.getTankCommander().isPlayer())
                {
                    playerPlatoons.add(unit);
                }
            }
        }
        return playerPlatoons;
    }

    public List<PlayerTankPlatoon> getAiUnits()
    {
        return new ArrayList<>();
    }

    public ITankPlatoon getReferencePlayerUnit() throws PWCGException
    {
        for (ITankPlatoon platoon : getPlayerUnits())
        {
            if (platoon.isPlayerPlatoon())
            {
                return platoon;
            }
        }
        
        throw new PWCGException("No player platoon found in mission");
    }
    
    public void finalizeMissionUnits() throws PWCGException
    {
        MissionUnitFinalizer unitFinalizer = new MissionUnitFinalizer(campaign, mission);
        unitFinalizer.finalizeMissionUnits();
    }

    public ITankPlatoon getPlayerUnitForCompany(int companyId) throws PWCGException
    {
        for (ITankPlatoon platoon : getPlayerUnits())
        {
            if (platoon.getCompany().getCompanyId() == companyId)
            {
                return platoon;
            }
        }
        return null;
    }

    public ITankPlatoon getPlatoon(int index)
    {
        return tankPlatoons.get(index);
    }
    
    public List<ITankPlatoon> getPlatoons()
    {
        return new ArrayList<>(tankPlatoons.values());
    }
}
