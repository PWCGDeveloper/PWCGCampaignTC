package pwcg.campaign.company;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;

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
        Company company = companyManager.getCompany(20111052);
        assert(company.determineDisplayName(campaign.getDate()).equals("I./JG52"));
    }

    @Test
    public void getActiveCompanysTest() throws PWCGException
    {
        CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        List<Company> companys = companyManager.getActiveCompanies(campaign.getDate());
        
        boolean foundJG52 = false;
        boolean foundStg2 = false;
        boolean found132Reg = false;
        boolean foundHs129 = false;
        for (Company company : companys)
        {
            String companyName = company.determineDisplayName(campaign.getDate());
            if (companyName.equals("I./JG52"))
            {
                foundJG52 = true;
            }
            else if (companyName.equals("II./St.G.2"))
            {
                foundStg2 = true;
            }
            else if (companyName.equals("132nd Bomber Air Regiment"))
            {
                found132Reg = true;
            }
            else if (companyName.equals("IV(Pz)./Sch.G.2"))
            {
                foundHs129 = true;
            }
        }
        assert(foundJG52);
        assert(foundStg2);
        assert(found132Reg);
        assert(!foundHs129);
    }

    @Test
    public void getActiveCompanysForSideTest() throws PWCGException
    {
        CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        List<Company> companys = companyManager.getActiveCompaniesForSide(campaign.getDate(), Side.AXIS);
        
        boolean foundJG52 = false;
        boolean foundStg2 = false;
        boolean found132Reg = false;
        boolean foundHs129 = false;
        for (Company company : companys)
        {
            String companyName = company.determineDisplayName(campaign.getDate());
            if (companyName.equals("I./JG52"))
            {
                foundJG52 = true;
            }
            else if (companyName.equals("II./St.G.2"))
            {
                foundStg2 = true;
            }
            else if (companyName.equals("132nd Bomber Air Regiment"))
            {
                found132Reg = true;
            }
            else if (companyName.equals("IV(Pz)./Sch.G.2"))
            {
                foundHs129 = true;
            }
        }
        assert(foundJG52);
        assert(foundStg2);
        assert(!found132Reg);
        assert(!foundHs129);
    }

    @Test
    public void getViableCompanysTest() throws PWCGException
    {
        int II_StG2_id = 20122002;
        CompanyPersonnel personnel = campaign.getPersonnelManager().getCompanyPersonnel(II_StG2_id);
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
        
        boolean foundJG52 = false;
        boolean foundStg2 = false;
        boolean found132Reg = false;
        boolean foundHs129 = false;
        for (Company company : companys)
        {
            String companyName = company.determineDisplayName(campaign.getDate());
            if (companyName.equals("I./JG52"))
            {
                foundJG52 = true;
            }
            else if (companyName.equals("II./St.G.2"))
            {
                foundStg2 = true;
            }
            else if (companyName.equals("132nd Bomber Air Regiment"))
            {
                found132Reg = true;
            }
            else if (companyName.equals("IV(Pz)./Sch.G.2"))
            {
                foundHs129 = true;
            }
        }
        assert(foundJG52);
        assert(!foundStg2);
        assert(found132Reg);
        assert(!foundHs129);
    }
}
