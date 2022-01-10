package pwcg.mission.unit;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;

public class UnitObjectiveDefinition
{
    private UnitMissionType missionType;
    private Coordinate startPosition;
    private Coordinate endPosition;
    private boolean isDefending;
    
    public UnitObjectiveDefinition(UnitMissionType missionType, Coordinate startPosition, Coordinate endPosition, boolean isDefending)
    {
        this.missionType = missionType;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.isDefending = isDefending;
    }

    public Coordinate getStartPosition()
    {
        return startPosition.copy();
    }

    public Coordinate getEndPosition()
    {
        return endPosition.copy();
    }

    public UnitMissionType getMissionType()
    {
        return missionType;
    }

    public boolean isDefending()
    {
        return isDefending;
    }

    public double calcAngleToObjective() throws PWCGException
    {
        return MathUtils.calcAngle(startPosition, endPosition);
    }
}
