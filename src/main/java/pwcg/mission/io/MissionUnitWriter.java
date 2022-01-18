package pwcg.mission.io;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.unit.ITankPlatoon;

public class MissionUnitWriter 
{
    private Mission mission = null;

	public MissionUnitWriter (Mission mission)
	{
		this.mission = mission;
	}
	
	public void writeUnits(BufferedWriter writer) throws PWCGException
	{
        writePlayerUnits(mission.getPlatoons().getPlayerUnits(), writer);
	}

    private void writePlayerUnits(List<ITankPlatoon> units, BufferedWriter writer) throws PWCGException
    {
        for (ITankPlatoon unit : units)
        {
            unit.write(writer);
        }
    }
}
