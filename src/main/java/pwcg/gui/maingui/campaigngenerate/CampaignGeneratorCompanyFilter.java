package pwcg.gui.maingui.campaigngenerate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.CampaignAces;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.context.AceManager;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.MapForBaseFinder;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.TankAce;
import pwcg.campaign.tank.PwcgRoleCategory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class CampaignGeneratorCompanyFilter
{
	public List<String> makeCompanyChoices(Date campaignDate, ArmedService dateCorrectedService, FrontMapIdentifier selectedCampaignMap, String selectedRole, boolean playerIsCommander) throws PWCGException 
	{
		List<String> validCompanys = new ArrayList<>();
	    try
	    {
    		CompanyManager companyManager =  PWCGContext.getInstance().getCompanyManager();
    		List<Company> companyList = companyManager.getPlayerCompaniesByService(dateCorrectedService, campaignDate);
            PWCGLogger.log(LogLevel.DEBUG, "makeCompanyChoices company list size: " + companyList.size());
    		for (Company company : companyList)
    		{
                PWCGLogger.log(LogLevel.DEBUG, company.determineDisplayName(campaignDate) + " makeCompanyChoices evaluate company");

       			if (rejectBecauseWrongRole(company, campaignDate, selectedRole))
    			{
	                PWCGLogger.log(LogLevel.DEBUG, company.determineDisplayName(campaignDate) + ": Cannot command  - incorrect role");
    				continue;
    			}
    			
       			if (rejectBecauseCommandConflict(company, campaignDate, playerIsCommander))
    			{
                    PWCGLogger.log(LogLevel.DEBUG, company.determineDisplayName(campaignDate) + ": Cannot command  - commaned by ace");
    				continue;
    			}
    			
       			if (rejectBecauseWrongMap(company, campaignDate, selectedCampaignMap))
    			{
                    PWCGLogger.log(LogLevel.DEBUG, company.determineDisplayName(campaignDate) + ": Cannot command  - commaned by ace");
    				continue;
    			}
    			
				String companyDisplayName = company.determineDisplayName(campaignDate);
				validCompanys.add(companyDisplayName);
    		}
	    }
	    catch (Exception exp)
	    {
            PWCGLogger.logException(exp);
            throw exp;
	    }
	    
	    return validCompanys;
	}
	
	private boolean rejectBecauseWrongRole(Company company, Date campaignDate, String roleDesc) throws PWCGException
	{
	    PwcgRoleCategory role = PwcgRoleCategory.getRoleCategoryFromDescription(roleDesc);
        PwcgRoleCategory companyRole = company.determineCompanyPrimaryRoleCategory(campaignDate);
        if (role == companyRole)
        {
            return false;
        }
		
		return true;
	}
	
	private boolean rejectBecauseCommandConflict(Company company, Date campaignDate, boolean playerIsCommander) throws PWCGException
	{
		AceManager aceManager = PWCGContext.getInstance().getAceManager();
		CampaignAces aces =  aceManager.loadFromHistoricalAces(campaignDate);
		List<TankAce> companyAces =  aceManager.getActiveAcesForCompany(aces, campaignDate, company.getCompanyId());
		if (companyAces.size() > 0)
		{
			if (playerIsCommander && company.isCommandedByAce(companyAces, campaignDate))
			{
				return true;
			}
		}    					
		
		return false;
	}
	
	private boolean rejectBecauseWrongMap(Company company, Date campaignDate, FrontMapIdentifier selectedCampaignMap) throws PWCGException
	{
		if (selectedCampaignMap == null)
		{
			return false;
		}
		
    	String airfieldName = company.determineBaseName(campaignDate);
    	List<FrontMapIdentifier> airfieldMaps = MapForBaseFinder.getMapForBase(airfieldName);
    	
    	for (FrontMapIdentifier airfieldMap : airfieldMaps)
    	{
	    	if (airfieldMap == selectedCampaignMap)
	    	{
				return false;
	    	}
    	}
		
		return true;
	}

}
