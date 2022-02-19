package pwcg.mission.company;

import java.util.Arrays;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.product.bos.country.TCServiceManager;

public class BritishCompanyParameters implements IAiCompanyParameters
{
    private List<String> armoredDivisionNames = Arrays.asList("2nd Armoured", "6th Armoured", "42nd Armoured");
    private List<String> infantryDivisionNames = Arrays.asList("8th Infantry", "11th Infantry", "14th Infantry", "17th Infantry");
    private List<String> tankDestroyerDivisionNames = Arrays.asList();
    private List<String> companyNames = Arrays.asList("3rd", "4th", "5th", "6th");

    private int startingCompanyId = TCServiceManager.BRITISH_ARMY;
    private Coordinate companyPosition;

    private int numberOfTank = 4;
    private int numberOfInfantry = 6;
    private int numberOfTankDestroyer = 0;

    public BritishCompanyParameters(Campaign campaign)
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
