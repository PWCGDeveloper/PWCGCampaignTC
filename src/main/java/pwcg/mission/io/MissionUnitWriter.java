package pwcg.mission.io;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.unit.ITankUnit;

public class MissionUnitWriter 
{
    private Mission mission = null;

	public MissionUnitWriter (Mission mission)
	{
		this.mission = mission;
	}
	
	public void writeUnits(BufferedWriter writer) throws PWCGException
	{
        writePlayerUnits(mission.getUnits().getPlayerUnits(), writer);
	}

    private void writePlayerUnits(List<ITankUnit> units, BufferedWriter writer) throws PWCGException
    {
        for (ITankUnit unit : units)
        {
            unit.write(writer);
        }
    }
}
