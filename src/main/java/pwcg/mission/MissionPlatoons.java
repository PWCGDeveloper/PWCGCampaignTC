package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.mission.unit.ITankPlatoon;
import pwcg.mission.unit.TankMcu;
import pwcg.mission.unit.TankPlatoon;

public class MissionPlatoons
{
    private List<ITankPlatoon> tankPlatoons = new ArrayList<>();
    private Mission mission;
    private Campaign campaign;

    MissionPlatoons(Mission mission)
    {
        this.mission = mission;
        this.campaign = mission.getCampaign();
    }
    
    public void addPlatoon(ITankPlatoon playerPlatoon)
    {
        tankPlatoons.add(playerPlatoon);
    }

    public List<Integer> determinePlayerVehicleIds() throws PWCGException
    {
        List<Integer> playersInMission = new ArrayList<>();
        for (ITankPlatoon unit : tankPlatoons)
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
        for (ITankPlatoon unit : tankPlatoons)
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

    public List<TankPlatoon> getAiUnits()
    {
        return new ArrayList<>();
    }

    public ITankPlatoon getReferencePlayerUnit()
    {
        return tankPlatoons.get(0);
    }
    

    public void finalizeMissionUnits() throws PWCGException
    {
        MissionUnitFinalizer unitFinalizer = new MissionUnitFinalizer(campaign, mission);
        unitFinalizer.finalizeMissionUnits();
    }

    public ITankPlatoon getPlayerUnitForCompany(int companyId) throws PWCGException
    {
        for (ITankPlatoon unit : getPlayerUnits())
        {
            if (unit.getCompany().getCompanyId() == companyId)
            {
                return unit;
            }
        }
        return null;
    }

}
