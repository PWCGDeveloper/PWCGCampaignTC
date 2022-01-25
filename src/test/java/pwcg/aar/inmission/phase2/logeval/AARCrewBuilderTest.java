package pwcg.aar.inmission.phase2.logeval;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogCrewMember;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTank;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.crewmember.TankAce;
import pwcg.core.exception.PWCGException;

@ExtendWith(MockitoExtension.class)
public class AARCrewBuilderTest
{
    @Mock
    Campaign campaign;
    
    @Mock
    private CampaignPersonnelManager personnelManager;

    @Mock
    private TankAce aceInMission1;
    
    @Mock
    private TankAce aceInMission2;

    @Mock
    private CrewMember player;

    @Mock
    private CrewMember aiInCompany;

    @Mock
    private CrewMember aiNotInCompany;

    private static Map <String, LogTank> planeAiEntities = new HashMap <>();
    
    public AARCrewBuilderTest() throws PWCGException
    {        
        

        planeAiEntities = new HashMap <>();
        addPlane(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        addPlane(SerialNumber.ACE_STARTING_SERIAL_NUMBER+1);
        addPlane(SerialNumber.ACE_STARTING_SERIAL_NUMBER+2);
        addPlane(SerialNumber.AI_STARTING_SERIAL_NUMBER+1);
        addPlane(SerialNumber.AI_STARTING_SERIAL_NUMBER+2);
    }

    private static void addPlane(Integer crewMemberSerialNumber)
    {
        LogCrewMember crewMember = new LogCrewMember();
        crewMember.setSerialNumber(crewMemberSerialNumber);
        
        LogTank plane1 = new LogTank(1);
        plane1.setCrewMemberSerialNumber(crewMemberSerialNumber);
        plane1.intializeCrewMember(crewMemberSerialNumber);
        
        String planeId = crewMemberSerialNumber.toString();
        planeAiEntities.put(planeId, plane1);
    }

    @Test
    public void testCrewMembers () throws PWCGException
    {        
        AARCrewBuilder crewBuilder = new AARCrewBuilder(planeAiEntities);
        List<LogCrewMember> inSquad = crewBuilder.buildCrewMembersFromLogTanks();
        assert(crewMemberIsInList(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, inSquad) == true);
        assert(crewMemberIsInList(SerialNumber.ACE_STARTING_SERIAL_NUMBER+1, inSquad) == true);
        assert(crewMemberIsInList(SerialNumber.ACE_STARTING_SERIAL_NUMBER+2, inSquad) == true);
        assert(crewMemberIsInList(SerialNumber.AI_STARTING_SERIAL_NUMBER+1, inSquad) == true);
        assert(crewMemberIsInList(SerialNumber.AI_STARTING_SERIAL_NUMBER+2, inSquad) == true);
    }

    @Test
    public void testAcesMembers () throws PWCGException
    {        
        AARCrewBuilder crewBuilder = new AARCrewBuilder(planeAiEntities);
        List<LogCrewMember> aces = crewBuilder.buildAcesFromLogTanks();
        assert(crewMemberIsInList(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, aces) == false);
        assert(crewMemberIsInList(SerialNumber.ACE_STARTING_SERIAL_NUMBER+1, aces) == true);
        assert(crewMemberIsInList(SerialNumber.ACE_STARTING_SERIAL_NUMBER+2, aces) == true);
        assert(crewMemberIsInList(SerialNumber.AI_STARTING_SERIAL_NUMBER+1, aces) == false);
        assert(crewMemberIsInList(SerialNumber.AI_STARTING_SERIAL_NUMBER+2, aces) == false);
    }
    
    public boolean crewMemberIsInList(Integer serialNumber, List<LogCrewMember> crewMemberList)
    {
        for (LogCrewMember crewMember : crewMemberList)
        {
            if (crewMember.getSerialNumber() == serialNumber)
            {
                return true;
            }
        }
        
        return false;
     }

}
