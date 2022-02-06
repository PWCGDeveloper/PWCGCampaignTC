package pwcg.mission.ground.builder;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.GroundUnitType;
import pwcg.mission.platoon.ITankPlatoon;
import pwcg.mission.platoon.tank.TankMcu;

public class ArmoredPlatoonDefensiveResponsiveRouteBuilder
{
    private GroundUnitCollection fixedUnitCollection;
    private Mission mission;

    public ArmoredPlatoonDefensiveResponsiveRouteBuilder(Mission mission)
    {
        this.mission = mission;
        this.fixedUnitCollection = mission.getGroundUnitBuilder().getAssault();
    }

    public void buildResponsiveRoutesForPlatoon(ITankPlatoon platoon) throws PWCGException
    {
        List<Coordinate> responsiveTargetPositions = new ArrayList<>();
        responsiveTargetPositions.addAll(getATGunPositions(platoon));
        responsiveTargetPositions.addAll(getArtilleryPositions(platoon));
        responsiveTargetPositions.add(getObjectivePosition());

        for(Coordinate responsiveTargetPosition : responsiveTargetPositions)
        {
            ArmoredPlatoonResponsiveRoute platoonResponsiveRoute = new ArmoredPlatoonResponsiveRoute();
            platoonResponsiveRoute.buildPointDefenseRouteForArmor(platoon, responsiveTargetPosition);
            platoon.addResponsiveRoute(platoonResponsiveRoute);
        }
    }

    private List<Coordinate> getATGunPositions(ITankPlatoon platoon) throws PWCGException
    {
        ArmoredAssaultTargetFinder targetFinderInfantry = new ArmoredAssaultTargetFinder(mission, fixedUnitCollection,
                GroundUnitType.ANTI_TANK_UNIT);
        List<Coordinate> responsiveTargetPositions = targetFinderInfantry.findUnitsCloseToPosition(platoon.getLeadVehicle().getPosition(),
                platoon.getPlatoonInformation().getCountry().getSide());
        return responsiveTargetPositions;
    }

    private List<Coordinate> getArtilleryPositions(ITankPlatoon platoon) throws PWCGException
    {
        ArmoredAssaultTargetFinder targetFinderInfantry = new ArmoredAssaultTargetFinder(mission, fixedUnitCollection,
                GroundUnitType.ARTILLERY_UNIT);
        List<Coordinate> responsiveTargetPositions = targetFinderInfantry.findUnitsCloseToPosition(platoon.getLeadVehicle().getPosition(),
                platoon.getPlatoonInformation().getCountry().getSide());
        return responsiveTargetPositions;
    }

    private Coordinate getObjectivePosition() throws PWCGException
    {
        return mission.getObjective().getPosition();
    }

    public void triggerResponsiveRoutes(ITankPlatoon platoon, List<ITankPlatoon> assaultingPlatoons)
    {
        List<Integer> triggerVehicles = new ArrayList<>();
        for (ITankPlatoon assaultingPlatoon : assaultingPlatoons)
        {
            for (TankMcu assaultingTank : assaultingPlatoon.getPlatoonTanks().getTanks())
            {
                triggerVehicles.add(assaultingTank.getLinkTrId());
            }
        }

        for (ArmoredPlatoonResponsiveRoute responsiveRoute : platoon.getResponsiveRoutes())
        {
            responsiveRoute.setTriggers(triggerVehicles);
        }

    }

}
