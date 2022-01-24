package pwcg.mission.mcu;

import java.util.List;

import org.junit.jupiter.api.Test;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;

public class CoalitionFactoryTest
{
    private static ICountry britain;
    private static ICountry germany;
    
    public CoalitionFactoryTest() throws PWCGException
    {
        britain = CountryFactory.makeCountryByCountry(Country.BRITAIN);
        germany = CountryFactory.makeCountryByCountry(Country.GERMANY);
    }
    
    @Test
    public void testCoalitionBySide() throws PWCGException
    {
        
        Coalition coalition = CoalitionFactory.getCoalitionBySide(Side.ALLIED);
        assert(coalition == Coalition.COALITION_ALLIED);

        coalition = CoalitionFactory.getCoalitionBySide(Side.AXIS);
        assert(coalition == Coalition.COALITION_AXIS);
    }
    
    @Test
    public void testFriendlyCoalition() throws PWCGException
    {
        
        Coalition coalition = CoalitionFactory.getFriendlyCoalition(britain);
        assert(coalition == Coalition.COALITION_ALLIED);

        coalition = CoalitionFactory.getFriendlyCoalition(germany);
        assert(coalition == Coalition.COALITION_AXIS);
    }
    
    @Test
    public void testEnemyCoalition() throws PWCGException
    {
        
        Coalition coalition = CoalitionFactory.getEnemyCoalition(britain);
        assert(coalition == Coalition.COALITION_AXIS);

        coalition = CoalitionFactory.getEnemyCoalition(germany);
        assert(coalition == Coalition.COALITION_ALLIED);
    }
    
    @Test
    public void testAllCoalitions() throws PWCGException
    {
        
        List<Coalition> wwiiCoalitions = CoalitionFactory.getAllCoalitions();
        assert(wwiiCoalitions.contains(Coalition.COALITION_AXIS));
        assert(wwiiCoalitions.contains(Coalition.COALITION_ALLIED));
    }
}
