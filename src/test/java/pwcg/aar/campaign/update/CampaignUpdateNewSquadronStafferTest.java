package pwcg.aar.campaign.update;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyViability;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CampaignUpdateNewCompanyStafferTest
{
    private Campaign campaign;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        
        campaign = CampaignCache.makeCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);
    }

    @Test
    public void testCompanyAdded() throws PWCGException
    {
        Date newDate = DateUtils.getDateYYYYMMDD("19420801");
        campaign.setDate(newDate);
        
        CampaignUpdateNewCompanyStaffer newCompanyStaffer = new CampaignUpdateNewCompanyStaffer(campaign);
        List<Integer> companysAdded = newCompanyStaffer.staffNewCompanies();
        assert(companysAdded.size() > 0);
        for (int companyId : companysAdded)
        {
            Company company = PWCGContext.getInstance().getCompanyManager().getCompany(companyId);
            assert(CompanyViability.isCompanyActive(company, campaign.getDate()));
            
            CompanyPersonnel companyPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(companyId);
            assert(companyPersonnel != null);

            CrewMembers crewMembers = CrewMemberFilter.filterActiveAIAndPlayerAndAces(companyPersonnel.getCrewMembersWithAces().getCrewMemberCollection(),campaign.getDate());
            assert(crewMembers != null);
            assert(crewMembers.getActiveCount(campaign.getDate()) >= Company.COMPANY_STAFF_SIZE);
        }
    }
}
