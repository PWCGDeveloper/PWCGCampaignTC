package pwcg.mission.unit.infantrysupport;

import pwcg.core.exception.PWCGException;
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
        InfantrySupportWaypointFactory waypointFactory = new InfantrySupportWaypointFactory(this);
        waypoints = waypointFactory.createWaypoints();
    }
}
