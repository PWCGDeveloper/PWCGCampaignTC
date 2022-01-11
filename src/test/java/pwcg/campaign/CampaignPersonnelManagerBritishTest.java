package pwcg.campaign;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CampaignPersonnelManagerBritishTest
{
	@BeforeAll
	public void setupSuite() throws PWCGException
	{
        
	}

    @Test
    public void getCrewMembersTest () throws PWCGException
    {            	    
        Campaign campaign = CampaignCache.makeCampaign(CompanyTestProfile.SEVENTH_DIVISION_PROFILE);

        CompanyPersonnel companyPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(CompanyTestProfile.SEVENTH_DIVISION_PROFILE.getCompanyId());
        CrewMembers crewMembersNoPlayerNoAces = CrewMemberFilter.filterActiveAI(companyPersonnel.getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());        
    	Assertions.assertTrue (crewMembersNoPlayerNoAces.getCrewMemberList().size() == (Company.COMPANY_STAFF_SIZE - 1));
        
        CrewMembers crewMembersNoPlayerWithAces = CrewMemberFilter.filterActiveAIAndAces(companyPersonnel.getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());        
        Assertions.assertTrue (crewMembersNoPlayerWithAces.getCrewMemberList().size() == (Company.COMPANY_STAFF_SIZE - 1));

        CrewMembers crewMembersWithPlayerWithAces = CrewMemberFilter.filterActiveAIAndPlayerAndAces(companyPersonnel.getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());        
        Assertions.assertTrue (crewMembersWithPlayerWithAces.getCrewMemberList().size() == Company.COMPANY_STAFF_SIZE);
    }
}
