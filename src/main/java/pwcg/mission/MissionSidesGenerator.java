package pwcg.mission;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.battle.Battle;
import pwcg.campaign.battle.BattleManager;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.skirmish.Skirmish;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class MissionSidesGenerator
{
    private Campaign campaign;
    private Skirmish skirmish;


    public MissionSidesGenerator(Campaign campaign, Skirmish skirmish)
    {
        this.campaign = campaign;
        this.skirmish = skirmish;
    }
    
    public ICountry getDefendingCountry() throws PWCGException
    {
        ICountry defendingCountry = getDefendingCountryFromSkirmish();
        if (defendingCountry.getCountry() == Country.NEUTRAL)
        {
            defendingCountry = getDefendingCountryByMapCircumstances();
        }
        return defendingCountry;
    }

    public ICountry getAssaultingCountry(ICountry defendingCountry) throws PWCGException
    {
        if (defendingCountry.getSide() == Side.ALLIED)
        {
            return PWCGContext.getInstance().getCurrentMap().getGroundCountryForMapBySide(Side.AXIS);
        }
        else
        {
            return PWCGContext.getInstance().getCurrentMap().getGroundCountryForMapBySide(Side.ALLIED);
        }
    }

    private ICountry getDefendingCountryFromSkirmish()
    {
        if (skirmish != null)
        {
            Side defendingSide =  skirmish.getAttackerGround().getOppositeSide();
            ICountry defendingCountry = CountryFactory.makeMapReferenceCountry(defendingSide);
            return defendingCountry;
        }
        
        return CountryFactory.makeCountryByCountry(Country.NEUTRAL);
    }

    private ICountry getDefendingCountryByMapCircumstances() throws PWCGException
    {
        BattleManager battleManager = PWCGContext.getInstance().getCurrentMap().getBattleManager();
        Battle battle = battleManager.getBattleForCampaign(PWCGContext.getInstance().getCurrentMap().getMapIdentifier(), campaign.getDate());
        if (battle != null)
        {
            int roll = RandomNumberGenerator.getRandom(100);
            if (roll < 80)
            {
                return CountryFactory.makeCountryByCountry(battle.getDefendercountry());
            }
            else
            {
                return CountryFactory.makeCountryByCountry(battle.getAggressorcountry());
            }
        }
        
        return chooseSidesRandom();
    }

    private ICountry chooseSidesRandom() throws PWCGException
    {
        int roll = RandomNumberGenerator.getRandom(100);
        if (roll < 50)
        {
            return PWCGContext.getInstance().getCurrentMap().getGroundCountryForMapBySide(Side.ALLIED);
        }
        else
        {
            return PWCGContext.getInstance().getCurrentMap().getGroundCountryForMapBySide(Side.AXIS);
        }
    }

}
