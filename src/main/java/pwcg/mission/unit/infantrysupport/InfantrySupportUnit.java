package pwcg.mission.unit.infantrysupport;

import pwcg.core.exception.PWCGException;
import pwcg.mission.unit.TankPlatoon;
import pwcg.mission.unit.PlatoonInformation;

public class InfantrySupportUnit extends TankPlatoon
{
    public InfantrySupportUnit(PlatoonInformation platoonInformation)
    {
        super(platoonInformation);
    }
    
    @Override
    protected void createWaypoints() throws PWCGException
    {
        InfantrySupportWaypointFactory waypointFactory = new InfantrySupportWaypointFactory(this);
        waypoints = waypointFactory.createWaypoints();
    }
}
