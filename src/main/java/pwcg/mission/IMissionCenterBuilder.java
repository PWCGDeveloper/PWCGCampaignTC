package pwcg.mission;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;

public interface IMissionCenterBuilder
{
    Coordinate findMissionCenter(int missionBoxRadius) throws PWCGException;
}
