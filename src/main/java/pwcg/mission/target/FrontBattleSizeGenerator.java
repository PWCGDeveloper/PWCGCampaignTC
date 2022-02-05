package pwcg.mission.target;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.builder.BattleSize;

public class FrontBattleSizeGenerator
{
    public static BattleSize createAssaultBattleSize(Campaign campaign) throws PWCGException 
    {
        BattleSize battleSize = BattleSize.BATTLE_SIZE_TINY;      
        ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();        
        String currentGroundSetting = configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigGroundKey);
        
        boolean isHistoricalBattle = 
                PWCGContext.getInstance().getCurrentMap().getBattleManager().isHistoricalBattleActive(PWCGContext.getInstance().getCurrentMap().getMapIdentifier(), campaign.getDate());
        
        if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_LOW))
        {
            battleSize = BattleSize.BATTLE_SIZE_TINY;
            if(isHistoricalBattle)
            {
                battleSize = BattleSize.BATTLE_SIZE_SKIRMISH;
            }
        }
        else if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_MED))
        {
            battleSize = BattleSize.BATTLE_SIZE_SKIRMISH;
            if(isHistoricalBattle)
            {
                battleSize = BattleSize.BATTLE_SIZE_ASSAULT;
            }
        }
        else
        {
            battleSize = BattleSize.BATTLE_SIZE_ASSAULT;
            if(isHistoricalBattle)
            {
                battleSize = BattleSize.BATTLE_SIZE_OFFENSIVE;
            }
        }

        return battleSize;
    }
}
