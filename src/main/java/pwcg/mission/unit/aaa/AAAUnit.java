package pwcg.mission.unit.aaa;

import pwcg.core.exception.PWCGException;
import pwcg.mission.unit.TankPlatoon;
import pwcg.mission.unit.PlatoonInformation;

public class AAAUnit extends TankPlatoon
{
    public AAAUnit(PlatoonInformation platoonInformation)
    {
        super(platoonInformation);
    }
    
    @Override
    protected void createWaypoints() throws PWCGException
    {
        AAAWaypointFactory waypointFactory = new AAAWaypointFactory(this);
        waypoints = waypointFactory.createWaypoints();
    }
}
