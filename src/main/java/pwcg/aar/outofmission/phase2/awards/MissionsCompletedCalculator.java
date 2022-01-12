package pwcg.aar.outofmission.phase2.awards;

import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;

public class MissionsCompletedCalculator
{
	public static int calculateMissionsCompleted(Campaign campaign, CrewMember crewMember) throws PWCGException 
    {        
        ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
        int missionCreditedPerMissionCompleted = configManager.getIntConfigParam(ConfigItemKeys.MissionsCreditedKey);        

        if (missionCreditedPerMissionCompleted < 1)
        {
            missionCreditedPerMissionCompleted = 1;
        }

        int updatedMissionsCompleted = crewMember.getBattlesFought() + missionCreditedPerMissionCompleted;
        return updatedMissionsCompleted;
    }
}
