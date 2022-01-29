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
import pwcg.campaign.company.Company;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.crewmember.TankAce;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CompanyTestProfile;

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

    private static Map <String, LogTank> tankAiEntities = new HashMap <>();
    
    public AARCrewBuilderTest() throws PWCGException
    {        
        

        tankAiEntities = new HashMap <>();
        addTank(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        addTank(SerialNumber.ACE_STARTING_SERIAL_NUMBER+1);
        addTank(SerialNumber.ACE_STARTING_SERIAL_NUMBER+2);
        addTank(SerialNumber.AI_STARTING_SERIAL_NUMBER+1);
        addTank(SerialNumber.AI_STARTING_SERIAL_NUMBER+2);
    }

    private static void addTank(Integer crewMemberSerialNumber)
    {
        LogCrewMember crewMember = new LogCrewMember();
        crewMember.setSerialNumber(crewMemberSerialNumber);
        
        LogTank tank = new LogTank(1);
        tank.setCrewMemberSerialNumber(crewMemberSerialNumber);
        
        String tankId = crewMemberSerialNumber.toString();
        tankAiEntities.put(tankId, tank);
    }

    @Test
    public void testCrewMembers () throws PWCGException
    {        
        for (LogTank tank : tankAiEntities.values())
        {
            tank.setCompanyId(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId());
        }
        
        AARCrewBuilder crewBuilder = new AARCrewBuilder(tankAiEntities);
        List<LogCrewMember> inSquad = crewBuilder.buildCrewMembersFromLogTanks();
        assert(crewMemberIsInList(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, inSquad) == true);
        assert(crewMemberIsInList(SerialNumber.ACE_STARTING_SERIAL_NUMBER+1, inSquad) == true);
        assert(crewMemberIsInList(SerialNumber.ACE_STARTING_SERIAL_NUMBER+2, inSquad) == true);
        assert(crewMemberIsInList(SerialNumber.AI_STARTING_SERIAL_NUMBER+1, inSquad) == true);
        assert(crewMemberIsInList(SerialNumber.AI_STARTING_SERIAL_NUMBER+2, inSquad) == true);
    }

    @Test
    public void testCrewMembersNotInCompany () throws PWCGException
    {        
        for (LogTank tank : tankAiEntities.values())
        {
            tank.setCompanyId(Company.AI);
        }
        

        AARCrewBuilder crewBuilder = new AARCrewBuilder(tankAiEntities);
        List<LogCrewMember> inSquad = crewBuilder.buildCrewMembersFromLogTanks();
        assert(crewMemberIsInList(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, inSquad) == false);
        assert(crewMemberIsInList(SerialNumber.ACE_STARTING_SERIAL_NUMBER+1, inSquad) == false);
        assert(crewMemberIsInList(SerialNumber.ACE_STARTING_SERIAL_NUMBER+2, inSquad) == false);
        assert(crewMemberIsInList(SerialNumber.AI_STARTING_SERIAL_NUMBER+1, inSquad) == false);
        assert(crewMemberIsInList(SerialNumber.AI_STARTING_SERIAL_NUMBER+2, inSquad) == false);
    }

    @Test
    public void testAcesMembers () throws PWCGException
    {        
        AARCrewBuilder crewBuilder = new AARCrewBuilder(tankAiEntities);
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
