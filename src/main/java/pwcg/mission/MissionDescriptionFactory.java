package pwcg.mission;

import pwcg.campaign.Campaign;
import pwcg.mission.unit.IPlayerUnit;

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
            return new MissionDescriptionSinglePlayer(campaign, mission, mission.getUnits().getReferencePlayerUnit());
	    }
	}

    public static IMissionDescription buildMissionDescription(Campaign campaign, Mission mission, IPlayerUnit  playerUnit)
    {
        return new MissionDescriptionSinglePlayer(campaign, mission, playerUnit);
    }

}
