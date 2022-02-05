package pwcg.mission.ground.builder;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.GroundUnitType;
import pwcg.mission.platoon.ITankPlatoon;

public class ArmoredDefensePlatoonRouteBuilder
{
    private GroundUnitCollection fixedUnitCollection;
    private Mission mission;

    public ArmoredDefensePlatoonRouteBuilder(Mission mission)
    {
        this.mission = mission;
        this.fixedUnitCollection = mission.getGroundUnitBuilder().getAssault();
    }

    public List<Coordinate> buildRoutesForArmor(ITankPlatoon platoon, Coordinate startPosition) throws PWCGException
    {
        Coordinate firstTargetPosition = getFirstTargetPositions(platoon, startPosition);

        List<Coordinate> platoonWaypoints = new ArrayList<>();
        platoonWaypoints.add(startPosition);
        platoonWaypoints.add(firstTargetPosition);

        return platoonWaypoints;
    }

    private Coordinate getFirstTargetPositions(ITankPlatoon platoon, Coordinate startPosition) throws PWCGException
    {
        ArmoredAssaultTargetFinder targetFinderInfantry = new ArmoredAssaultTargetFinder(mission, fixedUnitCollection,
                GroundUnitType.ANTI_TANK_UNIT);

        Coordinate positionNearEnemyMG = targetFinderInfantry.findTargetForTankPlatoon(startPosition, platoon.getPlatoonInformation().getCountry().getSide());
        Coordinate targetPosition = moveABit(positionNearEnemyMG);
        return targetPosition;
    }

    private Coordinate moveABit(Coordinate positionNearEnemyATGuns) throws PWCGException
    {
        int angle = RandomNumberGenerator.getRandom(360);
        int numMeters = RandomNumberGenerator.getRandom(100);
        Coordinate targetPosition = MathUtils.calcNextCoord(positionNearEnemyATGuns, angle, numMeters);
        return targetPosition;
    }
}
