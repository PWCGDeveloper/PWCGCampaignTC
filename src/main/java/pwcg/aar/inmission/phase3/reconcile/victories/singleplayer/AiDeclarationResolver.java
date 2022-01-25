package pwcg.aar.inmission.phase3.reconcile.victories.singleplayer;

import java.util.List;
import java.util.Map;

import pwcg.aar.data.AARContext;
import pwcg.aar.inmission.phase2.logeval.AARMissionEvaluationData;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTank;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogUnknown;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.aar.inmission.phase3.reconcile.victories.common.ConfirmedVictories;
import pwcg.aar.inmission.phase3.reconcile.victories.common.VictorySorter;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.company.Company;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.core.exception.PWCGException;

public class AiDeclarationResolver  extends PlayerVictoryResolver
{
    private ConfirmedVictories confirmedAiVictories = new ConfirmedVictories();
    private Campaign campaign;
    private AARContext aarContext;

    public AiDeclarationResolver (Campaign campaign, AARContext aarContext)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
    }

    public ConfirmedVictories determineAiAirResults  (VictorySorter victorySorter) throws PWCGException 
    {
        for (LogVictory resultVictory : victorySorter.getFirmTankVictories())
        {
            resolveAiFirmClaim(resultVictory);
        }
        
        for (LogVictory resultVictory : victorySorter.getFirmPlaneVictories())
        {
            resolveAiFirmClaim(resultVictory);
        }
        
        for (LogVictory resultVictory : victorySorter.getFuzzyTankVictories())
        {
            resolveAiClaimByProximity(resultVictory);
        }
        
        for (LogVictory resultVictory : victorySorter.getAllUnconfirmed())
        {
            resolveRandomAssignment(resultVictory);
        }
        
        return confirmedAiVictories;
    }

    private void resolveAiFirmClaim(LogVictory resultVictory) throws PWCGException 
    {
        if (resultVictory.getVictor() instanceof LogTank)
        {
            LogTank victorPlanePlane = (LogTank)resultVictory.getVictor();
            CrewMember companyMemberVictor = campaign.getPersonnelManager().getAnyCampaignMember(victorPlanePlane.getCrewMemberSerialNumber());
            if (companyMemberVictor != null)
            {
                if (!PlayerVictoryResolver.isPlayerVictory(companyMemberVictor, resultVictory.getVictor()))
                {
                    if (!resultVictory.isConfirmed())
                    {
                        createAiVictory(resultVictory, companyMemberVictor);
                    }
                }
            }
        }
    }

    private void resolveAiClaimByProximity(LogVictory resultVictory) throws PWCGException 
    {
        if (!resultVictory.isConfirmed())
        {
            if (resultVictory.getVictor() instanceof LogUnknown)
            {
                CrewMember crewMemberVictor = flightMemberForVictory(resultVictory);
                if (crewMemberVictor != null)
                {
                    createAiVictory(resultVictory, crewMemberVictor);
                }
            }
        }
    }

    private void resolveRandomAssignment(LogVictory resultVictory) throws PWCGException 
    {
        if (!resultVictory.isConfirmed())
        {
            if (resultVictory.getVictor() instanceof LogUnknown)
            {
                LogUnknown missionEntityUnknown = (LogUnknown)resultVictory.getVictor();
                if (missionEntityUnknown.getUnknownVictoryAssignment() == UnknownVictoryAssignments.RANDOM_ASSIGNMENT)
                {
                    CrewMember crewMemberVictor = flightMemberForVictory(resultVictory);
                    if (crewMemberVictor != null)
                    {
                        createAiVictory(resultVictory, crewMemberVictor);
                    }
                }
            }
        }
    }

    private CrewMember flightMemberForVictory(LogVictory resultVictory) throws PWCGException
    {
        ICountry victimCountry = resultVictory.getVictim().getCountry();
        CrewMembers crewMembersInMissionOtherThanPlayer = getAiMissionCrewMembers();
        for (CrewMember crewMemberVictor: crewMembersInMissionOtherThanPlayer.getCrewMemberList())
        {
            if (crewMemberVictor.determineCountry(campaign.getDate()).getSide() != victimCountry.getSide())
            {
                if (crewMemberVictor != null)
                {
                    if (!alreadyhasVictory(crewMemberVictor.getSerialNumber()))
                    {
                        return crewMemberVictor;
                    }
                }
            }
        }
        
        return null;
    }

    private CrewMembers getAiMissionCrewMembers() throws PWCGException
    {
        Map<Integer, CrewMember> campaignMembersInMission = aarContext.getPreliminaryData().getCampaignMembersInMission().getCrewMemberCollection();
        List<Company> playerCompanysInMission = aarContext.getPreliminaryData().getPlayerCompanysInMission();
        CrewMembers crewMembersInMissionOtherThanPlayer = new CrewMembers();
        for (Company company : playerCompanysInMission)
        {
            CrewMembers crewMembersForCompany = CrewMemberFilter.filterActiveAIForCompany(campaignMembersInMission, campaign.getDate(), company.getCompanyId());
            crewMembersInMissionOtherThanPlayer.addCrewMembers(crewMembersForCompany);
        }
        return crewMembersInMissionOtherThanPlayer;
    }

    private boolean alreadyhasVictory(Integer serialNumber) throws PWCGException
    {
        for (LogVictory confirmedAiVictory : confirmedAiVictories.getConfirmedVictories())
        {
            if (confirmedAiVictory.getVictor() instanceof LogTank)
            {
                LogTank victorPlanePlane = (LogTank)confirmedAiVictory.getVictor();
                if (victorPlanePlane.getCrewMemberSerialNumber() == serialNumber)
                {
                    return true;
                }
            }
        }

        return false;
   }

    private void createAiVictory(LogVictory resultVictory, CrewMember crewMemberVictor) throws PWCGException
    {
        AARMissionEvaluationData evaluationData = aarContext.getMissionEvaluationData();
        LogTank companyMemberPlane = evaluationData.getPlaneInMissionBySerialNumber(crewMemberVictor.getSerialNumber());
        if (companyMemberPlane != null)
        {
            resultVictory.setVictor(companyMemberPlane);
            resultVictory.setConfirmed(true);
                    
            confirmedAiVictories.addVictory(resultVictory);
        }
    }
}
