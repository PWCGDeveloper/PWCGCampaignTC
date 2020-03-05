package pwcg.aar.outofmission.phase3.tabulate.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.AARTestSetup;
import pwcg.aar.data.AAREquipmentLosses;
import pwcg.aar.data.AARPersonnelLosses;
import pwcg.aar.inmission.phase3.reconcile.victories.ReconciledVictoryData;
import pwcg.aar.tabulate.combatreport.CombatReportTabulator;
import pwcg.aar.ui.display.model.AARCombatReportPanelData;
import pwcg.aar.ui.events.PilotStatusEventGenerator;
import pwcg.aar.ui.events.PlaneStatusEventGenerator;
import pwcg.aar.ui.events.VictoryEventGenerator;
import pwcg.aar.ui.events.model.ClaimDeniedEvent;
import pwcg.aar.ui.events.model.PilotStatusEvent;
import pwcg.aar.ui.events.model.PlaneStatusEvent;
import pwcg.aar.ui.events.model.VictoryEvent;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.PlaneStatus;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadmember.Victory;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.SquadronTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class CombatReportTabulatorTest extends AARTestSetup
{
    @Mock private ReconciledVictoryData reconciledVictoryData;
    @Mock private SquadronMembers campaignMembersInMission;
    @Mock private PilotStatusEventGenerator pilotStatusEventGenerator;
    @Mock private PlaneStatusEventGenerator planeStatusEventGenerator;
    @Mock private VictoryEventGenerator victoryEventGenerator;
    @Mock private Victory victory;

    private Map<Integer, SquadronMember> campaignMembersInMissionMap = new HashMap<>();

    @Before
    public void setupForTestEnvironment() throws PWCGException
    {
        setupAARMocks();
    }
    
    @Test
    public void combatReportTabulationTest () throws PWCGException
    {             
        List<SquadronMember> crews = new ArrayList<>();
        crews.add(pilot1);
        
        campaignMembersInMissionMap.put(pilot1.getSerialNumber(), pilot1);

        Mockito.when(missionHeader.getMissionFileName()).thenReturn("MissionFileName");
        Mockito.when(aarContext.getPreliminaryData()).thenReturn(preliminaryData);
        Mockito.when(preliminaryData.getPwcgMissionData()).thenReturn(pwcgMissionData);
        Mockito.when(preliminaryData.getCampaignMembersInMission()).thenReturn(campaignMembersInMission);
        Mockito.when(campaignMembersInMission.getSquadronMemberCollection()).thenReturn(campaignMembersInMissionMap);
        
        Mockito.when(pwcgMissionData.getMissionHeader()).thenReturn(missionHeader);
        
        boolean isNewsWorthy = false;
        ClaimDeniedEvent claimDenied = new ClaimDeniedEvent(campaign, "Any Plane", player.getSquadronId(), player.getSerialNumber(), campaign.getDate(), isNewsWorthy);

        List<ClaimDeniedEvent> claimsDenied = new ArrayList<>();
        claimsDenied.add(claimDenied);
        Mockito.when(reconciledVictoryData.getPlayerClaimsDenied()).thenReturn(claimsDenied);
        Mockito.when(reconciledInMissionData.getReconciledVictoryData()).thenReturn(reconciledVictoryData);
        Mockito.when(aarContext.getReconciledInMissionData()).thenReturn(reconciledInMissionData);

        List<VictoryEvent> victories = new ArrayList<>();
        isNewsWorthy = true;
        VictoryEvent victoryEvent = new VictoryEvent(campaign, victory, SquadronTestProfile.ESC_103_PROFILE.getSquadronId(), pilot1.getSerialNumber(), campaign.getDate(), isNewsWorthy);        
        victories.add(victoryEvent);
        VictoryEvent enemyVictoryEvent = new VictoryEvent(campaign, victory, SquadronTestProfile.JASTA_11_PROFILE.getSquadronId(), enemyPilot1.getSerialNumber(), campaign.getDate(), isNewsWorthy);        
        victories.add(enemyVictoryEvent);
        Mockito.when(victoryEventGenerator.createPilotVictoryEvents(Matchers.<Map<Integer, List<Victory>>>any())).thenReturn(victories);
                
        isNewsWorthy = true;
        PilotStatusEvent pilotStatusEvent = new PilotStatusEvent(campaign, SquadronMemberStatus.STATUS_KIA, pilot1.getSquadronId(), pilot1.getSerialNumber(), campaign.getDate(), isNewsWorthy);
        PilotStatusEvent enemyPilotStatusEvent = new PilotStatusEvent(campaign, SquadronMemberStatus.STATUS_KIA, enemyPilot1.getSquadronId(), enemyPilot1.getSerialNumber(), campaign.getDate(), isNewsWorthy);

        Map<Integer, PilotStatusEvent> pilotsLost = new HashMap<>();
        pilotsLost.put(pilot1.getSerialNumber(), pilotStatusEvent);
        pilotsLost.put(enemyPilot1.getSerialNumber(), enemyPilotStatusEvent);
        Mockito.when(pilotStatusEventGenerator.createPilotLossEvents(Matchers.<AARPersonnelLosses>any())).thenReturn(pilotsLost);

        boolean isNewsworthy = true;
        PlaneStatusEvent planeStatusEvent = new PlaneStatusEvent(plane1.getSerialNumber(), "SPAD XIII", plane1.getSquadronId(), PlaneStatus.STATUS_DESTROYED, 
                campaign.getDate(), isNewsworthy);
        PlaneStatusEvent enemyPlaneStatusEvent = new PlaneStatusEvent(enemyPlane1.getSerialNumber(), "Fokker DVII", enemyPlane1.getSquadronId(), PlaneStatus.STATUS_DESTROYED, 
                campaign.getDate(), isNewsworthy);

        Map<Integer, PlaneStatusEvent> planesLost = new HashMap<>();
        planesLost.put(plane1.getSerialNumber(), planeStatusEvent);
        planesLost.put(enemyPlane1.getSerialNumber(), enemyPlaneStatusEvent);
        Mockito.when(planeStatusEventGenerator.createPlaneLossEvents(Matchers.<AAREquipmentLosses>any())).thenReturn(planesLost);

        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
        CombatReportTabulator combatReportPanelEventTabulator = new CombatReportTabulator(campaign, squadron, aarContext);
        combatReportPanelEventTabulator.setPilotStatusEventGenerator(pilotStatusEventGenerator);
        combatReportPanelEventTabulator.setPlaneStatusEventGenerator(planeStatusEventGenerator);
        combatReportPanelEventTabulator.setVictoryEventGenerator(victoryEventGenerator);
        
        combatReportPanelEventTabulator.tabulateForAARCombatReportPanel();
        AARCombatReportPanelData combatReportPanelData = combatReportPanelEventTabulator.getCombatReportPanelData();
        AARCombatReportPanelData combatReportForAllUnitsData = combatReportPanelEventTabulator.getCombatResultsForAllUnitsData();

        assert (combatReportPanelData.getClaimsDenied().size() == 1);
        assert (combatReportPanelData.getCrewsInMission().size() == 1);
        assert (combatReportPanelData.getMissionHeader().getMissionFileName().equals("MissionFileName"));
        assert (combatReportPanelData.getSquadronMembersLostInMission().size() == 1);
        assert (combatReportPanelData.getSquadronPlanesLostInMission().size() == 1);
        assert (combatReportPanelData.getVictoriesForSquadronMembersInMission().size() == 1);

        assert (combatReportForAllUnitsData.getSquadronMembersLostInMission().size() == 2);
        assert (combatReportForAllUnitsData.getSquadronPlanesLostInMission().size() == 2);
        assert (combatReportForAllUnitsData.getVictoriesForSquadronMembersInMission().size() == 2);

    }

}
