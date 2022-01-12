package pwcg.aar.outofmission.phase1.elapsedtime;

import pwcg.aar.data.AARContext;
import pwcg.aar.outofmission.phase2.awards.MissionsCompletedCalculator;
import pwcg.aar.prelim.CampaignMembersOutOfMissionFinder;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.core.exception.PWCGException;

public class AARMissionsCompletedUpdater
{
    private Campaign campaign;
    private AARContext aarContext;

    public AARMissionsCompletedUpdater(Campaign campaign, AARContext aarContext)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
    }

    public void updateMissionsCompleted() throws PWCGException 
    {        
        missionsCompletedInMission();
        missionsCompletedOutOfMission();
    }
    
    private void missionsCompletedInMission() throws PWCGException
    {
        CrewMembers campaignMembersInMission = aarContext.getPreliminaryData().getCampaignMembersInMission();
        for (CrewMember crewMember : campaignMembersInMission.getCrewMemberList())
        {
            int updatedMissionsCompleted = MissionsCompletedCalculator.calculateMissionsCompleted(campaign, crewMember);
            aarContext.getPersonnelAcheivements().updateMissionsCompleted(crewMember.getSerialNumber(), updatedMissionsCompleted);
        }
    }

    public void missionsCompletedOutOfMission() throws PWCGException 
    {        
        CrewMembers campaignMembersInMission = aarContext.getPreliminaryData().getCampaignMembersInMission();
        CrewMembers campaignMembersNotInMission = CampaignMembersOutOfMissionFinder.getActiveCampaignMembersNotInMission(campaign, campaignMembersInMission);
        
        for (CrewMember crewMember : campaignMembersNotInMission.getCrewMemberList())
        {
            if (OutOfMissionCrewMemberSelector.shouldCrewMemberBeEvaluated(campaign, crewMember)) 
            {
                int updatedMissionsCompleted = MissionsCompletedCalculator.calculateMissionsCompleted(campaign, crewMember);
                aarContext.getPersonnelAcheivements().updateMissionsCompleted(crewMember.getSerialNumber(), updatedMissionsCompleted);
            }
        }
    }
}
