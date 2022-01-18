package pwcg.mission.unit.defense;

import pwcg.core.exception.PWCGException;
import pwcg.mission.unit.TankPlatoon;
import pwcg.mission.unit.PlatoonInformation;

public class DefenseUnit extends TankPlatoon
{
    public DefenseUnit(PlatoonInformation platoonInformation)
    {
        super(platoonInformation);
    }
    
    @Override
    protected void createWaypoints() throws PWCGException
    {
        DefenseWaypointFactory missionWaypointFactory = new DefenseWaypointFactory(this);
        waypoints = missionWaypointFactory.createWaypoints();
    }
}
