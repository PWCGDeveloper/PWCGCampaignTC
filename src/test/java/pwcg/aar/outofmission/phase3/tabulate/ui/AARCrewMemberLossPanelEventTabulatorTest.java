package pwcg.aar.outofmission.phase3.tabulate.ui;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.aar.AARTestSetup;
import pwcg.aar.data.AARContext;
import pwcg.aar.tabulate.debrief.CrewMemberLossPanelEventTabulator;
import pwcg.aar.ui.display.model.AARCrewMemberLossPanelData;
import pwcg.aar.ui.events.AcesKilledEventGenerator;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.crewmember.TankAce;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.MissionEntityBuilder;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AARCrewMemberLossPanelEventTabulatorTest extends AARTestSetup
{
    private Map<Integer, CrewMember> crewMembersKilled = new HashMap<>();
    private Map<Integer, CrewMember> crewMembersCaptured = new HashMap<>();
    private Map<Integer, CrewMember> crewMembersMaimed = new HashMap<>();
    private Map<Integer, CrewMember> acesKilledMissionCompany = new HashMap<>();
    
    @Mock
    private AARContext aarContext;

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        setupAARMocks();
        
        crewMembersKilled.clear();
        crewMembersCaptured.clear();
        crewMembersMaimed.clear();
        acesKilledMissionCompany.clear();
 
        Mockito.when(personnelLosses.getPersonnelKilled()).thenReturn(crewMembersKilled);
        Mockito.when(personnelLosses.getPersonnelCaptured()).thenReturn(crewMembersCaptured);
        Mockito.when(personnelLosses.getPersonnelMaimed()).thenReturn(crewMembersMaimed);
        Mockito.when(personnelLosses.getAcesKilled(campaign)).thenReturn(acesKilledMissionCompany);
        
        Mockito.when(aarContext.getPersonnelLosses()).thenReturn(personnelLosses);
    }

    @Test
    public void testMergedCrewMembersLost() throws PWCGException 
    {
        CrewMember squaddie1 = MissionEntityBuilder.makeCrewMemberWithStatus("Squaddie A", SerialNumber.AI_STARTING_SERIAL_NUMBER+1, CrewMemberStatus.STATUS_KIA, campaign.getDate(), null);
        CrewMember squaddie2 = MissionEntityBuilder.makeCrewMemberWithStatus("Squaddie B", SerialNumber.AI_STARTING_SERIAL_NUMBER+2, CrewMemberStatus.STATUS_CAPTURED, campaign.getDate(), null);
        
        Date returnDate = DateUtils.advanceTimeDays(campaign.getDate(), 90);
        CrewMember squaddie3 = MissionEntityBuilder.makeCrewMemberWithStatus("Squaddie C", SerialNumber.AI_STARTING_SERIAL_NUMBER+3, CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED, campaign.getDate(), returnDate);
        TankAce ace1 = MissionEntityBuilder.makeDeadAceWithVictories("Ace A", SerialNumber.ACE_STARTING_SERIAL_NUMBER+1, AcesKilledEventGenerator.NUM_VICTORIES_FOR_ACE_TO_BE_NEWSWORTHY, campaign.getDate());

        crewMembersKilled.put(squaddie1.getSerialNumber(), squaddie1);
        crewMembersCaptured.put(squaddie2.getSerialNumber(), squaddie2);
        crewMembersMaimed.put(squaddie3.getSerialNumber(), squaddie3);
        acesKilledMissionCompany.put(ace1.getSerialNumber(), ace1);

        CrewMember squaddie4 = MissionEntityBuilder.makeCrewMemberWithStatus("Squaddie D", SerialNumber.AI_STARTING_SERIAL_NUMBER+4, CrewMemberStatus.STATUS_KIA, campaign.getDate(), null);
        CrewMember squaddie5 = MissionEntityBuilder.makeCrewMemberWithStatus("Squaddie E", SerialNumber.AI_STARTING_SERIAL_NUMBER+5, CrewMemberStatus.STATUS_CAPTURED, campaign.getDate(), null);        
        CrewMember squaddie6 = MissionEntityBuilder.makeCrewMemberWithStatus("Squaddie F", SerialNumber.AI_STARTING_SERIAL_NUMBER+6, CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED, campaign.getDate(), returnDate);
        TankAce ace2 = MissionEntityBuilder.makeDeadAceWithVictories("Ace B", SerialNumber.ACE_STARTING_SERIAL_NUMBER+2, AcesKilledEventGenerator.NUM_VICTORIES_FOR_ACE_TO_BE_NEWSWORTHY, campaign.getDate());

        crewMembersKilled.put(squaddie4.getSerialNumber(), squaddie4);
        crewMembersCaptured.put(squaddie5.getSerialNumber(), squaddie5);
        crewMembersMaimed.put(squaddie6.getSerialNumber(), squaddie6);
        acesKilledMissionCompany.put(ace2.getSerialNumber(), ace2);
        
        CrewMemberLossPanelEventTabulator crewMemberLossPanelEventTabulator = new CrewMemberLossPanelEventTabulator(campaign, aarContext);
        AARCrewMemberLossPanelData crewMemberLossPanelData = crewMemberLossPanelEventTabulator.tabulateForAARCrewMemberLossPanel();
        
        assert(crewMemberLossPanelData.getSquadMembersLost().size() == 8);
    }
}
