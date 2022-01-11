package pwcg.campaign.group;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class AirfieldInEnemyTerritory
{
	
	private Map<String, String> badAirfields = new HashMap<>();
	private boolean acceptNeutralPlacement = false;
    
	AirfieldInEnemyTerritory(boolean acceptNeutralPlacement)
	{
		this.acceptNeutralPlacement = acceptNeutralPlacement;
	}
	
	protected void findEnemy(FrontMapIdentifier mapId, Date startDate, Date endDate)
    {
        try
        {
            while (startDate.before(endDate))
            {                
                determineProperPlacementForDate(mapId, startDate);
                startDate = DateUtils.advanceTimeDays(startDate, 1);
            }            
            printBadCompanys();
            
            assert(badAirfields.size() == 0);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

	private void determineProperPlacementForDate(FrontMapIdentifier mapId, Date startDate) throws PWCGException
	{
		CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
		for (Company company : companyManager.getActiveCompanies(startDate))
		{
		    determineCompanyIsOnCorrectSide(mapId, startDate, company);
		}
	}

	private void determineCompanyIsOnCorrectSide(FrontMapIdentifier mapId, Date startDate, Company company) throws PWCGException
	{
		Airfield companyField = company.determineCurrentAirfieldCurrentMap(startDate);
		if (companyField != null)
		{
		    List<FrontMapIdentifier> mapsForAirfield = AirfieldManager.getMapIdForAirfield(companyField.getName());
		    for (FrontMapIdentifier mapForAirfield : mapsForAirfield)
		    {
		        if (mapForAirfield == mapId)
		        {
		            noteBadlyPlacedCompany(startDate, company, companyField, mapForAirfield);
		        }
		    }
		}
	}

	private void noteBadlyPlacedCompany(Date startDate, Company company, Airfield companyField,
	        FrontMapIdentifier mapForAirfield) throws PWCGException
	{
		ICountry companyCountry = company.determineCompanyCountry(startDate);
		ICountry airfieldCountry = companyField.determineCountryOnDate(startDate);
		if (isBadlyPlaced(companyCountry, airfieldCountry))
		{
			String key = formKey(company, companyField);
			String message = 
					"Badly placed company " + company.determineDisplayName(startDate) + 
					"\n   	field " + companyField.getName() + 
					"\n     map " + mapForAirfield + 
					"\n     date " + DateUtils.getDateStringDashDelimitedYYYYMMDD(startDate) + 
					"\n     side " + companyField.determineCountryOnDate(startDate).getCountryName();
			badAirfields.put(key, message);
		}
	}

	private boolean isBadlyPlaced(ICountry companyCountry, ICountry airfieldCountry)
	{
		if (companyCountry.isSameSide(airfieldCountry))
		{
			return false;
		}
		
		if (airfieldCountry.getCountry() == Country.NEUTRAL)
		{
			if (acceptNeutralPlacement)
			{
				return false;
			}
		}
		
		return true;
	}
	
	private void printBadCompanys()
	{
		for (String message : badAirfields.values())
		{
			System.out.println(message);
		}
	}
	
	private String formKey (Company company, Airfield companyField)
	{
    	return company.getCompanyId() + " at " + companyField.getName();
	}
}
