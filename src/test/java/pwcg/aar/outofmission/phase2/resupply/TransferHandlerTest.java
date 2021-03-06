package pwcg.aar.outofmission.phase2.resupply;

import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.campaign.resupply.ResupplyNeedBuilder;
import pwcg.campaign.resupply.personnel.CompanyTransferData;
import pwcg.campaign.resupply.personnel.TransferHandler;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.product.bos.country.TCServiceManager;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransferHandlerTest
{
    private Campaign campaign;
    
    @Mock private ArmedService armedService;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        
    }

    @BeforeEach
    public void setupTest() throws PWCGException
    {        
        campaign = CampaignCache.makeCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);
        Mockito.when(armedService.getServiceId()).thenReturn(TCServiceManager.WEHRMACHT);
    }

    @Test
    public void testTransfersInForLostCampaignMembers() throws PWCGException
    {
        ResupplyNeedBuilder transferNeedBuilder = new ResupplyNeedBuilder(campaign, armedService);
        TransferHandler companyTransferHandler = new TransferHandler(campaign, transferNeedBuilder);
        
        deactivateCampaignPersonnel();
      
        CompanyTransferData companyTransferData = companyTransferHandler.determineCrewMemberTransfers(armedService);
        Assertions.assertTrue (companyTransferData.getTransferCount() == 3);
    }

    private void deactivateCampaignPersonnel() throws PWCGException
    {
        int numInactivated = 0;
        CrewMembers allActiveCampaignMembers = CrewMemberFilter.filterActiveAI(campaign.getPersonnelManager().getAllCampaignMembers(), campaign.getDate());
        for (CrewMember crewMember : allActiveCampaignMembers.getCrewMemberList())
        {
            Company company = PWCGContext.getInstance().getCompanyManager().getCompany(crewMember.getCompanyId());
            if (!crewMember.isPlayer() && company.getService() == armedService.getServiceId())
            {
                Date inactiveDate = DateUtils.removeTimeDays(campaign.getDate(), 10);
                crewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_KIA, inactiveDate, null);
                ++numInactivated;
            }
            
            if (numInactivated == 3)
            {
                break;
            }
        }
    }
    

    @Test
    public void testTransfersInForLostCrewMembers() throws PWCGException
    {
        ResupplyNeedBuilder transferNeedBuilder = new ResupplyNeedBuilder(campaign, armedService);
        TransferHandler companyTransferHandler = new TransferHandler(campaign, transferNeedBuilder);
        
        deactivateCompanyPersonnel();
      
        CompanyTransferData companyTransferData = companyTransferHandler.determineCrewMemberTransfers(armedService);
        Assertions.assertTrue (companyTransferData.getTransferCount() == 3);
    }

    private void deactivateCompanyPersonnel() throws PWCGException
    {
        int numInactivated = 0;
        CrewMembers allActiveCampaignMembers = CrewMemberFilter.filterActiveAI(campaign.getPersonnelManager().getAllCampaignMembers(), campaign.getDate());
        for (CrewMember crewMember : allActiveCampaignMembers.getCrewMemberList())
        {
            Company company = PWCGContext.getInstance().getCompanyManager().getCompany(crewMember.getCompanyId());
            if (!crewMember.isPlayer() && company.getCompanyId() == CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId())
            {
                Date inactiveDate = DateUtils.removeTimeDays(campaign.getDate(), 10);
                crewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_KIA, inactiveDate, null);
                ++numInactivated;
            }
            
            if (numInactivated == 3)
            {
                break;
            }
        }
    }
}
