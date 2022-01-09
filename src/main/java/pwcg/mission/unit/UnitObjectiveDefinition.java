package pwcg.mission.unit;

import pwcg.core.location.Coordinate;

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
        return startPosition;
    }

    public Coordinate getEndPosition()
    {
        return endPosition;
    }

    public UnitMissionType getMissionType()
    {
        return missionType;
    }

    public boolean isDefending()
    {
        return isDefending;
    }
}
