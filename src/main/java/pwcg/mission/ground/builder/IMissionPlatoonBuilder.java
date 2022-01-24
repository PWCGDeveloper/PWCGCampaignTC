package pwcg.mission.ground.builder;

import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.MissionPlatoons;

public interface IMissionPlatoonBuilder
{
    MissionPlatoons createPlatoons() throws PWCGException;
}