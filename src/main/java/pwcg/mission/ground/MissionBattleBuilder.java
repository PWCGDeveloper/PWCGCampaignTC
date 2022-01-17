package pwcg.mission.ground;

import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.ground.builder.AssaultBuilder;
import pwcg.mission.ground.builder.IBattleBuilder;
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
        GroundUnitCollection assaultUnitCollection = AssaultBuilder.generateAssault(mission, mission.getObjective().getPosition());
        return assaultUnitCollection;
    }
}
