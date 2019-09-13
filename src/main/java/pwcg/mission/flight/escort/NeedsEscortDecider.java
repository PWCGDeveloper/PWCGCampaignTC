package pwcg.mission.flight.escort;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;

public class NeedsEscortDecider
{
    public boolean needsEscort(Mission mission, Flight escortedFlight) throws PWCGException
    {
        Campaign campaign = mission.getCampaign();
        if (campaign.getCampaignData().getCampaignMode() == CampaignMode.CAMPAIGN_MODE_SINGLE ||
            campaign.getCampaignData().getCampaignMode() == CampaignMode.CAMPAIGN_MODE_COOP)
        {
            if (escortedFlight.isPlayerFlight())
            {
                return playerNeedsEscort(mission, escortedFlight);
            }
            else
            {
                return aiNeedsEscort(mission, escortedFlight);
            }
        }
        
        return false;
    }
    
    private boolean playerNeedsEscort(Mission mission, Flight escortedFlight) throws PWCGException, PWCGException
    {
        if (!escortedFlight.isFighterMission())
        {
            return true;
        }

        return false;
    }
    
    private boolean aiNeedsEscort(Mission mission, Flight escortedFlight) throws PWCGException, PWCGException
    {
        Campaign campaign = mission.getCampaign();
        if (campaign.getCampaignData().getCampaignMode() == CampaignMode.CAMPAIGN_MODE_SINGLE ||
            campaign.getCampaignData().getCampaignMode() == CampaignMode.CAMPAIGN_MODE_COOP)
        {
            if (mission.getMissionFlightBuilder().hasPlayerFighterFlightType())
            {
                if (campaign.isFighterCampaign())
                {
                    int escortedOdds = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.IsEscortedOddsKey);
                    int escortedDiceRoll = RandomNumberGenerator.getRandom(100);        
                    if (escortedDiceRoll < escortedOdds)
                    {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }

}