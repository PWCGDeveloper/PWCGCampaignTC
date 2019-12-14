package pwcg.mission.flight;

import pwcg.mission.Mission;
import pwcg.mission.flight.escort.EscortForPlayerFlight;
import pwcg.mission.flight.plane.PlaneMCU;
import pwcg.mission.flight.waypoint.WaypointGeneratorUtils;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.mcu.McuWaypoint;

public class EscortForPlayerValidator
{
    public static void validateEscortForPlayer(Mission mission, Flight flight)
    {
        if (mission.isNightMission())
        {
            EscortForPlayerFlight escortForPlayerFlight = flight.getEscortForPlayer();
            assert(escortForPlayerFlight == null);
        }
        else
        {
            EscortForPlayerFlight escortForPlayerFlight = flight.getEscortForPlayer();
            assert(escortForPlayerFlight != null);
    
            PlaneMCU leadEscortedPlane = flight.getPlanes().get(0);
            assert(escortForPlayerFlight.getCover().getTargets().get(0).equals(new Integer(leadEscortedPlane.getEntity().getIndex()).toString()));
    
            McuWaypoint ingressWP = WaypointGeneratorUtils.findWaypointByType(flight.getAllFlightWaypoints(), WaypointType.INGRESS_WAYPOINT.getName());
            PlaneMCU leadEscortPlane = escortForPlayerFlight.getPlanes().get(0);
            assert(leadEscortPlane.getPosition().getXPos() == ingressWP.getPosition().getXPos());
            assert(leadEscortPlane.getPosition().getZPos() == ingressWP.getPosition().getZPos());
            assert(leadEscortPlane.getPosition().getYPos() > ingressWP.getPosition().getYPos());
        }
    }

    public static void validateNoEscortForPlayer(Flight flight)
    {
        EscortForPlayerFlight escortForPlayerFlight = flight.getEscortForPlayer();
        assert(escortForPlayerFlight == null);
    }
}
