package pwcg.mission.playerunit.crew;

import java.util.HashMap;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.core.exception.PWCGException;
import pwcg.mission.MissionHumanParticipants;
import pwcg.mission.unit.UnitInformation;

public class CrewFactory
{
    private Campaign campaign;
    private Company company;
    private MissionHumanParticipants participatingPlayers;
    private Map <Integer, CrewMember> crewsForCompany = new HashMap <>();
    
	public CrewFactory(UnitInformation unitInformation)
	{
        this.campaign = unitInformation.getCampaign();
        this.company = unitInformation.getCompany();
        this.participatingPlayers = unitInformation.getMission().getParticipatingPlayers();
	}

    public Map <Integer, CrewMember> createCrews() throws PWCGException 
    {
        createCrewsForCompany();
        ensurePlayerIsAssigned();
        return crewsForCompany;
    }

    private void createCrewsForCompany() throws PWCGException
    {
        CrewMembers companyMembers = CrewMemberFilter.filterActiveAIAndPlayerAndAcesNoWounded(
                campaign.getPersonnelManager().getCompanyPersonnel(company.getCompanyId()).getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());
        for (CrewMember crewMember : companyMembers.getCrewMemberList())
        {
            crewsForCompany.put(crewMember.getSerialNumber(), crewMember);
        }
    }

    private void ensurePlayerIsAssigned() throws PWCGException
    {
        for (CrewMember player : participatingPlayers.getAllParticipatingPlayers())
        {
            addPlayerToMissionIfNeeded(player);
        }
    }

    private void addPlayerToMissionIfNeeded(CrewMember player)
    {
        if (player.getCompanyId() != company.getCompanyId())
        {
            return;
        }
         
        if (crewsForCompany.containsKey(player.getSerialNumber()))
        {
            return;
        }
         
        for (CrewMember companyMemberToBeReplaced : crewsForCompany.values())
        {
            if (!companyMemberToBeReplaced.isPlayer())
            {
                crewsForCompany.put(player.getSerialNumber(), player);
                crewsForCompany.remove(companyMemberToBeReplaced.getSerialNumber());
                break;
            }
        }
    }
}
