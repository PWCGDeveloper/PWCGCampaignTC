package pwcg.mission.ground.builder;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.mcu.group.MissionBeginSelfDeactivatingCheckZone;
import pwcg.mission.platoon.ITankPlatoon;

public class ArmoredPlatoonResponsiveRoute
{
    private MissionBeginSelfDeactivatingCheckZone missionBeginUnitCheckZone;
    private McuTimer activateTimer = new McuTimer();
    private McuWaypoint responseWaypoint;

    public ArmoredPlatoonResponsiveRoute()
    {
    }

    public void buildPointDefenseRouteForArmor(ITankPlatoon platoon, Coordinate triggerPosition) throws PWCGException
    {
        missionBeginUnitCheckZone = new MissionBeginSelfDeactivatingCheckZone("Armor Defense Check Zone", triggerPosition, 800);

        int reactionTime = 30 + RandomNumberGenerator.getRandom(300);
        activateTimer.setTime(reactionTime);

        responseWaypoint = WaypointFactory.createObjectiveWaypointType();
        responseWaypoint.setTriggerArea(200);
        responseWaypoint.setDesc("Responsive Waypoint");
        responseWaypoint.setSpeed(platoon.getLeadVehicle().getCruisingSpeed());
        responseWaypoint.setPosition(triggerPosition.copy());
        responseWaypoint.setTargetWaypoint(true);

        createTargetAssociations();
    }


    private void createTargetAssociations()
    {
        missionBeginUnitCheckZone.linkCheckZoneTarget(activateTimer.getIndex());
        activateTimer.setTarget(responseWaypoint.getIndex());
    }

    public void write(BufferedWriter writer) throws PWCGException
    {
        missionBeginUnitCheckZone.write(writer);
        activateTimer.write(writer);
        responseWaypoint.write(writer);
    }

    public void setTriggers(List<Integer> triggerVehicles)
    {
        for (int triggerVehicle : triggerVehicles)
        {
            missionBeginUnitCheckZone.setCheckZoneTriggerObject(triggerVehicle);
        }
    }

    public Coordinate getTargetPosition()
    {
        return responseWaypoint.getPosition();
    }
}
