package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.target.FrontSegmentDefinition;

public class MissionBattleManager
{
    private List<FrontSegmentDefinition> missionAssaultDefinitions = new ArrayList<>();

    public void addMissionBattle(FrontSegmentDefinition missionBattle)
    {
        missionAssaultDefinitions.add(missionBattle);
    }
    
    public boolean isNearAnyBattle(Coordinate coordinate) throws PWCGException
    {
        for (FrontSegmentDefinition missionBattle : missionAssaultDefinitions)
        {
            if (missionBattle.isNearBattle(coordinate))
            {
                return true;
            }
        }
        
        return false;
    }

    public List<FrontSegmentDefinition> getMissionAssaultDefinitions()
    {
        return missionAssaultDefinitions;
    }
}
