package pwcg.campaign.personnel;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;

public class InitialCompanyStafferBritishTest
{
    @Test
    public void generateReconPersonnelTest() throws PWCGException
    {
        
        Campaign campaign = CampaignCache.makeCampaign(CompanyTestProfile.SEVENTH_DIVISION_PROFILE);

        Company company = PWCGContext.getInstance().getCompanyManager().getCompany(CompanyTestProfile.SEVENTH_DIVISION_PROFILE.getCompanyId());
        
        InitialCompanyStaffer initialCompanyStaffer = new InitialCompanyStaffer(campaign, company);
        CompanyPersonnel companyPersonnel = initialCompanyStaffer.generatePersonnel();        
        CrewMembers crewMembers = CrewMemberFilter.filterActiveAIAndPlayerAndAces(companyPersonnel.getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());        

        assert(crewMembers.getCrewMemberList().size() == Company.COMPANY_STAFF_SIZE);
        for (CrewMember crewMember : crewMembers.getCrewMemberList())
        {
            if(crewMember.determineRankPos(campaign.getDate()) == 0)
            {
                Assertions.assertTrue(crewMember.getCrewMemberVictories().getTankVictoryCount() > 0);
            }
        }
    }
}
