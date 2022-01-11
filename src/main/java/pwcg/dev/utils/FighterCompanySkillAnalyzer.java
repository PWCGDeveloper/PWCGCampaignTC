package pwcg.dev.utils;

import java.util.Date;
import java.util.List;

import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.tank.PwcgRoleCategory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class FighterCompanySkillAnalyzer 
{
	static public void main (String[] args)
	{
		try
		{
			FighterCompanySkillAnalyzer skillAnalyzer = new FighterCompanySkillAnalyzer();
			
			Date startDate = DateUtils.getEndOfWar();
			
			skillAnalyzer.findPlane(startDate);
		}
		catch (Throwable e)
		{
			 PWCGLogger.logException(e);;
		}
	}

	
	private void findPlane(Date startDate) throws PWCGException  
	{		
		List<Company> allSq =  PWCGContext.getInstance().getCompanyManager().getAllCompanies();
		int totalSkill = 0;
		int totalFighterSquads = 0;
		for (Company company : allSq)
		{
		    PwcgRoleCategory companyPrimaryRole = company.determineCompanyPrimaryRoleCategory(DateUtils.getEndOfWar());
            if (companyPrimaryRole == PwcgRoleCategory.FIGHTER)
		    {
		        int companyQuality = company.determineCompanySkill(startDate);
		        totalSkill += companyQuality;
		        ++totalFighterSquads;
		    }
		}
		
		int averageSkill = totalSkill / totalFighterSquads;
        PWCGLogger.log(LogLevel.INFO, "Average fighter skill: " + averageSkill);
	
	}
}
