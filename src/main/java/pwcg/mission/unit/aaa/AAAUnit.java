package pwcg.mission.unit.aaa;

import pwcg.core.exception.PWCGException;
import pwcg.mission.unit.TankUnit;
import pwcg.mission.unit.UnitInformation;

public class AAAUnit extends TankUnit
{
    public AAAUnit(UnitInformation unitInformation)
    {
        super(unitInformation);
    }
    
    @Override
    protected void createWaypoints() throws PWCGException
    {
        AAAWaypointFactory waypointFactory = new AAAWaypointFactory(this);
        waypoints = waypointFactory.createWaypoints();
    }
}
