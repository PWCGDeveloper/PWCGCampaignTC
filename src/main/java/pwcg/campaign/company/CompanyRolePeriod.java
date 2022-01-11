package pwcg.campaign.company;

import java.util.Date;
import java.util.List;

public class CompanyRolePeriod
{
    private Date startDate;
    private List<CompanyRoleWeight> weightedRoles;

    public List<CompanyRoleWeight> getWeightedRoles()
    {
        return weightedRoles;
    }

    public void setWeightedRoles(List<CompanyRoleWeight> weightedRoles)
    {
        this.weightedRoles = weightedRoles;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }
}
