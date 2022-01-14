package pwcg.campaign.personnel;

import java.util.List;

import org.junit.jupiter.api.Test;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;

public class InitialReplacementStafferTest
{
    
    @Test
    public void generateReplacementsTest() throws PWCGException
    {
        
        Campaign campaign = CampaignCache.makeCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);

        List<ArmedService> armedServices = ArmedServiceFactory.createServiceManager().getAllActiveArmedServices(campaign.getDate());
        for (ArmedService service : armedServices)
        {
            InitialReplacementStaffer initialReplacementStaffer = new InitialReplacementStaffer(campaign, service);
            CrewMembers crewMembers = initialReplacementStaffer.staffReplacementsForService();
            
            assert(crewMembers.getCrewMemberCollection().size() == InitialReplacementStaffer.NUM_INITIAL_REPLACEMENTS);
            for (CrewMember replacement : crewMembers.getCrewMemberCollection().values())
            {
                assert(replacement.getCompanyId() == Company.REPLACEMENT);
            }
        }
    }

}
