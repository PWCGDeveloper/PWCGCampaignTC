package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.mission.unit.ITankUnit;
import pwcg.mission.unit.TankUnit;
import pwcg.mission.unit.TankMcu;

public class MissionUnits
{
    private List<ITankUnit> tankUnits = new ArrayList<>();
    private Mission mission;
    private Campaign campaign;

    MissionUnits(Mission mission)
    {
        this.mission = mission;
        this.campaign = mission.getCampaign();
    }
    
    public void addPlayerUnit(ITankUnit playerUnit)
    {
        tankUnits.add(playerUnit);
    }

    public List<Integer> determinePlayerVehicleIds() throws PWCGException
    {
        List<Integer> playersInMission = new ArrayList<>();
        for (ITankUnit unit : tankUnits)
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

    public List<ITankUnit> getPlayerUnits() throws PWCGException
    {
        List<ITankUnit> playerUnits = new ArrayList<>();
        for (ITankUnit unit : tankUnits)
        {
            for (TankMcu tank : unit.getUnitTanks().getPlayerTanks())
            {
                if (tank.getTankCommander().isPlayer())
                {
                    playerUnits.add(unit);
                }
            }
        }
        return playerUnits;
    }

    public List<TankUnit> getAiUnits()
    {
        return new ArrayList<>();
    }

    public ITankUnit getReferencePlayerUnit()
    {
        return tankUnits.get(0);
    }
    

    public void finalizeMissionUnits() throws PWCGException
    {
        MissionUnitFinalizer unitFinalizer = new MissionUnitFinalizer(campaign, mission);
        unitFinalizer.finalizeMissionUnits();
    }

    public ITankUnit getPlayerUnitForCompany(int companyId) throws PWCGException
    {
        for (ITankUnit unit : getPlayerUnits())
        {
            if (unit.getCompany().getCompanyId() == companyId)
            {
                return unit;
            }
        }
        return null;
    }

}
