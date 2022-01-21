package pwcg.mission.ground.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.GroundUnitType;
import pwcg.mission.ground.org.IGroundUnit;

public class ArmoredDefenseTargetFinder
{
    private GroundUnitCollection assaultFixedUnitCollection;
    private Mission mission;
    private GroundUnitType groundUnitType;
    private Side infantryTargetSide;
    private List<Coordinate> initialTargetPositions = new ArrayList<>();;

    public ArmoredDefenseTargetFinder(Mission mission, GroundUnitCollection assaultFixedUnitCollection, Side side, GroundUnitType groundUnitType)
    {
        this.mission = mission;
        this.groundUnitType = groundUnitType;
        this.assaultFixedUnitCollection = assaultFixedUnitCollection;
        this.infantryTargetSide = side;
    }

    public List<Coordinate> findInitialTargetForTankPlatoon(int numTankPlatoons) throws PWCGException
    {
        List<IGroundUnit> targetUnitsForSide = assaultFixedUnitCollection.getGroundUnitsByTypeAndSide(groundUnitType, infantryTargetSide);
        addFirstTarget(targetUnitsForSide);
        addNextTargets(targetUnitsForSide, numTankPlatoons);
        return initialTargetPositions;
    }

    private void addFirstTarget(List<IGroundUnit> targetUnitsForSide) throws PWCGException
    {
        IGroundUnit firstUnit = findUnitClosestToObjective(targetUnitsForSide, mission.getObjective().getPosition());
        initialTargetPositions.add(firstUnit.getPosition());
    }

    private IGroundUnit findUnitClosestToObjective(List<IGroundUnit> targetUnitsForSide, Coordinate objectivePosition) throws PWCGException
    {
        IGroundUnit closestUnit = targetUnitsForSide.get(0);
        double closestUnitDistance = 100000000.0;
        for (IGroundUnit targetUnit : targetUnitsForSide)
        {
            double distanceToObjective = MathUtils.calcDist(targetUnit.getPosition(), objectivePosition);
            if (distanceToObjective < closestUnitDistance)
            {
                closestUnitDistance = distanceToObjective;
                closestUnit = targetUnit;
            }
        }
        return closestUnit;
    }

    private void addNextTargets(List<IGroundUnit> targetUnitsForSide, int numTankPlatoons) throws PWCGException
    {
        List<IGroundUnit> sortedTargetUnitsForSide = sortByClosestToFirstTarget(targetUnitsForSide);

        for (int i = 1; i < numTankPlatoons; ++i)
        {
            int index = i;
            if (index >= sortedTargetUnitsForSide.size())
            {
                index = sortedTargetUnitsForSide.size() % i;
            }

            initialTargetPositions.add(sortedTargetUnitsForSide.get(index).getPosition());
        }
    }

    private List<IGroundUnit> sortByClosestToFirstTarget(List<IGroundUnit> targetUnitsForSide) throws PWCGException
    {
        Map<Double, IGroundUnit> sortedTargetUnitsForSide = new TreeMap<>();

        for (IGroundUnit targetUnit : targetUnitsForSide)
        {
            double distanceToObjective = MathUtils.calcDist(targetUnit.getPosition(), initialTargetPositions.get(0));
            if (distanceToObjective > 20.0)
            {
                sortedTargetUnitsForSide.put(distanceToObjective, targetUnit);
            }
        }

        return new ArrayList<IGroundUnit>(sortedTargetUnitsForSide.values());
    }
}
