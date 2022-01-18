package pwcg.mission.playerunit.objective;

import java.util.Date;

import pwcg.core.exception.PWCGException;
import pwcg.mission.unit.ITankPlatoon;

public class MissionObjectiveFactory
{
    public static String formMissionObjective(ITankPlatoon unit, Date date) throws PWCGException
    {
        return "Do something useful for God and Country!";
    }
}
