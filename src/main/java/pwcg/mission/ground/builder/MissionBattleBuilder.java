package pwcg.mission.ground.builder;

import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.ground.org.GroundUnitCollection;

public class MissionBattleBuilder implements IBattleBuilder
{
    private Mission mission = null;

    public MissionBattleBuilder (Mission mission)
    {
        this.mission = mission;
    }

    @Override
    public GroundUnitCollection generateBattle() throws PWCGException
    {
        GroundUnitCollection assaultFixedUnitCollection = AssaultFixedUnitSegmentBuilder.generateAssault(mission);
        return assaultFixedUnitCollection;
    }
}
