package pwcg.mission.ground.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.ground.MissionPlatoons;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.platoon.ITankPlatoon;

public class MissionPlatoonBuilder extends PlatoonBuilderBase implements IPlatoonBuilder
{
    public MissionPlatoonBuilder(Mission mission)
    {
        super(mission);
    }

    @Override
    public MissionPlatoons createPlatoons() throws PWCGException
    {
        missionPlatoons = new MissionPlatoons(mission);
        buildPlatoonsForSide(Side.ALLIED);
        buildPlatoonsForSide(Side.AXIS);

        createAssaultingWaypoints();
        createDefendingingWaypoints();

        buildResponsiveRoutes();

        return missionPlatoons;
    }

    private void createAssaultingWaypoints() throws PWCGException
    {
        ArmoredAssaultRouteBuilder assaultRouteBuilder = new ArmoredAssaultRouteBuilder(mission);
        Map<Integer, List<Coordinate>> assaultRoutes = assaultRouteBuilder.buildAssaultRoutesForArmor(missionPlatoons.getPlatoons());
        for (int index : assaultRoutes.keySet())
        {
            List<Coordinate> waypointCoordinates = assaultRoutes.get(index);
            ITankPlatoon platoon = missionPlatoons.getPlatoon(index);

            createStartPosition(waypointCoordinates, platoon);

            List<McuWaypoint> assaultWaypoints = createWaypoints(waypointCoordinates, platoon.getLeadVehicle().getCruisingSpeed());
            platoon.setWaypoints(assaultWaypoints);
        }
    }

    private void createDefendingingWaypoints() throws PWCGException
    {
        ArmoredDefenseRouteBuilder defenseRouteBuilder = new ArmoredDefenseRouteBuilder(mission);
        Map<Integer, List<Coordinate>> defenseRoutes = defenseRouteBuilder.buildAssaultRoutesForArmor(missionPlatoons.getPlatoons());
        for (int index : defenseRoutes.keySet())
        {
            List<Coordinate> waypointCoordinates = defenseRoutes.get(index);
            ITankPlatoon platoon = missionPlatoons.getPlatoon(index);

            createStartPosition(waypointCoordinates, platoon);

            List<McuWaypoint> defenseWaypoints = createWaypoints(waypointCoordinates, platoon.getLeadVehicle().getCruisingSpeed());
            platoon.setWaypoints(defenseWaypoints);
        }
    }

    private void buildResponsiveRoutes() throws PWCGException
    {
        List<ITankPlatoon> defendingPlatoons = missionPlatoons.getPlatoonsForSide(mission.getObjective().getDefendingCountry().getSide());
        for (ITankPlatoon defendingPlatoon : defendingPlatoons)
        {
            ArmoredPlatoonDefensiveResponsiveRouteBuilder responsiveRouteBuilder = new ArmoredPlatoonDefensiveResponsiveRouteBuilder(mission);
            responsiveRouteBuilder.buildResponsiveRoutesForPlatoon(defendingPlatoon);

            List<ITankPlatoon> assaultingPlatoons = missionPlatoons.getPlatoonsForSide(mission.getObjective().getAssaultingCountry().getSide());
            responsiveRouteBuilder.triggerResponsiveRoutes(defendingPlatoon, assaultingPlatoons);
        }
    }

    private void createStartPosition(List<Coordinate> waypointCoordinates, ITankPlatoon platoon) throws PWCGException
    {
        Coordinate startPosition = waypointCoordinates.get(0);
        Coordinate towardsPosition = waypointCoordinates.get(1);
        platoon.setStartPosition(startPosition, towardsPosition);
    }

    private List<McuWaypoint> createWaypoints(List<Coordinate> assaultWaypointCoordinates, int platoonSpeed) throws PWCGException
    {
        List<McuWaypoint> assaultWaypoints = new ArrayList<>();
        McuWaypoint previousWaypoint = null;
        for(int i = 1; i < assaultWaypointCoordinates.size(); ++i)
        {
            Coordinate assaultWaypointCoordinate = assaultWaypointCoordinates.get(i);
            McuWaypoint waypoint;
            waypoint = WaypointFactory.createObjectiveWaypointType();
            waypoint.setTriggerArea(200);
            waypoint.setDesc("Assault Waypoint");
            waypoint.setSpeed(platoonSpeed);
            waypoint.setPosition(assaultWaypointCoordinate.copy());
            waypoint.setTargetWaypoint(true);

            double angle = MathUtils.calcAngle(assaultWaypointCoordinates.get(i-1), assaultWaypointCoordinate);
            waypoint.setOrientation(new Orientation(angle));

            if (previousWaypoint != null)
            {
                previousWaypoint.setTarget(waypoint.getIndex());
            }
            previousWaypoint = waypoint;

            assaultWaypoints.add(waypoint);
        }


        return assaultWaypoints;
    }
}
