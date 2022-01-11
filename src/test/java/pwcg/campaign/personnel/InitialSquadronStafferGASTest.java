package pwcg.campaign.personnel;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;

@ExtendWith(MockitoExtension.class)
public class InitialCompanyStafferGASTest
{
    @Test
    public void generatePersonnelWithAcesTest() throws PWCGException
    {
        
        Campaign campaign = CampaignCache.makeCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);

        Company company = PWCGContext.getInstance().getCompanyManager().getCompany(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId());
        
        InitialCompanyStaffer initialCompanyStaffer = new InitialCompanyStaffer(campaign, company);
        CompanyPersonnel companyPersonnel = initialCompanyStaffer.generatePersonnel();        
        CrewMembers crewMembers = companyPersonnel.getCrewMembersWithAces();
        CrewMembers filteredCrewMembers = CrewMemberFilter.filterActiveAIAndPlayerAndAces(crewMembers.getCrewMemberCollection(), campaign.getDate());        
        
        assert(filteredCrewMembers.getCrewMemberList().size() == Company.COMPANY_STAFF_SIZE);
    }

}
