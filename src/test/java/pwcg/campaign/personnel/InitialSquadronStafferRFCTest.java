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
public class InitialCompanyStafferRFCTest
{
    @Test
    public void generateReconPersonnelTest() throws PWCGException
    {
        
        Campaign campaign = CampaignCache.makeCampaign(CompanyTestProfile.RFC_2_PROFILE);

        Company company = PWCGContext.getInstance().getCompanyManager().getCompany(CompanyTestProfile.RFC_2_PROFILE.getCompanyId());
        
        InitialCompanyStaffer initialCompanyStaffer = new InitialCompanyStaffer(campaign, company);
        CompanyPersonnel companyPersonnel = initialCompanyStaffer.generatePersonnel();        
        CrewMembers crewMembers = CrewMemberFilter.filterActiveAIAndPlayerAndAces(companyPersonnel.getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());        
        
        assert(crewMembers.getCrewMemberList().size() == Company.COMPANY_STAFF_SIZE);
    }
}
