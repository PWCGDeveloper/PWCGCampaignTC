package pwcg.mission;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.mission.platoon.ITankPlatoon;

public class MissionDescriptionFactory 
{
	public static IMissionDescription buildMissionDescription(Campaign campaign, Mission mission) throws PWCGException
	{
	    if (mission.isAAATruckMission())
	    {
            return new MissionDescriptionAAATruck(campaign, mission);
	    }
	    else
	    {
            return new MissionDescriptionSinglePlayer(campaign, mission, mission.getPlatoons().getReferencePlayerUnit());
	    }
	}

    public static IMissionDescription buildMissionDescription(Campaign campaign, Mission mission, ITankPlatoon  playerPlatoon)
    {
        return new MissionDescriptionSinglePlayer(campaign, mission, playerPlatoon);
    }

}
