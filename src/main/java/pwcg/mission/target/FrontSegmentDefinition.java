package pwcg.mission.target;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;

public class FrontSegmentDefinition
{
    private static final Integer CLOSE_TO_BATTLE = 10000;

    private Coordinate assaultPosition = null;
    private Coordinate defensePosition = null;
    private ICountry assaultingCountry = null;
    private ICountry defendingCountry = null;

    public Orientation getTowardsDefenderOrientation() throws PWCGException
    {
        double angleToDefensePosition = MathUtils.calcAngle(assaultPosition, defensePosition);
        return new Orientation(angleToDefensePosition);
    }
    
    public Orientation getTowardsAttackerOrientation() throws PWCGException
    {
        double angleToAssaultPosition = MathUtils.calcAngle(defensePosition, assaultPosition);
        return new Orientation(angleToAssaultPosition);
    }

    public boolean isNearBattle(Coordinate coordinate)
    {
        double distanceFromAssault = MathUtils.calcDist(coordinate, assaultPosition);
        double distanceFromDefense = MathUtils.calcDist(coordinate, defensePosition);
        
        if (distanceFromAssault < CLOSE_TO_BATTLE || distanceFromDefense < CLOSE_TO_BATTLE)
        {
            return true;
        }

        return false;
    }

    public Coordinate getPositionForSide(Side side)
    {
        if(assaultingCountry.getSide() == side)
        {
            return assaultPosition;
        }
        else
        {
            return defensePosition;
        }
    }

    public double getOrientationTowardsEnemy(Side side) throws PWCGException
    {
        double angleToEnemy = MathUtils.calcAngle(defensePosition, assaultPosition);
        if(assaultingCountry.getSide() == side)
        {
            angleToEnemy = MathUtils.calcAngle(assaultPosition, defensePosition);
        }
        return angleToEnemy;
     }

    public double getOrientationFromEnemy(Side side) throws PWCGException
    {
        double angleToEnemy = getOrientationTowardsEnemy(side);
        double angleFromEnemy = MathUtils.adjustAngle(angleToEnemy, 180);
        return angleFromEnemy;
     }

    public Coordinate getAssaultPosition()
    {
        return assaultPosition;
    }

    public void setAssaultPosition(Coordinate assaultPosition)
    {
        this.assaultPosition = assaultPosition;
    }

    public Coordinate getDefensePosition()
    {
        return defensePosition;
    }

    public void setDefensePosition(Coordinate defensePosition)
    {
        this.defensePosition = defensePosition;
    }    

    public ICountry getAssaultingCountry()
    {
        return assaultingCountry;
    }

    public void setAssaultingCountry(ICountry assaultingCountry)
    {
        this.assaultingCountry = assaultingCountry;
    }

    public ICountry getDefendingCountry()
    {
        return defendingCountry;
    }

    public void setDefendingCountry(ICountry defendingCountry)
    {
        this.defendingCountry = defendingCountry;
    }
}
