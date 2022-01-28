package pwcg.aar.prelim;

import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.core.exception.PWCGException;
import pwcg.mission.data.PwcgGeneratedMissionVehicleData;

public class CampaignMembersInMissionFinder
{
    public CrewMembers determineCampaignMembersInMission(Campaign campaign, PwcgMissionData pwcgMissionData) throws PWCGException
    {
        CrewMembers campaignMembersInMission = new CrewMembers();
        for (PwcgGeneratedMissionVehicleData missionTank : pwcgMissionData.getMissionTanks().values())
        {
            CrewMember crewMember = campaign.getPersonnelManager().getAnyCampaignMember(missionTank.getCrewMemberSerialNumber());
            if (crewMember != null)
            {
                campaignMembersInMission.addToCrewMemberCollection(crewMember);
            }
        }

        return campaignMembersInMission;
    }

}
