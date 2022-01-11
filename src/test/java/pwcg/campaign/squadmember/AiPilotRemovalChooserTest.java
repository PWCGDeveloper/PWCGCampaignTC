package pwcg.campaign.squadmember;

import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.AiCrewMemberRemovalChooser;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AiCrewMemberRemovalChooserTest
{
    @Mock private Campaign campaign;
    @Mock private CampaignPersonnelManager campaignPersonnelManager;
    @Mock private CompanyPersonnel companyPersonnel;    
    @Mock private Company company;
    @Mock private CrewMember companyMember1;
    @Mock private CrewMember companyMember2;
    @Mock private CrewMember companyMember3;
    @Mock private CrewMember companyMember4;
    @Mock private CrewMember companyMember5;
    @Mock private CrewMember companyMember6;
    
    private Date campaignDate;

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        
        campaignDate = DateUtils.getDateYYYYMMDD("19420801");
        Mockito.when(campaign.getDate()).thenReturn(campaignDate);
        Mockito.when(campaign.getPersonnelManager()).thenReturn(campaignPersonnelManager);
        Mockito.when(campaign.getPersonnelManager()).thenReturn(campaignPersonnelManager);
        Mockito.when(campaignPersonnelManager.getCompanyPersonnel(ArgumentMatchers.anyInt())).thenReturn(companyPersonnel);

        company = PWCGContext.getInstance().getCompanyManager().getCompany(10131132); 
        
        Mockito.when(companyMember1.getRank()).thenReturn("Major");
        Mockito.when(companyMember1.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER+1);
        
        Mockito.when(companyMember2.getRank()).thenReturn("Kapitan");
        Mockito.when(companyMember2.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER+2);

        Mockito.when(companyMember3.getRank()).thenReturn("Starshyi leyitenant");
        Mockito.when(companyMember3.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER+3);

        Mockito.when(companyMember4.getRank()).thenReturn("Leyitenant");
        Mockito.when(companyMember4.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER+4);

        Mockito.when(companyMember5.getRank()).thenReturn("Leyitenant");
        Mockito.when(companyMember5.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER+5);

        Mockito.when(companyMember6.getRank()).thenReturn("Serzhant");
    }


    @Test
    public void testRemoveSameRank() throws PWCGException
    {
        Mockito.when(companyMember6.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER+6);
        
        CrewMembers crewMembers = new CrewMembers();
        Mockito.when(companyPersonnel.getCrewMembers()).thenReturn(crewMembers);
        crewMembers.addToCrewMemberCollection(companyMember1);
        crewMembers.addToCrewMemberCollection(companyMember2);
        crewMembers.addToCrewMemberCollection(companyMember3);
        crewMembers.addToCrewMemberCollection(companyMember4);
        crewMembers.addToCrewMemberCollection(companyMember5);
        crewMembers.addToCrewMemberCollection(companyMember6);

        
        AiCrewMemberRemovalChooser chooser = new AiCrewMemberRemovalChooser(campaign);
        CrewMember companyMemberRemoved = chooser.findAiCrewMemberToRemove("Leyitenant", 10131132);
        Assertions.assertTrue (companyMemberRemoved.getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+4 || 
                companyMemberRemoved.getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+5);
    }

    @Test
    public void testRemoveSimilarRank() throws PWCGException
    {
        Mockito.when(companyMember6.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER+6);
        
        CrewMembers crewMembers = new CrewMembers();
        Mockito.when(companyPersonnel.getCrewMembers()).thenReturn(crewMembers);
        crewMembers.addToCrewMemberCollection(companyMember1);
        crewMembers.addToCrewMemberCollection(companyMember2);
        crewMembers.addToCrewMemberCollection(companyMember3);
        crewMembers.addToCrewMemberCollection(companyMember6);

        
        AiCrewMemberRemovalChooser chooser = new AiCrewMemberRemovalChooser(campaign);
        CrewMember companyMemberRemoved = chooser.findAiCrewMemberToRemove("Leyitenant", 10131132);
        Assertions.assertTrue (companyMemberRemoved.getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+3 || 
                companyMemberRemoved.getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+6);
    }

    @Test
    public void testRemoveAnyNonCommandRank() throws PWCGException
    {
        CrewMembers crewMembers = new CrewMembers();
        Mockito.when(companyPersonnel.getCrewMembers()).thenReturn(crewMembers);
        crewMembers.addToCrewMemberCollection(companyMember1);
        crewMembers.addToCrewMemberCollection(companyMember2);

        
        AiCrewMemberRemovalChooser chooser = new AiCrewMemberRemovalChooser(campaign);
        CrewMember companyMemberRemoved = chooser.findAiCrewMemberToRemove("Leyitenant", 10131132);
        Assertions.assertTrue (companyMemberRemoved.getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+2);
    }

    @Test
    public void testCommanderRemoved() throws PWCGException
    {
        CrewMembers crewMembers = new CrewMembers();
        Mockito.when(companyPersonnel.getCrewMembers()).thenReturn(crewMembers);
        crewMembers.addToCrewMemberCollection(companyMember1);
        
        AiCrewMemberRemovalChooser chooser = new AiCrewMemberRemovalChooser(campaign);
        CrewMember companyMemberRemoved = chooser.findAiCrewMemberToRemove("Major", 10131132);
        Assertions.assertTrue (companyMemberRemoved.getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+1);
    }

    @Test
    public void testNobodyRemoved() throws PWCGException
    {
        CrewMembers crewMembers = new CrewMembers();
        Mockito.when(companyPersonnel.getCrewMembers()).thenReturn(crewMembers);
        crewMembers.addToCrewMemberCollection(companyMember1);
        
        AiCrewMemberRemovalChooser chooser = new AiCrewMemberRemovalChooser(campaign);
        CrewMember companyMemberRemoved = chooser.findAiCrewMemberToRemove("Leyitenant", 10131132);
        Assertions.assertTrue (companyMemberRemoved == null);
    }
}
