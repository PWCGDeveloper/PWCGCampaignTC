package pwcg.mission;

import pwcg.campaign.api.Side;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class MissionPlatoonSize
{

    public static int getNumAiPlatoonsForSide(Mission mission, Side side, int size) throws PWCGException
    {
        if (side == Side.ALLIED)
        {
            return getNumAlliedAiPlatoons(mission, size);
        }
        else
        {
            return getNumAxisAiPlatoons(mission, size);
        }
    }

    private static int getNumAlliedAiPlatoons(Mission mission, int alliedPlayerCompanies) throws PWCGException
    {
        int maxAlliedPlatoons = mission.getCampaign().getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AlliedPlatoonsInMissionKey);
        int minAlliedPlatoons = maxAlliedPlatoons / 2;
        if(minAlliedPlatoons < 3)
        {
            minAlliedPlatoons = 3;
        }
        
        if (alliedPlayerCompanies > minAlliedPlatoons)
        {
            minAlliedPlatoons = alliedPlayerCompanies;
        }
        
        int numPlatoons = minAlliedPlatoons;
        if (minAlliedPlatoons < maxAlliedPlatoons)
        {
            numPlatoons = minAlliedPlatoons + RandomNumberGenerator.getRandom(maxAlliedPlatoons - minAlliedPlatoons + 1);
        }        
        
        return numPlatoons - alliedPlayerCompanies;
    }
    
    private static int getNumAxisAiPlatoons(Mission mission, int axisPlayerCompanies) throws PWCGException
    {
        int maxAxisPlatoons = mission.getCampaign().getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.AxisPlatoonsInMissionKey);
        int minAxisPlatoons = maxAxisPlatoons / 2;
        if(minAxisPlatoons < 2)
        {
            minAxisPlatoons = 2;
        }
        
        if (axisPlayerCompanies > minAxisPlatoons)
        {
            minAxisPlatoons = axisPlayerCompanies;
        }
        
        int numPlatoons = minAxisPlatoons;
        if (minAxisPlatoons < maxAxisPlatoons)
        {
            numPlatoons = minAxisPlatoons + RandomNumberGenerator.getRandom(maxAxisPlatoons - minAxisPlatoons + 1);
        }        
        
        return numPlatoons - axisPlayerCompanies;
    }
}
