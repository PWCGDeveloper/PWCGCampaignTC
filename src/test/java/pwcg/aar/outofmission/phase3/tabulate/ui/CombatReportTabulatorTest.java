package pwcg.aar.outofmission.phase3.tabulate.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import pwcg.aar.AARTestSetup;
import pwcg.aar.data.AARContextEventSequence;
import pwcg.aar.data.AAREquipmentLosses;
import pwcg.aar.data.AARPersonnelAcheivements;
import pwcg.aar.data.AARPersonnelLosses;
import pwcg.aar.inmission.phase2.logeval.AARMissionEvaluationData;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTank;
import pwcg.aar.tabulate.combatreport.AARCombatReportTabulator;
import pwcg.aar.ui.display.model.AARCombatReportPanelData;
import pwcg.aar.ui.events.CrewMemberStatusEventGenerator;
import pwcg.aar.ui.events.TankStatusEventGenerator;
import pwcg.aar.ui.events.VictoryEventGenerator;
import pwcg.aar.ui.events.model.ClaimDeniedEvent;
import pwcg.aar.ui.events.model.CrewMemberStatusEvent;
import pwcg.aar.ui.events.model.TankStatusEvent;
import pwcg.aar.ui.events.model.VictoryEvent;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.crewmember.Victory;
import pwcg.campaign.tank.TankStatus;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CompanyTestProfile;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CombatReportTabulatorTest extends AARTestSetup
{
    @Mock private CrewMembers campaignMembersInMission;
    @Mock private CrewMemberStatusEventGenerator crewMemberStatusEventGenerator;
    @Mock private TankStatusEventGenerator planeStatusEventGenerator;
    @Mock private VictoryEventGenerator victoryEventGenerator;
    @Mock private AARMissionEvaluationData missionEvaluationData;
    @Mock private AARPersonnelAcheivements personnelAcheivements;
    @Mock private Victory victory;

    private Map<Integer, CrewMember> campaignMembersInMissionMap = new HashMap<>();

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        setupAARMocks();
    }
    
    @Test
    public void combatReportTabulationTest () throws PWCGException
    {             
        List<CrewMember> crews = new ArrayList<>();
        crews.add(crewMember1);
        
        campaignMembersInMissionMap.put(crewMember1.getSerialNumber(), crewMember1);

        Mockito.when(missionHeader.getMissionFileName()).thenReturn("MissionFileName");
        Mockito.when(aarContext.getPreliminaryData()).thenReturn(preliminaryData);
        Mockito.when(aarContext.getMissionEvaluationData()).thenReturn(missionEvaluationData);
        Mockito.when(aarContext.getPersonnelAcheivements()).thenReturn(personnelAcheivements);
        Mockito.when(preliminaryData.getPwcgMissionData()).thenReturn(pwcgMissionData);
        Mockito.when(preliminaryData.getCampaignMembersInMission()).thenReturn(campaignMembersInMission);
        Mockito.when(campaignMembersInMission.getCrewMemberCollection()).thenReturn(campaignMembersInMissionMap);
        Mockito.when(missionEvaluationData.wasCrewMemberInMission(Mockito.anyInt())).thenReturn(true);
        
        Mockito.when(pwcgMissionData.getMissionHeader()).thenReturn(missionHeader);
        
        boolean isNewsWorthy = false;
        ClaimDeniedEvent claimDenied = new ClaimDeniedEvent(campaign, "Any Plane", player.getCompanyId(), player.getSerialNumber(), campaign.getDate(), isNewsWorthy);

        List<ClaimDeniedEvent> claimsDenied = new ArrayList<>();
        claimsDenied.add(claimDenied);
        Mockito.when(aarContext.getPersonnelAcheivements().getPlayerClaimsDenied()).thenReturn(claimsDenied);

        isNewsWorthy = true;
        List<VictoryEvent> victories = new ArrayList<>();
        VictoryEvent victoryEvent = new VictoryEvent(campaign, victory, CompanyTestProfile.THIRD_DIVISION_PROFILE.getCompanyId(), crewMember1.getSerialNumber(), campaign.getDate(), isNewsWorthy);        
        victories.add(victoryEvent);
        VictoryEvent victoryEventNotFromCompany = new VictoryEvent(campaign, victory, CompanyTestProfile.TANK_DIVISION_147_PROFILE.getCompanyId(), crewMember2.getSerialNumber(), campaign.getDate(), isNewsWorthy);        
        victories.add(victoryEventNotFromCompany);
        Mockito.when(victoryEventGenerator.createCrewMemberVictoryEvents(ArgumentMatchers.<Map<Integer, List<Victory>>>any())).thenReturn(victories);
                
        isNewsWorthy = true;
        CrewMemberStatusEvent crewMemberStatusEvent = new CrewMemberStatusEvent(campaign, CrewMemberStatus.STATUS_KIA, CompanyTestProfile.THIRD_DIVISION_PROFILE.getCompanyId(), crewMember1.getSerialNumber(), campaign.getDate(), isNewsWorthy);
        CrewMemberStatusEvent crewMemberStatusEventNotFromCompany = new CrewMemberStatusEvent(campaign, CrewMemberStatus.STATUS_KIA, CompanyTestProfile.TANK_DIVISION_147_PROFILE.getCompanyId(), crewMember2.getSerialNumber(), campaign.getDate(), isNewsWorthy);
        Map<Integer, CrewMemberStatusEvent> crewMembersLost = new HashMap<>();
        crewMembersLost.put(crewMember1.getSerialNumber(), crewMemberStatusEvent);
        crewMembersLost.put(crewMember2.getSerialNumber(), crewMemberStatusEventNotFromCompany);
        Mockito.when(crewMemberStatusEventGenerator.createCrewMemberLossEvents(ArgumentMatchers.<AARPersonnelLosses>any())).thenReturn(crewMembersLost);

        boolean isNewsworthy = true;
        LogTank logPlane = new LogTank(AARContextEventSequence.getNextOutOfMissionEventSequenceNumber());
        logPlane.mapToEquippedTankForTest(campaign, plane1, crewMember1);
        TankStatusEvent planeStatusEvent = new TankStatusEvent(campaign, logPlane, TankStatus.STATUS_DESTROYED, isNewsworthy);

        LogTank logPlaneNotFromCompany = new LogTank(AARContextEventSequence.getNextOutOfMissionEventSequenceNumber());
        logPlaneNotFromCompany.mapToEquippedTankForTest(campaign, plane2, crewMember2);
        TankStatusEvent planeStatusEventNotFromCompany = new TankStatusEvent(campaign, logPlane, TankStatus.STATUS_DESTROYED, isNewsworthy);

        Map<Integer, TankStatusEvent> planesLost = new HashMap<>();
        planesLost.put(plane1.getSerialNumber(), planeStatusEvent);
        planesLost.put(plane2.getSerialNumber(), planeStatusEventNotFromCompany);
        Mockito.when(planeStatusEventGenerator.createTankLossEvents(ArgumentMatchers.<AAREquipmentLosses>any())).thenReturn(planesLost);

        Company company = PWCGContext.getInstance().getCompanyManager().getCompany(CompanyTestProfile.THIRD_DIVISION_PROFILE.getCompanyId());
        AARCombatReportTabulator combatReportPanelEventTabulator = new AARCombatReportTabulator(campaign, company, aarContext);
        combatReportPanelEventTabulator.setCrewMemberStatusEventGenerator(crewMemberStatusEventGenerator);
        combatReportPanelEventTabulator.setTankStatusEventGenerator(planeStatusEventGenerator);
        combatReportPanelEventTabulator.setVictoryEventGenerator(victoryEventGenerator);
        AARCombatReportPanelData combatReportPanelData = combatReportPanelEventTabulator.tabulateForAARCombatReportPanel();

        Assertions.assertTrue (combatReportPanelData.getClaimsDenied().size() == 1);
        Assertions.assertTrue (combatReportPanelData.getCrewsInMission().size() == 1);
        Assertions.assertTrue (combatReportPanelData.getMissionHeader().getMissionFileName().equals("MissionFileName"));
        Assertions.assertTrue (combatReportPanelData.getCrewMembersLostInMission().size() == 1);
        Assertions.assertTrue (combatReportPanelData.getCompanyTanksLostInMission().size() == 1);
        Assertions.assertTrue (combatReportPanelData.getVictoriesForCrewMembersInMission().size() == 1);

    }

}
