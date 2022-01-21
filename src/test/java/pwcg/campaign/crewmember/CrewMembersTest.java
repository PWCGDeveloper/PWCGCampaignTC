package pwcg.campaign.crewmember;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CrewMembersTest
{
    @Mock
    private Campaign campaign;
    
    @Mock
    private CrewMember companyMember1;
    
    @Mock
    private CrewMember companyMember2;
    
    @Mock
    private CrewMember companyMember3;
    
    @Mock
    private CrewMember companyMember4;
    
    private  CrewMembers crewMembers;
    
    @BeforeEach
    public void setupTest() throws PWCGException 
    {
        
        
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420801"));

        Mockito.when(companyMember1.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1);
        Mockito.when(companyMember2.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER + 2);
        Mockito.when(companyMember3.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER + 3);
        Mockito.when(companyMember4.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER + 4);
        
        Mockito.when(companyMember1.getName()).thenReturn("John Bonham");
        Mockito.when(companyMember3.getName()).thenReturn("Jimmy Page");
        Mockito.when(companyMember4.getName()).thenReturn("Robert Plant");

        crewMembers = new CrewMembers();
        crewMembers.addToCrewMemberCollection(companyMember1);
        crewMembers.addToCrewMemberCollection(companyMember2);
    }

    @Test
    public void addCrewMember() throws PWCGException 
    {
        crewMembers.addToCrewMemberCollection(companyMember3);
        assert(crewMembers.getActiveCount(campaign.getDate()) == 3);
    }

    @Test
    public void removeAnyCrewMember() throws PWCGException 
    {
        crewMembers.addToCrewMemberCollection(companyMember3);
        crewMembers.addToCrewMemberCollection(companyMember4);
        CrewMember crewMember = crewMembers.findCrewMember();
        crewMembers.removeCrewMember(crewMember.getSerialNumber());
        assert(crewMembers.getActiveCount(campaign.getDate()) == 3);
    }

    @Test
    public void removeCrewMember() throws PWCGException 
    {
        crewMembers.addToCrewMemberCollection(companyMember3);
        crewMembers.addToCrewMemberCollection(companyMember4);
        CrewMember crewMember = crewMembers.removeCrewMember(SerialNumber.AI_STARTING_SERIAL_NUMBER + 3);
        assert(crewMembers.getActiveCount(campaign.getDate()) == 3);
        assert(crewMember.getSerialNumber() == (SerialNumber.AI_STARTING_SERIAL_NUMBER + 3));
    }

    @Test
    public void getCrewMemberByName() throws PWCGException 
    {
        crewMembers.addToCrewMemberCollection(companyMember3);
        crewMembers.addToCrewMemberCollection(companyMember4);
        CrewMember crewMember = crewMembers.getCrewMemberByName("Jimmy Page");
        assert(crewMembers.getActiveCount(campaign.getDate()) == 4);
        assert(crewMember.getSerialNumber() == (SerialNumber.AI_STARTING_SERIAL_NUMBER + 3));
    }

}
