package pwcg.dev.utils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.company.Company;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.io.json.CompanyIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.PWCGLocation;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class CompanyAirfieldToBaseConverter 
{
	static public void main (String[] args)
	{
        try
		{
			CompanyAirfieldToBaseConverter finder = new CompanyAirfieldToBaseConverter();
			finder.convertAirfieldsToTowns();
		}
		catch (Exception e)
		{
			 PWCGLogger.logException(e);
		}
	}

    
    private void convertAirfieldsToTowns() throws PWCGException  
    {        
        TreeMap<Integer, String> airfieldsOnMapSorted = new TreeMap<>();
        
        List<Company> allCOmpanies =  PWCGContext.getInstance().getCompanyManager().getAllCompanies();
        for (Company company : allCOmpanies)
        {
            determineCurrentAirfieldCurrentMap(company);
        }

        for (int companyId : airfieldsOnMapSorted.keySet())
        {
            PWCGLogger.log(LogLevel.DEBUG, "" + companyId);
        }
    }

    public void determineCurrentAirfieldCurrentMap(Company company) throws PWCGException
    {
        Map<Date, String> baseMap = new TreeMap<>();
        for (Date baseDate : company.getBases().keySet())
        {
            String airfieldName = determineCurrentAirfieldName(company, baseDate);
            Airfield field =  PWCGContext.getInstance().getAirfieldAllMaps(airfieldName);
            if (field == null)
            {
                throw new PWCGException(company.determineBaseName(baseDate) + "field not found" + airfieldName);
            }
            else
            {
                String closestTown = findClosestTown(company, field.getPosition(), baseDate);
                baseMap.put(baseDate, closestTown);
                System.out.println("best town is " + closestTown);
                company.setBases(baseMap);
                CompanyIOJson.writeJson(company);
            }
        }         
    }

    private String findClosestTown(Company company, Coordinate position, Date baseDate) throws PWCGException
    {
        FrontMapIdentifier mapId = PWCGContext.getInstance().getMapForDate(baseDate);
        PWCGContext.getInstance().changeContext(mapId);
        PWCGLocation town = PWCGContext.getInstance().getCurrentMap().getGroupManager().getTownFinder().findClosestTownForSide(company.determineSide(), baseDate, position);
        return town.getName();
    }


    public String determineCurrentAirfieldName(Company company, Date campaignDate)
    {
        String currentAirFieldName = null;
        
        for (Date baseStartDate : company.getBases().keySet())
        {
            if (!baseStartDate.after(campaignDate))
            {
                currentAirFieldName = company.getBases().get(baseStartDate);
            }
            else
            {
                break;
            }
        }
        
        return currentAirFieldName;
    }
}
