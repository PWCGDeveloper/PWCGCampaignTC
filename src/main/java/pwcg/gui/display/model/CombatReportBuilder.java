package pwcg.gui.display.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import pwcg.aar.AARCoordinator;
import pwcg.aar.ui.events.model.ClaimDeniedEvent;
import pwcg.aar.ui.events.model.CrewMemberStatusEvent;
import pwcg.aar.ui.events.model.TankStatusEvent;
import pwcg.aar.ui.events.model.VictoryEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.CombatReport;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.crewmember.VictoryDescription;
import pwcg.campaign.tank.EquippedTank;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.data.MissionHeader;

public class CombatReportBuilder
{
    private Campaign campaign;
    private CrewMember reportCrewMember;
    private AARCoordinator aarCoordinator;
    private CombatReport combatReport = new CombatReport();

    public CombatReportBuilder(Campaign campaign, CrewMember reportCrewMember, AARCoordinator aarCoordinator)
    {
        this.campaign = campaign;
        this.reportCrewMember = reportCrewMember;
        this.aarCoordinator = aarCoordinator;
    }
    
    public CombatReport createCombatReport() throws PWCGException 
    {                
        createCombatReportHeader();

        String missionStatement = createCombatReportMissionStatement();
        combatReport.setHaReport(missionStatement);

        String narrativeStatement = "";
        combatReport.setNarrative(narrativeStatement);
        
        return combatReport;
    }

    
    private CombatReport createCombatReportHeader() throws PWCGException
    {
        combatReport.setCrewMemberSerialNumber(reportCrewMember.getSerialNumber());
        combatReport.setReportCrewMemberName(reportCrewMember.getNameAndRank());
        combatReport.setCompany(reportCrewMember.determineCompany().determineDisplayName(campaign.getDate()));
        
        MissionHeader missionHeader = aarCoordinator.getAarContext().findUiCombatReportDataForCompany(reportCrewMember.getCompanyId()).
                        getCombatReportPanelData().getMissionHeader();
        Date combatReportDate = DateUtils.getDateYYYYMMDD(missionHeader.getDate());
        
        combatReport.setDate(combatReportDate);
        combatReport.setTime(missionHeader.getTime());
        
        if (campaign.isCoop())
        {
            setCrewMembersForCoop();            
        }
        else
        {
            setCrewMembersForSinglePlayer();
        }
        
        combatReport.setType(missionHeader.getVehicleType());
        combatReport.setDuty(missionHeader.getDuty());

        return combatReport;
    }

    private void setCrewMembersForSinglePlayer() throws PWCGException
    {
        combatReport.setReportCrewMemberName(reportCrewMember.getNameAndRank());
    }   

    private void setCrewMembersForCoop()
    {
        CrewMembers campaignMembersInMission = aarCoordinator.getAarContext().getPreliminaryData().getCampaignMembersInMission();
        for (CrewMember crewMember : campaignMembersInMission.getCrewMemberList())
        {
            if (crewMember.getCompanyId() == reportCrewMember.getCompanyId())
            {
                combatReport.addFlightCrewMember(crewMember.getNameAndRank());
            }
        }
    }   

    private String createCombatReportMissionStatement() throws PWCGException
    {
        String missionStatement = "";
        missionStatement += createCrewsInMissionReport();
        missionStatement += createClaimStatusReport();
        missionStatement += createCrewMemberLostReport();
        missionStatement += createEquipmentLostReport();
        return missionStatement;
    }


    private String createCrewMemberLostReport() throws PWCGException
    {
        Map<Integer, CrewMemberStatusEvent> crewMembersLostInMission = aarCoordinator.getAarContext()
                        .findUiCombatReportDataForCompany(reportCrewMember.getCompanyId()).
                        getCombatReportPanelData().getCrewMembersLostInMission();

        String crewMembersLostStatement = "";
        String crewMembersLostAppend = "";            
        for (CrewMemberStatusEvent crewMemberLostEvent : crewMembersLostInMission.values())
        {
            crewMembersLostAppend += "    " + 
                            crewMemberLostEvent.getCrewMemberName() + ": " + 
                            CrewMemberStatus.crewMemberStatusToStatusDescription(crewMemberLostEvent.getStatus()) + "\n";
        }
        
        if (crewMembersLostAppend.length() > 0)
        {
            crewMembersLostStatement = "Crew members lost: \n" + crewMembersLostAppend;
        }

        return crewMembersLostStatement;
    }


    private String createEquipmentLostReport() throws PWCGException
    {
        Map<Integer, TankStatusEvent> companyTanksLostInMission = aarCoordinator.getAarContext()
                    .findUiCombatReportDataForCompany(reportCrewMember.getCompanyId()).
                    getCombatReportPanelData().getCompanyTanksLostInMission();

        String tanksLostStatement = "";
        String tanksLostAppend = "";        
        for (TankStatusEvent tankLostEvent :companyTanksLostInMission.values())
        {
            EquippedTank lostTank = campaign.getEquipmentManager().getAnyTankWithPreference(tankLostEvent.getPlaneSerialNumber());
            if(lostTank != null)
            {
                tanksLostAppend += "    " + lostTank.getDisplayName() + "\n";
            }
        }
        
        if (tanksLostAppend.length() > 0)
        {
            tanksLostStatement = "Vehicles lost: \n" + tanksLostAppend;
        }

        return tanksLostStatement;
    }
    
    private String createCrewsInMissionReport() throws PWCGException
    {        
        Map<Integer, CrewMember> crewMembersInMission = aarCoordinator.getAarContext().
                        findUiCombatReportDataForCompany(reportCrewMember.getCompanyId()).getCombatReportPanelData().getCrewsInMission();

        String missionStatement;
        missionStatement = "This mission was undertaken by:\n";
        for (CrewMember crewMemberCrewMember : crewMembersInMission.values())
        {
            if (crewMemberCrewMember != null)
            {
                missionStatement += crewMemberCrewMember.getNameAndRank();
                missionStatement += "\n";
            }

        }
        return missionStatement;
    }

    private String createClaimStatusReport() throws PWCGException
    {
        List<VictoryEvent> victoryEvents = aarCoordinator.getAarContext().
                        findUiCombatReportDataForCompany(reportCrewMember.getCompanyId()).getCombatReportPanelData().getVictoriesForCrewMembersInMission();

        String claimStatusStatement = "";
        String victoryAppend = "";
        for (VictoryEvent victoryEvent : victoryEvents)
        {            
            VictoryDescription victoryDescription = new VictoryDescription(campaign, victoryEvent.getVictory());
            String victoryDescriptionText = victoryDescription.createVictoryDescription();
            victoryAppend += victoryDescriptionText + "\n\n";
        }
        
        
        List<ClaimDeniedEvent> claimDeniedEvents = aarCoordinator.getAarContext().
                        findUiCombatReportDataForCompany(reportCrewMember.getCompanyId()).getCombatReportPanelData().getClaimsDenied();

        for (ClaimDeniedEvent claimDeniedEvent : claimDeniedEvents)
        {
            victoryAppend += "    Claim denied for " + claimDeniedEvent.getType() + "\n\n";               
        }

        if (victoryAppend.length() > 0)
        {
            claimStatusStatement = "Claim Status: \n" + victoryAppend;
        }

        claimStatusStatement += "\n";

        return claimStatusStatement; 
    }

}
