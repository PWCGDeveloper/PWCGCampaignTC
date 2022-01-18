package pwcg.mission.unit.assault;

import pwcg.core.exception.PWCGException;
import pwcg.mission.unit.TankPlatoon;
import pwcg.mission.unit.PlatoonInformation;

public class AssaultUnit extends TankPlatoon
{
    public AssaultUnit(PlatoonInformation platoonInformation)
    {
        super(platoonInformation);
    }
    
    @Override
    protected void createWaypoints() throws PWCGException
    {
        AssaultWaypointFactory waypointFactory = new AssaultWaypointFactory(this);
        waypoints = waypointFactory.createWaypoints();
    }
}
