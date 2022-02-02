package pwcg.mission.ground;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.MissionUnitFinalizer;
import pwcg.mission.platoon.ITankPlatoon;
import pwcg.mission.platoon.tank.TankMcu;

public class MissionPlatoons
{
    private Map<Integer, ITankPlatoon> tankPlatoons = new HashMap<>();
    private Mission mission;
    private Campaign campaign;

    public MissionPlatoons(Mission mission)
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
        for (ITankPlatoon platoon : tankPlatoons.values())
        {
            for (TankMcu tank : platoon.getPlatoonTanks().getPlayerTanks())
            {
                if (tank.getTankCommander().isPlayer())
                {
                    playersInMission.add(tank.getLinkTrId());
                }
            }
        }
        return playersInMission;
    }

    public List<ITankPlatoon> getPlayerPlatoons() throws PWCGException
    {
        List<ITankPlatoon> playerPlatoons = new ArrayList<>();
        for (ITankPlatoon platoon : tankPlatoons.values())
        {
            if (platoon.isPlayerPlatoon())
            {
                playerPlatoons.add(platoon);
            }
        }
        return playerPlatoons;
    }

    public List<ITankPlatoon> getAiPlatoonsForSide(Side side) throws PWCGException
    {
        List<ITankPlatoon> aiPlatoons = new ArrayList<>();
        for (ITankPlatoon platoon : tankPlatoons.values())
        {
            if (!platoon.isPlayerPlatoon())
            {
                aiPlatoons.add(platoon);
            }
        }
        return aiPlatoons;
    }

    public ITankPlatoon getReferencePlayerPlatoon() throws PWCGException
    {
        for (ITankPlatoon platoon : getPlayerPlatoons())
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
        MissionUnitFinalizer platoonFinalizer = new MissionUnitFinalizer(campaign, mission);
        platoonFinalizer.finalizeMissionUnits();
    }

    public ITankPlatoon getPlayerUnitForCompany(int companyId) throws PWCGException
    {
        for (ITankPlatoon platoon : getPlayerPlatoons())
        {
            if (platoon.getCompany().getCompanyId() == companyId)
            {
                return platoon;
            }
        }
        return null;
    }

    public List<ITankPlatoon> getPlatoonsForSide(Side side) throws PWCGException
    {
        List<ITankPlatoon> playerPlatoons = new ArrayList<>();
        for (ITankPlatoon platoon : tankPlatoons.values())
        {
            if (platoon.getCompany().getCountry().getSide() == side)
            {
                playerPlatoons.add(platoon);
            }
        }
        return playerPlatoons;
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
