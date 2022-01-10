package pwcg.mission.unit.assault;

import pwcg.core.exception.PWCGException;
import pwcg.mission.unit.TankUnit;
import pwcg.mission.unit.UnitInformation;

public class AssaultUnit extends TankUnit
{
    public AssaultUnit(UnitInformation unitInformation)
    {
        super(unitInformation);
    }
    
    @Override
    protected void createWaypoints() throws PWCGException
    {
        AssaultWaypointFactory waypointFactory = new AssaultWaypointFactory(this);
        waypoints = waypointFactory.createWaypoints();
    }
}
