package pwcg.testutils;

import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class CrewMemberPicker
{
    public static CrewMember pickNonAceCampaignMember (Campaign campaign, int companyId) throws PWCGException
    {
        Map<Integer, CrewMember> companyAllCampaignMembers = campaign.getPersonnelManager().getAllCampaignMembers();        
        CrewMembers crewMembers = CrewMemberFilter.filterActiveAINoWounded(companyAllCampaignMembers, campaign.getDate());        
        List<CrewMember> allHealthyCampaignMembers = crewMembers.getCrewMemberList();
        int index = RandomNumberGenerator.getRandom(allHealthyCampaignMembers.size());
        return allHealthyCampaignMembers.get(index);
    }
    
    public static CrewMember pickNonAceCrewMember (Campaign campaign, int companyId) throws PWCGException
    {
        CompanyPersonnel companyPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(companyId);        
        CrewMembers crewMembers = CrewMemberFilter.filterActiveAINoWounded(companyPersonnel.getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());        
        List<CrewMember> allHealthyCrewMembers = crewMembers.getCrewMemberList();
        int index = RandomNumberGenerator.getRandom(allHealthyCrewMembers.size());
        return allHealthyCrewMembers.get(index);
    }
    
    public static CrewMember pickPlayerCrewMember (Campaign campaign, int companyId) throws PWCGException
    {
        CompanyPersonnel companyPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(companyId);        
        CrewMembers crewMembers = CrewMemberFilter.filterActivePlayers(companyPersonnel.getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());        
        List<CrewMember> allHealthyPlayers = crewMembers.getCrewMemberList();
        int index = RandomNumberGenerator.getRandom(allHealthyPlayers.size());
        return allHealthyPlayers.get(index);
    }
}
