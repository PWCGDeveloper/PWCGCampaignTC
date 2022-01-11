package pwcg.aar.outofmission.phase3.tabulate.ui;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.aar.AARTestSetup;
import pwcg.aar.ui.events.AcesKilledEventGenerator;
import pwcg.aar.ui.events.CrewMemberStatusEventGenerator;
import pwcg.aar.ui.events.model.CrewMemberStatusEvent;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.crewmember.TankAce;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.MissionEntityBuilder;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class InMissionCompanyCrewMemberStatusEventGeneratorTest extends AARTestSetup
{	
    private Map<Integer, CrewMember> crewMembersKilledInMission = new HashMap<>();
    private Map<Integer, CrewMember> crewMembersCapturedInMission = new HashMap<>();
    private Map<Integer, CrewMember> crewMembersMaimedInMission = new HashMap<>();
    private Map<Integer, CrewMember> crewMembersWoundedInMission = new HashMap<>();
    private Map<Integer, CrewMember> acesKilledMissionCompanyInMission = new HashMap<>();
	
    @BeforeEach
    public void setupTest() throws PWCGException
    {
        crewMembersKilledInMission = new HashMap<>();
        crewMembersCapturedInMission = new HashMap<>();
        crewMembersMaimedInMission = new HashMap<>();
        crewMembersWoundedInMission = new HashMap<>();
        acesKilledMissionCompanyInMission = new HashMap<>();

        setupAARMocks();
        
        CrewMember squaddie1 = MissionEntityBuilder.makeCrewMemberWithStatus("Squaddie A", SerialNumber.AI_STARTING_SERIAL_NUMBER+1, CrewMemberStatus.STATUS_KIA, campaign.getDate(), null);
        CrewMember squaddie2 = MissionEntityBuilder.makeCrewMemberWithStatus("Squaddie B", SerialNumber.AI_STARTING_SERIAL_NUMBER+2, CrewMemberStatus.STATUS_KIA, campaign.getDate(), null);
        CrewMember squaddie3 = MissionEntityBuilder.makeCrewMemberWithStatus("Squaddie C", SerialNumber.AI_STARTING_SERIAL_NUMBER+3, CrewMemberStatus.STATUS_CAPTURED, campaign.getDate(), null);
        
        Date returnDate = DateUtils.advanceTimeDays(campaign.getDate(), 90);
        CrewMember squaddie4 = MissionEntityBuilder.makeCrewMemberWithStatus("Squaddie D", SerialNumber.AI_STARTING_SERIAL_NUMBER+4, CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED, campaign.getDate(), returnDate);

        crewMembersKilledInMission.put(squaddie1.getSerialNumber(), squaddie1);
        crewMembersKilledInMission.put(squaddie2.getSerialNumber(), squaddie2);
        crewMembersCapturedInMission.put(squaddie3.getSerialNumber(), squaddie3);
        crewMembersMaimedInMission.put(squaddie4.getSerialNumber(), squaddie4);

        TankAce ace1 = MissionEntityBuilder.makeDeadAceWithVictories("Ace A", SerialNumber.ACE_STARTING_SERIAL_NUMBER+1, AcesKilledEventGenerator.NUM_VICTORIES_FOR_ACE_TO_BE_NEWSWORTHY, campaign.getDate());
        crewMembersCapturedInMission.put(ace1.getSerialNumber(), ace1);
        
        Mockito.when(personnelLosses.getPersonnelKilled()).thenReturn(crewMembersKilledInMission);
        Mockito.when(personnelLosses.getPersonnelCaptured()).thenReturn(crewMembersCapturedInMission);
        Mockito.when(personnelLosses.getPersonnelMaimed()).thenReturn(crewMembersMaimedInMission);
        Mockito.when(personnelLosses.getPersonnelWounded()).thenReturn(crewMembersWoundedInMission);
        Mockito.when(personnelLosses.getAcesKilled(campaign)).thenReturn(acesKilledMissionCompanyInMission);
    }
    
	@Test
	public void testCrewMemberAndPlayerKilled() throws PWCGException
	{
        CrewMember player = MissionEntityBuilder.makeCrewMemberWithStatus("PLayer CrewMember", SerialNumber.PLAYER_STARTING_SERIAL_NUMBER+1, CrewMemberStatus.STATUS_KIA, campaign.getDate(), null);
        crewMembersKilledInMission.put(player.getSerialNumber(), player);

        CrewMemberStatusEventGenerator inMissionCompanyCrewMemberStatusEventGenerator = new CrewMemberStatusEventGenerator(campaign);
        Map<Integer, CrewMemberStatusEvent> crewMembersLostInMission = inMissionCompanyCrewMemberStatusEventGenerator.createCrewMemberLossEvents(personnelLosses);
        
        assert(crewMembersLostInMission.size() == 6);
	}
    
    @Test
    public void testCrewMemberAndPlayerMaimed() throws PWCGException
    {
        Date returnDate = DateUtils.advanceTimeDays(campaign.getDate(), 90);
        CrewMember player = MissionEntityBuilder.makeCrewMemberWithStatus("PLayer CrewMember", SerialNumber.PLAYER_STARTING_SERIAL_NUMBER+1, CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED, campaign.getDate(), returnDate);
        crewMembersMaimedInMission.put(player.getSerialNumber(), player);

        CrewMemberStatusEventGenerator inMissionCompanyCrewMemberStatusEventGenerator = new CrewMemberStatusEventGenerator(campaign);
        Map<Integer, CrewMemberStatusEvent> crewMembersLostInMission = inMissionCompanyCrewMemberStatusEventGenerator.createCrewMemberLossEvents(personnelLosses);
        
        assert(crewMembersLostInMission.size() == 6);
    }
    
    @Test
    public void testCrewMemberAndPlayerWounded() throws PWCGException
    {
        Date returnDate = DateUtils.advanceTimeDays(campaign.getDate(), 14);
        CrewMember player = MissionEntityBuilder.makeCrewMemberWithStatus("PLayer CrewMember", SerialNumber.PLAYER_STARTING_SERIAL_NUMBER+1, CrewMemberStatus.STATUS_WOUNDED, campaign.getDate(), returnDate);
        crewMembersWoundedInMission.put(player.getSerialNumber(), player);

        CrewMemberStatusEventGenerator inMissionCompanyCrewMemberStatusEventGenerator = new CrewMemberStatusEventGenerator(campaign);
        Map<Integer, CrewMemberStatusEvent> crewMembersLostInMission = inMissionCompanyCrewMemberStatusEventGenerator.createCrewMemberLossEvents(personnelLosses);
        
        assert(crewMembersLostInMission.size() == 5);
    }
    
	@Test
	public void testCrewMemberAndPlayerOK() throws PWCGException
	{
        CrewMemberStatusEventGenerator inMissionCompanyCrewMemberStatusEventGenerator = new CrewMemberStatusEventGenerator(campaign);
        Map<Integer, CrewMemberStatusEvent> crewMembersLostInMission = inMissionCompanyCrewMemberStatusEventGenerator.createCrewMemberLossEvents(personnelLosses);
        
        assert(crewMembersLostInMission.size() == 5);
	}
}
