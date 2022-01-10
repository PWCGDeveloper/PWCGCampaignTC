package pwcg.aar.campaign.update;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.product.fc.country.TCServiceManager;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;

public class CampaignServiceChangeHandlerTest
{
    public CampaignServiceChangeHandlerTest() throws PWCGException
    {
        
    }

    @Test
    public void testRafTransition() throws PWCGException 
    {
        Campaign campaign = CampaignCache.makeCampaign(CompanyTestProfile.RFC_2_PROFILE);
        ArmedService service = campaign.determinePlayerCompanies().get(0).determineServiceForCompany(campaign.getDate());
        ICountry country = service.getCountry();
        CompanyPersonnel personnel = campaign.getPersonnelManager().getCompanyPersonnel(CompanyTestProfile.RFC_2_PROFILE.getCompanyId());

        Assertions.assertTrue (country.getCountry() == Country.BRITAIN);
        Assertions.assertTrue (service.getName().equals(BritishServiceBuilder.BRITISH_ARMY_NAME));

        CrewMembers rfcCrewMembers = CrewMemberFilter.filterActiveAIAndPlayerAndAces(personnel.getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());
        for (CrewMember crewMember : rfcCrewMembers.getCrewMemberList())
        {
            IRankHelper rank = RankFactory.createRankHelper();
            int rankPos = rank.getRankPosByService(crewMember.getRank(), service);
            assert(rankPos >= 0);
        }

        ServiceChangeHandler serviceChangeHandler = new ServiceChangeHandler(campaign);
        serviceChangeHandler.handleChangeOfService(DateUtils.getRAFDate());
                
        service = campaign.determinePlayerCompanies().get(0).determineServiceForCompany(DateUtils.getRAFDate());
        Assertions.assertTrue (service.getCountry().getCountry() == Country.BRITAIN);
        Assertions.assertTrue (service.getName().equals(BritishServiceBuilder.BRITISH_ARMY_NAME));

        CrewMembers rafCrewMembers = CrewMemberFilter.filterActiveAIAndPlayerAndAces(personnel.getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());
        for (CrewMember crewMember : rafCrewMembers.getCrewMemberList())
        {
            IRankHelper rank = RankFactory.createRankHelper();
            int rankPos = rank.getRankPosByService(crewMember.getRank(), service);
            assert(rankPos >= 0);
        }
    }
}
