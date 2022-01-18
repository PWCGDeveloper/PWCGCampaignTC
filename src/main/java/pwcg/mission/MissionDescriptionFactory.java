package pwcg.mission;

import pwcg.campaign.Campaign;
import pwcg.mission.unit.ITankPlatoon;

public class MissionDescriptionFactory 
{
	public static IMissionDescription buildMissionDescription(Campaign campaign, Mission mission)
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
