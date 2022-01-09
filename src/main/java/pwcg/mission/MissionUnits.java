package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.mission.unit.IPlayerUnit;
import pwcg.mission.unit.PlayerUnit;

public class MissionUnits
{
    private List<IPlayerUnit> playerUnits = new ArrayList<>();
    private Mission mission;
    private Campaign campaign;

    MissionUnits(Mission mission)
    {
        this.mission = mission;
        this.campaign = mission.getCampaign();
    }
    
    public void addPlayerUnit(IPlayerUnit playerUnit)
    {
        playerUnits.add(playerUnit);
    }

    public List<Integer> getPlayersInMission()
    {
        return null;
    }

    public List<Integer> determinePlayerVehicleIds()
    {
        return null;
    }

    public List<PlayerUnit> getPlayerUnits()
    {
        return null;
    }

    public List<PlayerUnit> getAiUnits()
    {
        return null;
    }

    public IPlayerUnit getReferencePlayerUnit()
    {
        return null;
    }
    

    public void finalizeMissionUnits() throws PWCGException
    {
        MissionUnitFinalizer unitFinalizer = new MissionUnitFinalizer(campaign, mission);
        unitFinalizer.finalizeMissionUnits();
    }

    public IPlayerUnit getPlayerUnitForCompany(int companyId)
    {
        return null;
    }

}
