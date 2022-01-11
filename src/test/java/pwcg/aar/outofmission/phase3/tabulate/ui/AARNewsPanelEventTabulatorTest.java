package pwcg.aar.outofmission.phase3.tabulate.ui;

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
import pwcg.aar.tabulate.debrief.NewsPanelEventTabulator;
import pwcg.aar.ui.display.model.AARNewsPanelData;
import pwcg.aar.ui.events.AcesKilledEventGenerator;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.crewmember.TankAce;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.MissionEntityBuilder;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AARNewsPanelEventTabulatorTest extends AARTestSetup
{
    private Map<Integer, CrewMember> crewMembersKilled = new HashMap<>();
    private Map<Integer, CrewMember> crewMembersCaptured = new HashMap<>();
    private Map<Integer, CrewMember> crewMembersMaimed = new HashMap<>();
    private Map<Integer, CrewMember> acesKilledMissionCompany = new HashMap<>();

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
    }

    @Test
    public void testAcesKilled() throws PWCGException 
    {
        TankAce ace1 = MissionEntityBuilder.makeDeadAceWithVictories("Ace A", SerialNumber.ACE_STARTING_SERIAL_NUMBER+1, AcesKilledEventGenerator.NUM_VICTORIES_FOR_ACE_TO_BE_NEWSWORTHY, campaign.getDate());
        TankAce ace2 = MissionEntityBuilder.makeDeadAceWithVictories("Ace B", SerialNumber.ACE_STARTING_SERIAL_NUMBER+2, AcesKilledEventGenerator.NUM_VICTORIES_FOR_ACE_TO_BE_NEWSWORTHY, campaign.getDate());
        TankAce ace3 = MissionEntityBuilder.makeDeadAceWithVictories("Ace C", SerialNumber.ACE_STARTING_SERIAL_NUMBER+3, AcesKilledEventGenerator.NUM_VICTORIES_FOR_ACE_TO_BE_NEWSWORTHY, campaign.getDate());
        TankAce ace4 = MissionEntityBuilder.makeDeadAceWithVictories("Ace D", SerialNumber.ACE_STARTING_SERIAL_NUMBER+4, AcesKilledEventGenerator.NUM_VICTORIES_FOR_ACE_TO_BE_NEWSWORTHY, campaign.getDate());
        
        crewMembersKilled.put(ace1.getSerialNumber(), ace1);
        crewMembersCaptured.put(ace2.getSerialNumber(), ace2);
        acesKilledMissionCompany.put(ace3.getSerialNumber(), ace3);
        acesKilledMissionCompany.put(ace4.getSerialNumber(), ace4);
        
        NewsPanelEventTabulator newsPanelEventTabulator = new NewsPanelEventTabulator(campaign, aarContext);
        AARNewsPanelData newsPanelData = newsPanelEventTabulator.createNewspaperEvents();
        
        assert(newsPanelData.getAcesKilledDuringElapsedTime().size() == 2);
    }

    @Test
    public void testMergedAcesKilledOneNotNewsworthy() throws PWCGException 
    {
        TankAce ace1 = MissionEntityBuilder.makeDeadAceWithVictories("Ace A", SerialNumber.ACE_STARTING_SERIAL_NUMBER+1, AcesKilledEventGenerator.NUM_VICTORIES_FOR_ACE_TO_BE_NEWSWORTHY, campaign.getDate());
        TankAce ace2 = MissionEntityBuilder.makeDeadAceWithVictories("Ace B", SerialNumber.ACE_STARTING_SERIAL_NUMBER+2, AcesKilledEventGenerator.NUM_VICTORIES_FOR_ACE_TO_BE_NEWSWORTHY, campaign.getDate());
        TankAce ace3 = MissionEntityBuilder.makeDeadAceWithVictories("Ace C", SerialNumber.ACE_STARTING_SERIAL_NUMBER+3, AcesKilledEventGenerator.NUM_VICTORIES_FOR_ACE_TO_BE_NEWSWORTHY-1, campaign.getDate());
        TankAce ace4 = MissionEntityBuilder.makeDeadAceWithVictories("Ace D", SerialNumber.ACE_STARTING_SERIAL_NUMBER+4, AcesKilledEventGenerator.NUM_VICTORIES_FOR_ACE_TO_BE_NEWSWORTHY, campaign.getDate());
        
        crewMembersKilled.put(ace1.getSerialNumber(), ace1);
        crewMembersCaptured.put(ace2.getSerialNumber(), ace2);
        acesKilledMissionCompany.put(ace3.getSerialNumber(), ace3);
        acesKilledMissionCompany.put(ace4.getSerialNumber(), ace4);

        NewsPanelEventTabulator newsPanelEventTabulator = new NewsPanelEventTabulator(campaign, aarContext);
        AARNewsPanelData newsPanelData = newsPanelEventTabulator.createNewspaperEvents();
        
        assert(newsPanelData.getAcesKilledDuringElapsedTime().size() == 1);
    }

}
