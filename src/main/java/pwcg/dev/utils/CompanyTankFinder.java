package pwcg.dev.utils;

import java.util.Date;
import java.util.List;

import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.tank.CompanyTankAssignment;
import pwcg.campaign.tank.ITankTypeFactory;
import pwcg.campaign.tank.TankTypeInformation;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class CompanyTankFinder 
{
	static public void main (String[] args)
	{
        try
		{
			CompanyTankFinder finder = new CompanyTankFinder();
			
			Date startDate = DateUtils.getBeginningOfWar();
			Date endDate = DateUtils.getEndOfWar();
			
			finder.findPlane("bf109", startDate, endDate);
			
			finder.printPlanes();
		}
		catch (Exception e)
		{
			 PWCGLogger.logException(e);;
		}
	}

    
    private void findPlane(String planeId, Date startDate, Date endDate) throws PWCGException  
    {       
        List<Company> allSq =  PWCGContext.getInstance().getCompanyManager().getAllCompanies();
        PWCGLogger.log(LogLevel.DEBUG, "TankType Id: " + planeId);
        for (Company company : allSq)
        {
            boolean hasPlane = false;
            for (CompanyTankAssignment planeAssignment : company.getTankAssignments())
            {
                if (planeAssignment.getArchType().equals(planeId))
                {
                    if (!planeAssignment.getCompanyWithdrawal().before(endDate))
                    {
                        hasPlane = true;
                    }
                }
            }

            if (hasPlane)
            {
                PWCGLogger.log(LogLevel.DEBUG, "" + company.getCompanyId());
            }
        }
    }
    
    private void printPlanes() throws PWCGException  
    {       
        List<Company> allSq =  PWCGContext.getInstance().getCompanyManager().getAllCompanies();
        ITankTypeFactory planeTypeFactory = PWCGContext.getInstance().getPlayerTankTypeFactory();
        for (Company company : allSq)
        {
            PWCGLogger.log(LogLevel.DEBUG, "Company: " + company.getCompanyId());
            boolean hasPlane = false;
            for (CompanyTankAssignment planeAssignment : company.getTankAssignments())
            {
                for (TankTypeInformation plane : planeTypeFactory.createTankTypesForArchType(planeAssignment.getArchType()))
                {
                    PWCGLogger.log(LogLevel.DEBUG, "        " + plane.getDisplayName());
                }
            }

            if (hasPlane)
            {
                PWCGLogger.log(LogLevel.DEBUG, "" + company.getCompanyId());
            }
        }
    }
}
