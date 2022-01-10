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

        CompanyPersonnel squadronPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(CompanyTestProfile.SEVENTH_DIVISION_PROFILE.getCompanyId());
        CrewMembers squadronMembersNoPlayerNoAces = CrewMemberFilter.filterActiveAI(squadronPersonnel.getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());        
    	Assertions.assertTrue (squadronMembersNoPlayerNoAces.getCrewMemberList().size() == (Company.COMPANY_STAFF_SIZE - 1));
        
        CrewMembers squadronMembersNoPlayerWithAces = CrewMemberFilter.filterActiveAIAndAces(squadronPersonnel.getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());        
        Assertions.assertTrue (squadronMembersNoPlayerWithAces.getCrewMemberList().size() == (Company.COMPANY_STAFF_SIZE - 1));

        CrewMembers squadronMembersWithPlayerWithAces = CrewMemberFilter.filterActiveAIAndPlayerAndAces(squadronPersonnel.getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());        
        Assertions.assertTrue (squadronMembersWithPlayerWithAces.getCrewMemberList().size() == Company.COMPANY_STAFF_SIZE);
    }
}