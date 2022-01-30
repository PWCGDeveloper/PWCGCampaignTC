package pwcg.dev.utils;

import java.util.List;
import java.util.TreeMap;

import pwcg.campaign.company.Company;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class CompanyMapFinder 
{
	static public void main (String[] args)
	{
        try
		{
			CompanyMapFinder finder = new CompanyMapFinder();
			finder.companyIsOnMap();
		}
		catch (Exception e)
		{
			 PWCGLogger.logException(e);
		}
	}

    
    private void companyIsOnMap() throws PWCGException  
    {     
        
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.KUBAN_MAP);
        
        TreeMap<Integer, String> airfieldsOnMapSorted = new TreeMap<>();
        
        List<Company> allSq =  PWCGContext.getInstance().getCompanyManager().getAllCompanies();
        for (Company company : allSq)
        {
            Airfield airfield = company.determineCurrentAirfieldCurrentMap(DateUtils.getDateYYYYMMDD("19430801"));
            if (airfield != null)
            {
                airfieldsOnMapSorted.put(company.getCompanyId(), airfield.getName());
            }
        }

        for (int companyId : airfieldsOnMapSorted.keySet())
        {
            PWCGLogger.log(LogLevel.DEBUG, "" + companyId);
        }
    }
}
