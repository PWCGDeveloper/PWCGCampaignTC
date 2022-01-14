package pwcg.mission.ground;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.ground.builder.AssaultBuilder;
import pwcg.mission.ground.builder.IBattleBuilder;
import pwcg.mission.ground.org.GroundUnitCollection;

public class MissionBattleBuilder implements IBattleBuilder
{
    private Mission mission = null;

    private List<GroundUnitCollection> battles = new ArrayList<>();

    public MissionBattleBuilder (Mission mission)
    {
        this.mission = mission;
    }

    @Override
    public List<GroundUnitCollection> generateBattle() throws PWCGException
    {
        GroundUnitCollection assaultUnitCollection = AssaultBuilder.generateAssault(mission, mission.getObjective().getPosition());
        battles.add(assaultUnitCollection);
        return battles;
    }
}
