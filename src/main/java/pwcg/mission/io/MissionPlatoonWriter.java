package pwcg.mission.io;

import java.io.BufferedWriter;

import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.platoon.ITankPlatoon;

public class MissionPlatoonWriter 
{
    private Mission mission = null;

	public MissionPlatoonWriter (Mission mission)
	{
		this.mission = mission;
	}
	
	public void writePlatoons(BufferedWriter writer) throws PWCGException
	{
        for (ITankPlatoon platoon : mission.getPlatoons().getPlayerPlatoons())
        {
            platoon.write(writer);
        }
        
        for (ITankPlatoon platoon : mission.getPlatoons().getAiPlatoonsForSide(Side.ALLIED))
        {
            platoon.write(writer);
        }
        
        for (ITankPlatoon platoon : mission.getPlatoons().getAiPlatoonsForSide(Side.AXIS))
        {
            platoon.write(writer);
        }
	}
}
