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

public class ArmoredAssaultPlatoonRouteBuilder
{
    private GroundUnitCollection assaultFixedUnitCollection;
    private Mission mission;

    public ArmoredAssaultPlatoonRouteBuilder(Mission mission)
    {
        this.mission = mission;
        this.assaultFixedUnitCollection = mission.getGroundUnitBuilder().getAssault();
    }

    public List<Coordinate> buildRoutesForArmor(ITankPlatoon platoon, Coordinate startPosition) throws PWCGException
    {
        Coordinate firstTargetPosition = getFirstTargetPosition(platoon, startPosition);
        Coordinate secondTargetPosition = getSecondTargetPositions(platoon, firstTargetPosition);
        Coordinate thirdTargetPosition = getThirdTargetPositions(platoon, secondTargetPosition);
        Coordinate objectivePosition = getObjectiveTargetPositions(platoon, thirdTargetPosition);
        
        List<Coordinate> platoonWaypoints = new ArrayList<>();
        platoonWaypoints.add(startPosition);
        platoonWaypoints.add(secondTargetPosition);
        platoonWaypoints.add(thirdTargetPosition);
        platoonWaypoints.add(objectivePosition);
        return platoonWaypoints;
    }

    private Coordinate getFirstTargetPosition(ITankPlatoon platoon, Coordinate startPosition) throws PWCGException
    {
        ArmoredAssaultTargetFinder targetFinderInfantry = new ArmoredAssaultTargetFinder(mission, assaultFixedUnitCollection,
                GroundUnitType.INFANTRY_UNIT);

        Coordinate positionNearEnemyMG = targetFinderInfantry.findTargetForTankPlatoon(startPosition, platoon.getPlatoonInformation().getCountry().getSide());
        Coordinate targetPosition = moveABit(positionNearEnemyMG);
        return targetPosition;
    }

    private Coordinate getSecondTargetPositions(ITankPlatoon platoon, Coordinate lastPosition) throws PWCGException
    {
        ArmoredAssaultTargetFinder targetFinderInfantry = new ArmoredAssaultTargetFinder(mission, assaultFixedUnitCollection, 
                GroundUnitType.ANTI_TANK_UNIT);

        Coordinate positionNearEnemyATGuns = targetFinderInfantry.findTargetForTankPlatoon(lastPosition, platoon.getPlatoonInformation().getCountry().getSide().getOppositeSide());
        Coordinate targetPosition = moveABit(positionNearEnemyATGuns);
        return targetPosition;
    }

    private Coordinate getThirdTargetPositions(ITankPlatoon platoon, Coordinate lastPosition) throws PWCGException
    {
        ArmoredAssaultTargetFinder targetFinderInfantry = new ArmoredAssaultTargetFinder(mission, assaultFixedUnitCollection, 
                GroundUnitType.ARTILLERY_UNIT);

        Coordinate positionNearEnemyArtillery = targetFinderInfantry.findTargetForTankPlatoon(lastPosition, platoon.getPlatoonInformation().getCountry().getSide().getOppositeSide());
        Coordinate targetPosition = moveABit(positionNearEnemyArtillery);
        return targetPosition;
    }

    private Coordinate getObjectiveTargetPositions(ITankPlatoon platoon, Coordinate lastObjectivePosition) throws PWCGException
    {
        Coordinate objectivePosition = mission.getObjective().getPosition().copy();
        double angleBeyondObjective = MathUtils.calcAngle(lastObjectivePosition, objectivePosition);
        Coordinate platoonObjectivePosition = MathUtils.calcNextCoord(objectivePosition, angleBeyondObjective, RandomNumberGenerator.getRandom(500));
        Coordinate targetPosition = moveABit(platoonObjectivePosition);

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
