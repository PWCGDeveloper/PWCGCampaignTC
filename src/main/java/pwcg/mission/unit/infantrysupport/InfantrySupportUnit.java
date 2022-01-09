package pwcg.mission.unit.infantrysupport;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.unit.PlayerUnit;
import pwcg.mission.unit.UnitInformation;

public class InfantrySupportUnit extends PlayerUnit
{
    public InfantrySupportUnit(UnitInformation unitInformation)
    {
        super(unitInformation);
    }
    
    @Override
    protected void createWaypoints() throws PWCGException
    {
        InfantrySupportWaypointFactory missionWaypointFactory = new InfantrySupportWaypointFactory(this);
        IMissionPointSet missionWaypoints = missionWaypointFactory.createWaypoints();
        this.getWaypointPackage().addMissionPointSet(missionWaypoints);
    }
}
