package pwcg.mission.playerunit.objective;

import java.util.Date;

import pwcg.core.exception.PWCGException;
import pwcg.mission.unit.ITankUnit;

public class MissionObjectiveFactory
{
    public static String formMissionObjective(ITankUnit unit, Date date) throws PWCGException
    {
        return "Do something useful for God and Country!";
    }
}
