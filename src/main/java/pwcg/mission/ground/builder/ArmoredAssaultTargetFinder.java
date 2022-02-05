package pwcg.mission.ground.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.GroundUnitType;
import pwcg.mission.ground.org.IGroundUnit;

public class ArmoredAssaultTargetFinder
{
    private GroundUnitCollection assaultFixedUnitCollection;
    private GroundUnitType groundUnitType;

    public ArmoredAssaultTargetFinder(Mission mission, GroundUnitCollection assaultFixedUnitCollection, GroundUnitType groundUnitType)
    {
        this.groundUnitType = groundUnitType;
        this.assaultFixedUnitCollection = assaultFixedUnitCollection;
    }

    public Coordinate findTargetForTankPlatoon(Coordinate startPosition, Side side) throws PWCGException
    {
        List<IGroundUnit> targetUnitsForSide = assaultFixedUnitCollection.getGroundUnitsByTypeAndSide(groundUnitType, side);
        IGroundUnit targetUnit = findUnitCloseToPosition(targetUnitsForSide, startPosition);
        return targetUnit.getPosition().copy();
    }

    private IGroundUnit findUnitCloseToPosition(List<IGroundUnit> targetUnitsForSide, Coordinate objectivePosition) throws PWCGException
    {
        List<IGroundUnit> viableTargetUnits = new ArrayList<>();
        double closeUnitDistance = 4000.0;
        while (viableTargetUnits.isEmpty())
        {
            for (IGroundUnit targetUnit : targetUnitsForSide)
            {
                double distanceToPlatoon = MathUtils.calcDist(targetUnit.getPosition(), objectivePosition);
                if (distanceToPlatoon < closeUnitDistance)
                {
                    viableTargetUnits.add(targetUnit);
                }
            }
            
            closeUnitDistance += 1000;
        }
        
        Collections.shuffle(viableTargetUnits);
        return viableTargetUnits.get(0);
    }
}
