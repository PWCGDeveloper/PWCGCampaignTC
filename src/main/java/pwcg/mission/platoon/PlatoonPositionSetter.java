package pwcg.mission.platoon;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.platoon.tank.TankMcu;

public class PlatoonPositionSetter
{
    private static int HORIZONTAL_SPACING = 20;
    private static int VERTICAL_SPACING = 10;
    
    public static void setPlatoonPositions(ITankPlatoon tankPlatoon,Coordinate startPosition, Coordinate towardsPosition) throws PWCGException
    {
        double angleToObjective = MathUtils.calcAngle(startPosition, towardsPosition);
        resetStartFormation(tankPlatoon, startPosition, angleToObjective);
    }

    private static void resetStartFormation(ITankPlatoon unit, Coordinate startCoordinate, double angleToObjective) throws PWCGException
    {
        setUnittOrientation(unit.getTanks(), angleToObjective);
        setTankPositions(unit.getTanks(), startCoordinate, angleToObjective);
    }

    private static void setUnittOrientation(List<TankMcu> tanks, double orientationAngle)
    {
        for (TankMcu tank : tanks)
        {
            tank.setOrientation(new Orientation(orientationAngle));
        }
    }

    private static void setTankPositions(List<TankMcu> tanks, Coordinate startCoordinate, double angleToObjective) throws PWCGException
    {
        int numberInFormation = 0;
        for (TankMcu tank : tanks)
        {
            tank.setNumberInFormation(numberInFormation);
            Coordinate tankCoordinate = startCoordinate.copy();
            
            double horizontalSpacingAngle = MathUtils.adjustAngle(angleToObjective, 90);
            int horizontalSpacing = HORIZONTAL_SPACING * (numberInFormation);
            tankCoordinate = MathUtils.calcNextCoord(tankCoordinate, horizontalSpacingAngle, horizontalSpacing);
            
            double verticalSpacingAngle = MathUtils.adjustAngle(angleToObjective, 90);
            int verticalSpacing = VERTICAL_SPACING * (numberInFormation);
            tankCoordinate = MathUtils.calcNextCoord(tankCoordinate, verticalSpacingAngle, verticalSpacing);
            
            tankCoordinate.setYPos(0.0);
            
            tank.setPosition(tankCoordinate);
            
            tank.getEntity().enableEntity();
            
            ++numberInFormation;
        }
    }
}
