package pwcg.aar.inmission.phase3.reconcile.victories.singleplayer;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTank;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogUnknown;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.aar.inmission.phase3.reconcile.victories.common.VictorySorter;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;

class PlayerVictoryReassigner
{
    private Campaign campaign;
    
    PlayerVictoryReassigner (Campaign campaign)
    {
        this.campaign = campaign;
    }

    void resetUnclamedPlayerVictoriesForAssignmentToOthers(VictorySorter victorySorter) throws PWCGException
    {
        for (LogVictory resultVictory : victorySorter.getFirmTankVictories())
        {
            if(VictoryAssignDecision.isAssignPlayer(resultVictory))
            {
                forRandomAssignment(resultVictory);
            }
        }
        
        for (LogVictory resultVictory : victorySorter.getFirmPlaneVictories())
        {
            if(VictoryAssignDecision.isAssignPlayer(resultVictory))
            {
                forRandomAssignment(resultVictory);
            }
        }
    }

    private void forRandomAssignment(LogVictory resultVictory) throws PWCGException
    {
        if (!resultVictory.isConfirmed())
        {
            if (resultVictory.getVictor() instanceof LogTank)
            {
                LogTank victorTank = (LogTank)resultVictory.getVictor();
                CrewMember crewMember = campaign.getPersonnelManager().getAnyCampaignMember(victorTank.getCrewMemberSerialNumber());
                if (PlayerVictoryResolver.isPlayerVictory(crewMember, resultVictory.getVictor()))
                {
                    LogUnknown markedForAssignment = new LogUnknown();
                    markedForAssignment.setUnknownVictoryAssignment(UnknownVictoryAssignments.RANDOM_ASSIGNMENT);
                    resultVictory.setVictor(markedForAssignment);
                }
            }
        }
    }

}
