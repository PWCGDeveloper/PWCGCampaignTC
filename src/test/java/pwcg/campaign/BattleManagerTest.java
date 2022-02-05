package pwcg.campaign;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.battle.HistoricalBattle;
import pwcg.campaign.battle.HistoricalBattleManager;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
public class BattleManagerTest
{
    public BattleManagerTest() throws PWCGException
    {
        
    }
    
    @Test
    public void getBattleTest () throws PWCGException
    {   
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.STALINGRAD_MAP);

    	HistoricalBattleManager battleManager = PWCGContext.getInstance().getCurrentMap().getBattleManager();
    	
    	HistoricalBattle battle = battleManager.getBattleForCampaign(FrontMapIdentifier.STALINGRAD_MAP, DateUtils.getDateYYYYMMDD("19421120"));
        Assertions.assertTrue (battle.getName().equals("Stalingrad Operation Uranus"));
        Assertions.assertTrue (battle.getAggressorcountry() == Country.RUSSIA);
        Assertions.assertTrue (battle.getDefendercountry() == Country.GERMANY);
    }

    @Test
    public void getBattleTestDateWrong () throws PWCGException
    {        
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.STALINGRAD_MAP);
        HistoricalBattleManager battleManager = PWCGContext.getInstance().getCurrentMap().getBattleManager();
    	
    	HistoricalBattle battle = battleManager.getBattleForCampaign(FrontMapIdentifier.STALINGRAD_MAP, DateUtils.getDateYYYYMMDD("19420701"));
        Assertions.assertTrue (battle == null);
    }

    @Test
    public void getBattleTestMapWrong () throws PWCGException
    {        
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.BODENPLATTE_MAP);
        HistoricalBattleManager battleManager = PWCGContext.getInstance().getCurrentMap().getBattleManager();
    	
    	HistoricalBattle battle = battleManager.getBattleForCampaign(FrontMapIdentifier.STALINGRAD_MAP, DateUtils.getDateYYYYMMDD("19421120"));
        Assertions.assertTrue (battle == null);
    }    

}
