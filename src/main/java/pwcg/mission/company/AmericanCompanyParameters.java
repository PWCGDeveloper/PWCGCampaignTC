package pwcg.mission.company;

import java.util.Arrays;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.product.bos.country.TCServiceManager;

public class AmericanCompanyParameters implements IAiCompanyParameters
{
    private List<String> armoredDivisionNames = Arrays.asList("4th Armored", "2nd Armored", "3rd Amropred");
    private List<String> infantryDivisionNames = Arrays.asList("1st Infantry", "8th Infantry", "99th Infantry", "29th Infantry");
    private List<String> tankDestroyerDivisionNames = Arrays.asList("609th Anti Tank", "628th Anti Tank", "640th Anti Tank");
    private List<String> companyNames = Arrays.asList("Charlie", "Dog", "Easy", "Fox");

    private int startingCompanyId = TCServiceManager.US_ARMY;
    private Coordinate companyPosition;

    private int numberOfTank = 3;
    private int numberOfInfantry = 6;
    private int numberOfTankDestroyer = 1;

    public AmericanCompanyParameters(Campaign campaign)
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
