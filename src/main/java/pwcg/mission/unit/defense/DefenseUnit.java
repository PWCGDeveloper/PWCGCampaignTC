package pwcg.mission.unit.defense;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
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
        IMissionPointSet missionWaypoints = missionWaypointFactory.createWaypoints();
        this.getWaypointPackage().addMissionPointSet(missionWaypoints);
    }
}
