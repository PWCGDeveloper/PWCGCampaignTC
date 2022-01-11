package pwcg.dev.utils;

import java.util.Date;
import java.util.List;
import java.util.Map;

import pwcg.campaign.api.Side;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.tank.PwcgRoleCategory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.dev.utils.AirfieldDistanceOrganizer.AirfieldSet;

public class AirfieldDistanceEvaluator
{

    private static String[] mapTransitionDates = 
    {
            "19430301",
            "19430918",
            "19430927",
            "19431004",
            "19431008",
    };

    AirfieldDistanceOrganizer airfieldDistanceOrganizer = new AirfieldDistanceOrganizer();
    
    public static void main(String[] args) throws PWCGException
    {
	    UserDir.setUserDir();
	    
	    
        AirfieldDistanceEvaluator airfieldReporter = new AirfieldDistanceEvaluator();
        airfieldReporter.process();
    }

    public void process()
    {

        try
        {
            PWCGContext.getInstance();
                        
            for (int i = 0; i < mapTransitionDates.length - 1; ++i)
            {
                String startDateString = mapTransitionDates[i];
                String endDateString = mapTransitionDates[i+1];
                
                PWCGLogger.log(LogLevel.DEBUG, "\n\n\nDate is " + startDateString + " to " + endDateString);
                
                Date startDate = DateUtils.getDateWithValidityCheck(startDateString);
                             
                airfieldDistanceOrganizer.process(startDate, FrontMapIdentifier.MOSCOW_MAP);
                
                analyzeCompanys(airfieldDistanceOrganizer.alliedAirfieldSet, startDate, Side.ALLIED);
                
                analyzeCompanys(airfieldDistanceOrganizer.axisAirfieldSet, startDate, Side.AXIS);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void analyzeCompanys(AirfieldSet airfieldSet, Date dateNow, Side sideCompanys) throws PWCGException
    {
        CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        
        Map <String, Airfield> fighterFields = airfieldSet.getFighterFields();
        Map <String, Airfield> bomberFields = airfieldSet.getBomberFields();

        List<Company> allActiveCompanys = companyManager.getActiveCompanies(dateNow);
        for (Company company : allActiveCompanys)
        {
            boolean bad = false;
            
            Airfield companyField = company.determineCurrentAirfieldAnyMap(dateNow);
            
            if (company.determineCompanyCountry(dateNow).getSide() != sideCompanys)
            {
                continue;
            }
            
            if (companyField == null)
            {
                continue;
            }
            
            String reason = " ";
            
            PwcgRoleCategory companyRoleCategory = company.determineCompanyPrimaryRoleCategory(dateNow);
            if (companyRoleCategory == PwcgRoleCategory.FIGHTER)
            {
                if (!fighterFields.containsKey(companyField.getName()))
                {
                    if (bomberFields.containsKey(companyField.getName()))
                    {
                        reason = " ... is at a bomber field";
                        bad = true;
                    }
                    else if (airfieldSet.getFieldsTooClose().containsKey(companyField.getName()))
                    {
                        reason = " ... is too close";
                        bad = true;
                    }
                    else if (airfieldSet.getFieldsTooFar().containsKey(companyField.getName()))
                    {
                        reason = " ... is too far";
                        bad = true;
                    }
                }
            }
            else
            {
                
                if (!bomberFields.containsKey(companyField.getName()))
                {
                    if (fighterFields.containsKey(companyField.getName()))
                    {
                        reason = " ... is at a fighter field";
                        bad = true;
                    }
                    else if (airfieldSet.getFieldsTooClose().containsKey(companyField.getName()))
                    {
                        reason = " ... is too close";
                        bad = true;
                    }
                    else if (airfieldSet.getFieldsTooFar().containsKey(companyField.getName()))
                    {
                        reason = " ... is too far";
                        bad = true;
                    }
                }
            }
            

            if (bad)
            {
                PWCGLogger.log(LogLevel.DEBUG, "\nCompany " + company.getCompanyId() + " at field " + companyField.getName() + " on date " + dateNow + reason);
                int distance = AirfieldReporter.getDistanceToFront(companyField, sideCompanys, dateNow);
                PWCGLogger.log(LogLevel.DEBUG, companyField.getName() + "   Km to front: " + distance);
                AirfieldBestMMatchFinder.recommendBestMatch(company, dateNow);
            }
        }
    }
}
