package pwcg.mission.ground.builder;

import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.MissionPlatoons;

public interface IPlatoonBuilder
{
    MissionPlatoons createPlatoons() throws PWCGException;
}