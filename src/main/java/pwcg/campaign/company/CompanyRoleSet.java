package pwcg.campaign.company;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.tank.PwcgRole;
import pwcg.campaign.tank.PwcgRoleCategory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.RandomNumberGenerator;

public class CompanyRoleSet
{
    private List<CompanyRolePeriod> companyRolePeriods = new ArrayList<>();

    public PwcgRole selectRoleForMission(Date date) throws PWCGException 
    {
        PwcgRole selectedRole = PwcgRole.ROLE_NONE;
        CompanyRolePeriod companyRoleForDate = selectRoleSetByDate(date);
        if (companyRoleForDate != null)
        {
            selectedRole = selectRoleFromSet(companyRoleForDate);
        }
        else
        {
            throw new PWCGException("No role found for company on date " + DateUtils.getDateStringDashDelimitedYYYYMMDD(date));
        }
        
        return selectedRole;
    }
    
    public PwcgRoleCategory selectCompanyPrimaryRoleCategory(Date date) throws PWCGException 
    {
        CompanyRolePeriod companyRoleForDate = selectRoleSetByDate(date);
        PwcgRoleCategory selectedRoleCategory = CompanyRoleWeightCalculator.calculateHeaviestCompanyRole(companyRoleForDate);
        return selectedRoleCategory;
    }

    private PwcgRole selectRoleFromSet(CompanyRolePeriod companyRole) throws PWCGException 
    {
        PwcgRole selectedRole = PwcgRole.ROLE_NONE;
        
        int totalWeight = 0;
        for (CompanyRoleWeight companyRoleWeight : companyRole.getWeightedRoles())
        {
            totalWeight += companyRoleWeight.getWeight();
        }
        
        int evaluatedWeight = 0;
        int roll = RandomNumberGenerator.getRandom(totalWeight);
        for (CompanyRoleWeight companyRoleWeight : companyRole.getWeightedRoles())
        {
            evaluatedWeight += companyRoleWeight.getWeight();
            if (roll <= evaluatedWeight)
            {
                selectedRole = companyRoleWeight.getRole();
                break;
            }
        }
        
        return selectedRole;
    }
    
    public CompanyRolePeriod selectRoleSetByDate(Date date) throws PWCGException 
    {
        CompanyRolePeriod rolesForPeriod = null;
        for (CompanyRolePeriod companyRole : companyRolePeriods)
        {
            Date startDate = companyRole.getStartDate();
            if (DateUtils.isDateOnOrBefore(date, startDate))
            {
                rolesForPeriod = companyRole;
            }
        }
        
        if (rolesForPeriod == null)
        {
            throw new PWCGException("No roles for date range " +  date);
        }

        return rolesForPeriod;
    }

    public boolean isCompanyThisRole (Date date, PwcgRole requestedRole) throws PWCGException 
    {
        CompanyRolePeriod companyRoleForDate = selectRoleSetByDate(date);
        for (CompanyRoleWeight companyRoleWeight : companyRoleForDate.getWeightedRoles())
        {
            if (companyRoleWeight.getRole() == requestedRole)
            {
                return true;
            }
        }
        
        return false;
    }

    public List<CompanyRolePeriod> getCompanyRolePeriods()
    {
        return companyRolePeriods;
    }
    
    public void overrideRolesForTest(List<CompanyRolePeriod> companyRolePeriods)
    {
        this.companyRolePeriods = companyRolePeriods;
    }
}
