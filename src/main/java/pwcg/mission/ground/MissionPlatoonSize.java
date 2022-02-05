package pwcg.mission.ground;

import pwcg.campaign.api.Side;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.ground.builder.BattleSize;

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
        
        int minAlliedPlatoons = getMinPlatoons(mission, 2);
        
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
        
        int minAxisPlatoons = getMinPlatoons(mission, 0);

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

    private static int getMinPlatoons(Mission mission, int minPlatoonsTiny)
    {
        int minPlatoons = minPlatoonsTiny;
        if (mission.getObjective().getBattleSize() == BattleSize.BATTLE_SIZE_SKIRMISH)
        {
            minPlatoons += 1;
        }
        else if (mission.getObjective().getBattleSize() == BattleSize.BATTLE_SIZE_SKIRMISH)
        {
            minPlatoons += 2;
        }
        else if (mission.getObjective().getBattleSize() == BattleSize.BATTLE_SIZE_ASSAULT)
        {
            minPlatoons += 3;
        }
        else if (mission.getObjective().getBattleSize() == BattleSize.BATTLE_SIZE_OFFENSIVE)
        {
            minPlatoons += 4;
        }
        return minPlatoons;
    }
}
