package pwcg.mission.unit.defense;

import pwcg.core.exception.PWCGException;
import pwcg.mission.unit.PlayerUnit;
import pwcg.mission.unit.UnitInformation;

public class DefenseUnit extends PlayerUnit
{
    public DefenseUnit(UnitInformation unitInformation)
    {
        super(unitInformation);
    }
    
    @Override
    protected void createWaypoints() throws PWCGException
    {
        DefenseWaypointFactory missionWaypointFactory = new DefenseWaypointFactory(this);
        waypoints = missionWaypointFactory.createWaypoints();
    }
}
