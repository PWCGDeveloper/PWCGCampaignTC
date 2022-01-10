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
public class InitialSquadronStafferLWTest
{
    @Test
    public void generateFighterPersonnelTest() throws PWCGException
    {
        
        Campaign campaign = CampaignCache.makeCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);

        Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId());
        
        InitialCompanyStaffer initialSquadronStaffer = new InitialCompanyStaffer(campaign, squadron);
        CompanyPersonnel squadronPersonnel = initialSquadronStaffer.generatePersonnel();        
        CrewMembers squadronMembers = CrewMemberFilter.filterActiveAIAndPlayerAndAces(squadronPersonnel.getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());        

        assert(squadronMembers.getCrewMemberList().size() == Company.COMPANY_STAFF_SIZE);
    }
}
