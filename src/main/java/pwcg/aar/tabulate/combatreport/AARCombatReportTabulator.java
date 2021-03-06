package pwcg.aar.tabulate.combatreport;

import java.util.List;
import java.util.Map;

import pwcg.aar.data.AARContext;
import pwcg.aar.ui.display.model.AARCombatReportPanelData;
import pwcg.aar.ui.events.CrewMemberStatusEventGenerator;
import pwcg.aar.ui.events.TankStatusEventGenerator;
import pwcg.aar.ui.events.VictoryEventGenerator;
import pwcg.aar.ui.events.model.CrewMemberStatusEvent;
import pwcg.aar.ui.events.model.TankStatusEvent;
import pwcg.aar.ui.events.model.VictoryEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.crewmember.Victory;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.core.exception.PWCGException;

public class AARCombatReportTabulator 
{
    private Campaign campaign;
    private Company company;
    private AARContext aarContext;
    
    private CrewMemberStatusEventGenerator crewMemberStatusEventGenerator;
    private TankStatusEventGenerator tankStatusEventGenerator;
    private VictoryEventGenerator victoryEventGenerator;
    private AARCombatReportPanelData combatReportPanelData = new AARCombatReportPanelData();
    
    public AARCombatReportTabulator (Campaign campaign, Company company, AARContext aarContext)
    {
        this.aarContext = aarContext;
        this.campaign = campaign;
        this.company = company;
        
        crewMemberStatusEventGenerator = new CrewMemberStatusEventGenerator(campaign);
        tankStatusEventGenerator = new TankStatusEventGenerator(campaign);
        victoryEventGenerator = new VictoryEventGenerator(campaign);
    }
        
    public AARCombatReportPanelData tabulateForAARCombatReportPanel() throws PWCGException
    {
        createCrewsInMission();
        createDeniedClaims();
        extractMissionHeader();
        createLossesForCrewMembersInMission();
        createLossesForEquipmentInMission();
        createVictoryEventsForCrewMembersInMission();
        return combatReportPanelData;
    }

    private void createDeniedClaims()
    {
        combatReportPanelData.addClaimsDenied(aarContext.getPersonnelAcheivements().getPlayerClaimsDenied());        
    }

    private void createCrewsInMission() throws PWCGException
    {
        Map<Integer, CrewMember> campaignMembersInMission = aarContext.getPreliminaryData().getCampaignMembersInMission().getCrewMemberCollection();
        CrewMembers crewMembersInMission = CrewMemberFilter.filterActiveAIAndPlayerAndAcesForCompany(campaignMembersInMission, campaign.getDate(), company.getCompanyId());
        combatReportPanelData.addCrewMembersInMission(crewMembersInMission);
    }

    private void extractMissionHeader()
    {
        combatReportPanelData.setMissionAARHeader(aarContext.getPreliminaryData().getPwcgMissionData().getMissionHeader());
    }

    private void createLossesForCrewMembersInMission() throws PWCGException
    {
        Map<Integer, CrewMemberStatusEvent> crewMembersLostInMission = crewMemberStatusEventGenerator.createCrewMemberLossEvents(aarContext.getPersonnelLosses());
        for (CrewMemberStatusEvent crewMemberLostEvent : crewMembersLostInMission.values())
        {
            if (isIncludeInCombatReport(crewMemberLostEvent.getCompanyId(), crewMemberLostEvent.getCrewMemberSerialNumber()))
            {
                combatReportPanelData.addCrewMemberLostInMission(crewMemberLostEvent);
            }
        }
    }

    private void createLossesForEquipmentInMission() throws PWCGException
    {
        Map<Integer, TankStatusEvent> tanksLostInMission = tankStatusEventGenerator.createTankLossEvents(aarContext.getEquipmentLosses());
        for (TankStatusEvent tankLostEvent : tanksLostInMission.values())
        {
            if (isIncludeInCombatReport(tankLostEvent.getCompanyId(), tankLostEvent.getCrewMemberSerialNumber()))
            {
                combatReportPanelData.addPlaneLostInMission(tankLostEvent);
            }
        }
    }

    private void createVictoryEventsForCrewMembersInMission() throws PWCGException
    {
        Map<Integer, List<Victory>> victoryAwardByCrewMember = aarContext.getPersonnelAcheivements().getVictoriesByCrewMember();
        List<VictoryEvent> victoriesInMission = victoryEventGenerator.createCrewMemberVictoryEvents(victoryAwardByCrewMember);
        for (VictoryEvent victoryEvent : victoriesInMission)
        {
            if (isIncludeInCombatReport(victoryEvent.getCompanyId(), victoryEvent.getCrewMemberSerialNumber()))
            {
                combatReportPanelData.addVictoryForCrewMembers(victoryEvent);
            }
        }
    }
    
    boolean isIncludeInCombatReport(int companyId, int serialNumber) throws PWCGException
    {
        if (companyId == company.getCompanyId())
        {
            if (aarContext.getMissionEvaluationData().wasCrewMemberInMission(serialNumber))
            {
                return true;
            }
        }
        
        return false;
    }

    public void setTankStatusEventGenerator(TankStatusEventGenerator tankStatusEventGenerator)
    {
        this.tankStatusEventGenerator = tankStatusEventGenerator;
    }

    public void setCrewMemberStatusEventGenerator(CrewMemberStatusEventGenerator crewMemberStatusEventGenerator)
    {
        this.crewMemberStatusEventGenerator = crewMemberStatusEventGenerator;
    }

    public void setVictoryEventGenerator(VictoryEventGenerator victoryEventGenerator)
    {
        this.victoryEventGenerator = victoryEventGenerator;
    }
}
