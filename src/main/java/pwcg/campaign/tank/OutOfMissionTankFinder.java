package pwcg.campaign.tank;

import java.util.Date;

import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class OutOfMissionTankFinder
{

    public TankTypeInformation findTankType(Company company, PwcgRoleCategory roleCategory, Date date) throws PWCGException
    {
        TankTypeInformation planeType = findPreferredTankTypeForCompany(company, date);
        if (planeType == null)
        {
            planeType = findAlternativeTankTypeForCompany(company, roleCategory, date);
        }
        
        if (planeType == null)
        {
            planeType = findAnyTankTypeForCountryAndDate(company, date);
        }
        
        if (planeType == null)
        {
            planeType = findEarliestTankTypeForCompany(company);
        }

        if (planeType == null)
        {
            throw new PWCGException("Unable to find any plane for company " + company.determineDisplayName(date) + " on date " + DateUtils.getDateStringYYYYMMDD(date));
        }

        return planeType;
    }

    private TankTypeInformation findPreferredTankTypeForCompany(Company company, Date date) throws PWCGException
    {
        TankTypeInformation planeType = company.determineBestPlane(date);
        return planeType;
    }

    private TankTypeInformation findAlternativeTankTypeForCompany(Company company, PwcgRoleCategory roleCategory, Date date) throws PWCGException
    {
        TankTypeInformation planeType = PWCGContext.getInstance().getTankTypeFactory().findActiveTankTypeByCountryDateAndRole(
                company.determineCompanyCountry(date), date, roleCategory);
        return planeType;        
    }

    private TankTypeInformation findAnyTankTypeForCountryAndDate(Company company, Date date) throws PWCGException
    {
        TankTypeInformation planeType = PWCGContext.getInstance().getTankTypeFactory().findAnyTankTypeForCountryAndDate(
                company.determineCompanyCountry(date), date);
        return planeType;        
    }

    private TankTypeInformation findEarliestTankTypeForCompany(Company company) throws PWCGException
    {
        TankTypeInformation planeType = company.determineEarliestPlane();
        return planeType;
    }
}
