package pwcg.aar.outofmission.phase2.resupply;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.data.AARPersonnelLosses;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.resupply.personnel.CompanyPersonnelNeed;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
public class CompanyTransferNeedTest
{
    @Mock private Campaign campaign;
    @Mock private Company company;
    @Mock private CampaignPersonnelManager campaignPersonnelManager;
    @Mock private AARPersonnelLosses lossesInMissionData;
    @Mock private CompanyPersonnel companyPersonnel;
    @Mock private CrewMembers activeCrewMembers;
    @Mock private CrewMembers inactiveCrewMembers;

    private Map<Integer, CrewMember> activeCrewMemberCollection = new HashMap<>();
    private Map<Integer, CrewMember> inactiveCrewMemberCollection = new HashMap<>();
    
    SerialNumber serialNumber = new SerialNumber();
    
    @BeforeEach
    public void setupTest() throws PWCGException
    {
        activeCrewMemberCollection.clear();
        inactiveCrewMemberCollection.clear();
        
        
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19170430"));
        Mockito.when(campaign.getPersonnelManager()).thenReturn(campaignPersonnelManager);
        Mockito.when(campaignPersonnelManager.getCompanyPersonnel(ArgumentMatchers.<Integer>any())).thenReturn(companyPersonnel);
        Mockito.when(companyPersonnel.getCrewMembersWithAces()).thenReturn(activeCrewMembers);
        Mockito.when(companyPersonnel.getRecentlyInactiveCrewMembers()).thenReturn(inactiveCrewMembers);
        Mockito.when(activeCrewMembers.getCrewMemberCollection()).thenReturn(activeCrewMemberCollection);
     }

    @Test
    public void testResupplyWithNoCrewMembers() throws PWCGException
    {
        CompanyPersonnelNeed companyTransferNeed = new CompanyPersonnelNeed(campaign, company);
        companyTransferNeed.determineResupplyNeeded();
        Assertions.assertTrue (companyTransferNeed.needsResupply() == true);
        
        for (int i = 0; i < Company.COMPANY_STAFF_SIZE - 1; ++i)
        {
            companyTransferNeed.noteResupply();
            Assertions.assertTrue (companyTransferNeed.needsResupply() == true);
        }

        companyTransferNeed.noteResupply();
        Assertions.assertTrue (companyTransferNeed.needsResupply() == false);
    }
    

    @Test
    public void testResupplyWithActiveCrewMembers() throws PWCGException
    {
        for (int i = 0; i < 9; ++i)
        {
            CrewMember crewMember = new CrewMember();
            crewMember.setSerialNumber(serialNumber.getNextCrewMemberSerialNumber());
            crewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_ACTIVE, null, null);
            activeCrewMemberCollection.put(crewMember.getSerialNumber(), crewMember);
        }
                
        CompanyPersonnelNeed companyResupplyNeed = new CompanyPersonnelNeed(campaign, company);
        companyResupplyNeed.determineResupplyNeeded();
        Assertions.assertTrue (companyResupplyNeed.needsResupply() == true);
        
        for (int i = 0; i < 2; ++i)
        {
            companyResupplyNeed.noteResupply();
            Assertions.assertTrue (companyResupplyNeed.needsResupply() == true);
        }

        companyResupplyNeed.noteResupply();
        Assertions.assertTrue (companyResupplyNeed.needsResupply() == false);
    }
    

    @Test
    public void testResupplyWithActiveAndInactiveCrewMembers() throws PWCGException
    {
        for (int i = 0; i < 7; ++i)
        {
            CrewMember crewMember = new CrewMember();
            crewMember.setSerialNumber(serialNumber.getNextCrewMemberSerialNumber());
            crewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_ACTIVE, null, null);
            activeCrewMemberCollection.put(crewMember.getSerialNumber(), crewMember);
        }
        
        for (int i = 0; i < 2; ++i)
        {
            CrewMember crewMember = new CrewMember();
            crewMember.setSerialNumber(serialNumber.getNextCrewMemberSerialNumber());
            crewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_KIA, campaign.getDate(), null);
            inactiveCrewMemberCollection.put(crewMember.getSerialNumber(), crewMember);
        }
        
        Mockito.when(inactiveCrewMembers.getActiveCount(campaign.getDate())).thenReturn(2);

        CompanyPersonnelNeed companyResupplyNeed = new CompanyPersonnelNeed(campaign, company);
        companyResupplyNeed.determineResupplyNeeded();
        Assertions.assertTrue (companyResupplyNeed.needsResupply() == true);
        
        for (int i = 0; i < 2; ++i)
        {
            companyResupplyNeed.noteResupply();
            Assertions.assertTrue (companyResupplyNeed.needsResupply() == true);
        }

        companyResupplyNeed.noteResupply();
        Assertions.assertTrue (companyResupplyNeed.needsResupply() == false);
    }

    @Test
    public void testNoResupplyNeeded() throws PWCGException
    {
        for (int i = 0; i < 10; ++i)
        {
            CrewMember crewMember = new CrewMember();
            crewMember.setSerialNumber(serialNumber.getNextCrewMemberSerialNumber());
            crewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_ACTIVE, null, null);
            activeCrewMemberCollection.put(crewMember.getSerialNumber(), crewMember);
        }
        
        for (int i = 0; i < 2; ++i)
        {
            CrewMember crewMember = new CrewMember();
            crewMember.setSerialNumber(serialNumber.getNextCrewMemberSerialNumber());
            crewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_KIA, campaign.getDate(), null);
            inactiveCrewMemberCollection.put(crewMember.getSerialNumber(), crewMember);
        }
        
        Mockito.when(inactiveCrewMembers.getActiveCount(campaign.getDate())).thenReturn(2);

        CompanyPersonnelNeed companyResupplyNeed = new CompanyPersonnelNeed(campaign, company);
        companyResupplyNeed.determineResupplyNeeded();
        Assertions.assertTrue (companyResupplyNeed.needsResupply() == false);
    }

}
