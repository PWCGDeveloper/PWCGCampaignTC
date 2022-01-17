package pwcg.aar.outofmission.phase2.resupply;

import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.campaign.resupply.CompanyNeedFactory;
import pwcg.campaign.resupply.CompanyNeedFactory.CompanyNeedType;
import pwcg.campaign.resupply.ICompanyNeed;
import pwcg.campaign.resupply.ServiceResupplyNeed;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.product.bos.country.TCServiceManager;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ServiceTransferNeedTest
{
    private Campaign campaign;
    private static final int JASTA_16 = 401016;
    
    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        
        campaign = CampaignCache.makeCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);
     }

    @Test
    public void testTransfersWithNoCrewMembers() throws PWCGException
    {
        deactivateCompanyPersonnel();
        
        CompanyNeedFactory companyNeedFactory = new CompanyNeedFactory(CompanyNeedType.PERSONNEL);
        ServiceResupplyNeed serviceTransferNeed = new ServiceResupplyNeed(campaign, TCServiceManager.WEHRMACHT, companyNeedFactory);
        serviceTransferNeed.determineResupplyNeed();
        
        Assertions.assertTrue (serviceTransferNeed.hasNeedyCompany() == true);
        
        boolean jasta16Needs = false;
        while (serviceTransferNeed.hasNeedyCompany())
        {
            ICompanyNeed selectedCompanyNeed = serviceTransferNeed.chooseNeedyCompany();
            if (selectedCompanyNeed.getCompanyId() == JASTA_16)
            {
                jasta16Needs = true;
                break;
            }
        }
        Assertions.assertTrue (jasta16Needs);
    }
    

    private void deactivateCompanyPersonnel() throws PWCGException
    {
        CrewMembers jasta16CrewMembers = CrewMemberFilter.filterActiveAI(campaign.getPersonnelManager().getCompanyPersonnel(JASTA_16).getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());
        int numInactivated = 0;
        for (CrewMember crewMember : jasta16CrewMembers.getCrewMemberList())
        {
            if (!crewMember.isPlayer())
            {
                crewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_KIA, campaign.getDate(), null);
                Date inactiveDate = DateUtils.removeTimeDays(campaign.getDate(), 9);
                crewMember.setInactiveDate(inactiveDate);
                ++numInactivated;                
            }
            
            if (numInactivated == 3)
            {
                break;
            }
        }
    }
}
