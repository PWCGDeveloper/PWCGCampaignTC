package pwcg.testutils;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.logfiles.LogParser;
import pwcg.core.logfiles.event.AType12;
import pwcg.core.logfiles.event.IAType12;
import pwcg.product.bos.country.BoSCountry;

public class TestATypeFactory
{
    static public int id = 100;
    
    static public AType12 makeRussianTank(String russianCrewMemberName, String russianCrewMemberBotId) throws PWCGException
    {
        ++id;
        AType12 aType12Parsed = new AType12("AType:12 ID:1488895 TYPE:t34-76stz COUNTRY:101 NAME:Lt Pierre Trudeau PID:-1 POS(119648.406,152.195,44274.488)", Integer.valueOf(id).toString());
        AType12 aType12 = new AType12(russianCrewMemberBotId, aType12Parsed.getType(), russianCrewMemberName, aType12Parsed.getCountry(), aType12Parsed.getPid(), new Coordinate(500000, 0, 50000));
        return aType12;
    }
    
    static public AType12 makeGermanTank(String germanCrewMemberName, String germanCrewMemberBotId) throws PWCGException
    {
        ++id;
        AType12 aType12Parsed = new AType12("AType:12 ID:1488895 TYPE:pziv-g COUNTRY:201 NAME:Lt Hans Schmidt PID:-1 POS(119648.406,152.195,44274.488)", Integer.valueOf(id).toString());
        AType12 aType12 = new AType12(germanCrewMemberBotId, aType12Parsed.getType(), germanCrewMemberName, aType12Parsed.getCountry(), aType12Parsed.getPid(), new Coordinate(500000, 0, 50000));
        return aType12;
    }
    
    static public AType12 makeBalloon(ICountry country) throws PWCGException
    {
        ++id;
        AType12 aType12 = new AType12(Integer.valueOf (id).toString(), "Balloon", "Balloon", country, LogParser.UNKNOWN_MISSION_LOG_ENTITY, new Coordinate(500000, 0, 50000));
        return aType12;
    }
    
    static public AType12 makeTruck(Country countryCode) throws PWCGException
    {
        ++id;        
        ICountry country;
        if (countryCode == Country.RUSSIA)
        {
            country = new BoSCountry(Country.RUSSIA);            
        }
        else
        {
            country = new BoSCountry(Country.GERMANY);            
        }
        AType12 aType12 = new AType12(Integer.valueOf (id).toString(), "Truck", "Truck", country, LogParser.UNKNOWN_MISSION_LOG_ENTITY, new Coordinate(500000, 0, 50000));
        return aType12;
    }
    
    static public AType12 makeCrewMemberBot(IAType12 plane) throws PWCGException
    {
        ++id;
        AType12 aType12 = new AType12(Integer.valueOf (id).toString(), "BotCrewMember", "Bot", plane.getCountry(), plane.getId(), new Coordinate(500000, 0, 50000));
        return aType12;
    }
}
