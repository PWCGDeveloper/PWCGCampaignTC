package pwcg.mission.ground.builder;

import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.org.GroundUnitCollection;

public interface IBattleBuilder
{

    GroundUnitCollection generateBattle() throws PWCGException;

}