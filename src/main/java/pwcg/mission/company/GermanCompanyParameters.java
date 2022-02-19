package pwcg.mission.company;

import java.util.Arrays;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.product.bos.country.TCServiceManager;

public class GermanCompanyParameters implements IAiCompanyParameters
{
    private List<String> infantryDivisionNames = Arrays.asList("19th Grenadier", "35th Infantry", "83rd Infantry", "122nd Infantry");
    private List<String> armoredDivisionNames = Arrays.asList("5th Panzer", "10th Panzer", "20th Panzer");
    private List<String> tankDestroyerDivisionNames = Arrays.asList("654th Panzerjager", "171st Panzerjager", "3rd Panzerjager");
    private List<String> companyNames = Arrays.asList("3rd", "4th", "5th", "6th");

    private int startingCompanyId = TCServiceManager.WEHRMACHT;
    private Coordinate companyPosition;

    private int numberOfTank = 4;
    private int numberOfInfantry = 5;
    private int numberOfTankDestroyer = 1;

    public GermanCompanyParameters(Campaign campaign)
    {
        try
        {
            companyPosition = PWCGContext.getInstance().getCurrentMap().getGroupManager().getTownLocations()
                    .getLocationsWithinRadiusBySide(Side.AXIS, campaign.getDate(), new Coordinate(75000, 0, 75000), 1000000).get(0).getPosition();
        }
        catch (PWCGException e)
        {
            companyPosition = new Coordinate(75000, 0, 75000);
        }
    }

    @Override
    public List<String> getInfantryDivisionNames()
    {
        return infantryDivisionNames;
    }

    @Override
    public List<String> getArmoredDivisionNames()
    {
        return armoredDivisionNames;
    }

    @Override
    public List<String> getTankDestroyerDivisionNames()
    {
        return tankDestroyerDivisionNames;
    }

    @Override
    public List<String> getCompanyNames()
    {
        return companyNames;
    }

    @Override
    public int getStartingCompanyId()
    {
        return startingCompanyId;
    }

    @Override
    public Coordinate getCompanyPosition()
    {
        return companyPosition;
    }

    @Override
    public int getNumberOfTank()
    {
        return numberOfTank;
    }

    @Override
    public int getNumberOfInfantry()
    {
        return numberOfInfantry;
    }

    @Override
    public int getNumberOfTankDestroyer()
    {
        return numberOfTankDestroyer;
    }
}
