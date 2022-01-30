package pwcg.dev.utils;

import java.util.List;
import java.util.TreeMap;

import pwcg.campaign.company.Company;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.PWCGLocation;
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
        
        List<Company> alLCompanies =  PWCGContext.getInstance().getCompanyManager().getAllCompanies();
        for (Company company : alLCompanies)
        {
            PWCGLocation base = company.determineCurrentBaseCurrentMap(DateUtils.getDateYYYYMMDD("19430801"));
            if (base != null)
            {
                airfieldsOnMapSorted.put(company.getCompanyId(), base.getName());
            }
        }

        for (int companyId : airfieldsOnMapSorted.keySet())
        {
            PWCGLogger.log(LogLevel.DEBUG, "" + companyId);
        }
    }
}
