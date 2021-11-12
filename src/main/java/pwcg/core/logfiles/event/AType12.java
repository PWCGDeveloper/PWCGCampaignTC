package pwcg.core.logfiles.event;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;

// T:15 AType:12 ID:302079 TYPE:He 111 H-6 COUNTRY:201 NAME:Obltn Heinkel Mann PID:-1 POS(64429.594,174.325,41093.000)
public class AType12 extends ATypeBase implements IAType12
{
    private String id = "";
    private String type = "";
    private String name;
    private ICountry country = CountryFactory.makeNeutralCountry();
    private String pid = "";
    private Coordinate position;

    public AType12(String line) throws PWCGException
    {    
        super(AType.ATYPE12);
        parse(line);
    }

    public AType12(String line, String id) throws PWCGException
    {    
        super(AType.ATYPE12);
        parse(line);
        this.id = id;
    }

    public AType12(
            String id, 
            String type, 
            String name, 
            ICountry country,
            String pid) throws PWCGException
    {    
        super(AType.ATYPE12);
        this.id = id;
        this.type = type;
        this.name = name;
        this.country = country;
        this.pid = pid;
    }

    private void parse (String line) throws PWCGException 
    {
        id = getId(line, "ATYPE:12 ID:", " TYPE:");
        type = getString(line, " TYPE:", " COUNTRY:");

        int countryCode = getInteger(line, "COUNTRY:", " NAME:");
        country = CountryFactory.makeCountryByCode(countryCode);

        name = getString(line, "NAME:", " PID:");
        if (name.startsWith("\u0001"))
        {
            name = name.substring(1);
        }
        
        if (line.contains(" POS("))
        {
            pid = getId(line, "PID:", " POS(");
        }
        else
        {
            pid = getId(line, "PID:", null);
        }
        
        position = findCoordinate(line, "POS(");
    }

    @Override
    public String getId()
    {
        return id;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getType()
    {
        return type;
    }

    @Override
    public ICountry getCountry()
    {
        return country;
    }

    @Override
    public String getPid()
    {
        return pid;
    }

    @Override
    public Coordinate getPosition()
    {
        return position;
    }
}
