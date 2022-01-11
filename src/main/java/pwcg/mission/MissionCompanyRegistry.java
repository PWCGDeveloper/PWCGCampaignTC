package pwcg.mission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.company.Company;
import pwcg.core.exception.PWCGException;

public class MissionCompanyRegistry
{
    private Map<Integer, Company> companysInUse = new HashMap<>();

    public boolean isCompanyAvailable(Company company)
    {
        if (companysInUse.containsKey(company.getCompanyId()))
        {
            return false;
        }
        return true;
    }

    public void registerCompanyForUse(Company company) throws PWCGException
    {
        if (companysInUse.containsKey(company.getCompanyId()))
        {
            throw new PWCGException("Duplicate use of company " + company.getCompanyId());
        }
        companysInUse.put(company.getCompanyId(), company);
    }

    public List<Company> removeCompanysInUse(List<Company> companys)
    {
        List<Company> companysNotInUse = new ArrayList<>();
        for (Company company : companys)
        {
            if (!companysInUse.containsKey(company.getCompanyId()))
            {
                companysNotInUse.add(company);
            }
        }
        return companysNotInUse;
    }

}
