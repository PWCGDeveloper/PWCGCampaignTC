package pwcg.mission.company;

import java.util.Arrays;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.product.bos.country.TCServiceManager;

public class RussianCompanyParameters implements IAiCompanyParameters
{
    private List<String> armoredDivisionNames = Arrays.asList("143rd Tank", "167th Tank", "322nd Tank", "46th Tank");
    private List<String> infantryDivisionNames = Arrays.asList("102nd Rifle", "41st Rifle", "260th Rifle", "193rd Rifle");
    private List<String> tankDestroyerDivisionNames = Arrays.asList();
    private List<String> companyNames = Arrays.asList("3rd", "4th", "5th", "6th");

    private int startingCompanyId = TCServiceManager.SSV;
    private Coordinate companyPosition;

    private int numberOfTank = 5;
    private int numberOfInfantry = 5;
    private int numberOfTankDestroyer = 0;

    public RussianCompanyParameters(Campaign campaign)
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
