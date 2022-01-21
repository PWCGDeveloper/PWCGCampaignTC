package pwcg.campaign.company;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;
import pwcg.testutils.TestIdentifiers;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CompanyManagerTest
{
    Campaign campaign;
    
    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        
        campaign = CampaignCache.makeCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);
    }

    @Test
    public void getCompanyTest() throws PWCGException
    {
        CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        Company company = companyManager.getCompany(TestIdentifiers.TEST_GERMAN_COMPANY_ID);
        assert(company.determineDisplayName(campaign.getDate()).equals("Gross Deutschland Division, 1st Company"));
    }

    @Test
    public void getActiveCompanysTest() throws PWCGException
    {
        CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        List<Company> companys = companyManager.getActiveCompanies(campaign.getDate());
        
        boolean foundGD = false;
        boolean found16th = false;
        boolean found146th = false;
        for (Company company : companys)
        {
            String companyName = company.determineDisplayName(campaign.getDate());
            if (companyName.equals("Gross Deutschland Division, 1st Company"))
            {
                foundGD = true;
            }
            else if (companyName.equals("16th Panzer Division, 3rd Company"))
            {
                found16th = true;
            }
            else if (companyName.equals("146th Tank Division, 2nd Company"))
            {
                found146th = true;
            }
        }
        assert(foundGD);
        assert(found16th);
        assert(found146th);
    }

    @Test
    public void getActiveCompanysForSideTest() throws PWCGException
    {
        CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        List<Company> companys = companyManager.getActiveCompaniesForSide(campaign.getDate(), Side.AXIS);
        
        boolean foundGD = false;
        boolean found16th = false;
        boolean found146th = false;
        for (Company company : companys)
        {
            String companyName = company.determineDisplayName(campaign.getDate());
            if (companyName.equals("Gross Deutschland Division, 1st Company"))
            {
                foundGD = true;
            }
            else if (companyName.equals("16th Panzer Division, 3rd Company"))
            {
                found16th = true;
            }
            else if (companyName.equals("146th Tank Division, 2nd Company"))
            {
                found146th = true;
            }
        }
        assert(foundGD);
        assert(found16th);
        assert(!found146th);
    }

    @Test
    public void getViableCompanysTest() throws PWCGException
    {
        int panzerDivision16 = 201016003;
        CompanyPersonnel personnel = campaign.getPersonnelManager().getCompanyPersonnel(panzerDivision16);
        int numSaved = 0;
        for (CrewMember crewMember : personnel.getCrewMembers().getCrewMemberList())
        {
            if (numSaved > 4)
            {
                crewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_KIA, campaign.getDate(), null);
            }
            ++numSaved;
        }
        
        CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        List<Company> companys = companyManager.getViableCompanies(campaign);
        
        boolean foundGD = false;
        boolean found16th = false;
        boolean found146th = false;
        for (Company company : companys)
        {
            String companyName = company.determineDisplayName(campaign.getDate());
            if (companyName.equals("Gross Deutschland Division, 1st Company"))
            {
                foundGD = true;
            }
            else if (companyName.equals("16th Panzer Division, 3rd Company"))
            {
                found16th = true;
            }
            else if (companyName.equals("146th Tank Division, 2nd Company"))
            {
                found146th = true;
            }
        }
        assert(foundGD);
        assert(!found16th);
        assert(found146th);
    }
}
