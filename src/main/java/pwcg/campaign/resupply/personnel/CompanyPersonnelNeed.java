package pwcg.campaign.resupply.personnel;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.campaign.resupply.ICompanyNeed;
import pwcg.core.exception.PWCGException;

public class CompanyPersonnelNeed implements ICompanyNeed
{
    private Campaign campaign;
    private Company company;
    private int transfersNeeded = 0;

    public CompanyPersonnelNeed(Campaign campaign, Company company)
    {
        this.campaign = campaign;
        this.company = company;
    }
    
    public void determineResupplyNeeded() throws PWCGException
    {
        CompanyPersonnel companyPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(company.getCompanyId());
        CrewMembers activeCrewMembers = CrewMemberFilter.filterActiveAIAndPlayerAndAces(companyPersonnel.getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());
        int activeCompanySize = activeCrewMembers.getActiveCount(campaign.getDate());
        int getRecentlyInactive = companyPersonnel.getRecentlyInactiveCrewMembers().getActiveCount(campaign.getDate());
      
        if ((Company.COMPANY_STAFF_SIZE -  activeCompanySize - getRecentlyInactive) > 0)
        {
            transfersNeeded = Company.COMPANY_STAFF_SIZE -  activeCompanySize - getRecentlyInactive;
        }
        else
        {
            transfersNeeded = 0;
        }
    }
    
    public int getCompanyId()
    {
        return company.getCompanyId();
    }

    public boolean needsResupply()
    {
        return (transfersNeeded > 0);
    }

    public void noteResupply()
    {
        --transfersNeeded;
    }

    @Override
    public int getNumNeeded()
    {
        return transfersNeeded;
    }
}
