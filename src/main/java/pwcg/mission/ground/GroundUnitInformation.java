package pwcg.mission.ground;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.target.TargetType;

public class GroundUnitInformation
{
    private ICountry country = CountryFactory.makeCountryByCountry(Country.NEUTRAL);
    private String name = "";
    private Date date;
    private Coordinate position = new Coordinate();
    private List<Coordinate> destinations = new ArrayList<>();
	private Orientation orientation = new Orientation();
    private TargetType targetType = TargetType.TARGET_NONE;
    private String requestedUnitType = "";

    public ICountry getCountry()
    {
        return country;
    }

    public void setCountry(ICountry country)
    {
        this.country = country;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Coordinate getPosition()
    {
        return position;
    }

    public void setPosition(Coordinate position)
    {
        this.position = position;
    }

    public Coordinate getDestination()
    {
        if (!destinations.isEmpty())
        {
            return destinations.get(destinations.size()-1);
        }
        return null;
    }

    public void addDestination(Coordinate destination)
    {
        this.destinations.add(destination);
    }

    public Orientation getOrientation()
    {
        return orientation;
    }

    public void setOrientation(Orientation orientation)
    {
        this.orientation = orientation;
    }

    public TargetType getTargetType()
    {
        return targetType;
    }

    public void setTargetType(TargetType targetType)
    {
        this.targetType = targetType;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public String getRequestedUnitType()
    {
        return requestedUnitType;
    }

    public void setRequestedUnitType(String requestedUnitType)
    {
        this.requestedUnitType = requestedUnitType;
    }
}
