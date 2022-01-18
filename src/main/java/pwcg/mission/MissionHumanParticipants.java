package pwcg.mission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.api.Side;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.MathUtils;

public class MissionHumanParticipants 
{
	private Map<Integer, List<CrewMember>> participatingPlayers = new HashMap<>();

	public void addCrewMembers(List<CrewMember> participatingPlayers)
	{
		for (CrewMember participatingPlayer : participatingPlayers)
		{
			addCrewMember(participatingPlayer);
		}
	}

	public void addCrewMember(CrewMember participatingPlayer)
	{
		if (!participatingPlayers.containsKey(participatingPlayer.getCompanyId()))
		{
			List<CrewMember> participatingPlayersForCompany = new ArrayList<>();
			participatingPlayers.put(participatingPlayer.getCompanyId(), participatingPlayersForCompany);
		}
		
		List<CrewMember> participatingPlayersForCompany = participatingPlayers.get(participatingPlayer.getCompanyId());
		participatingPlayersForCompany.add(participatingPlayer);
	}
	
	public List<CrewMember> getParticipatingPlayersForCompany (int companyId)
	{
		List<CrewMember> participatingPlayersForCompany = participatingPlayers.get(companyId);
		if (participatingPlayersForCompany == null)
		{
			return new ArrayList<>();
		}
		return participatingPlayersForCompany;
	}
    
    public List<Integer> getParticipatingCompanyIds ()
    {
        return new ArrayList<Integer>(participatingPlayers.keySet());
    }
	
	public boolean isCompanyInMission(Company company)
	{
	       return participatingPlayers.containsKey(company.getCompanyId());
	}
	
	public boolean isPlayerInMission(Company company, CrewMember player)
	{
		List<CrewMember> playersForCompany =  getParticipatingPlayersForCompany(company.getCompanyId());
		for (CrewMember playerForCompany : playersForCompany)
		{
			if (playerForCompany.getSerialNumber() == player.getSerialNumber())
			{
				return true;
			}
		}
		
		return false;
	}
    
    public List<CrewMember> getAllParticipatingPlayers()
    {
        List<CrewMember> allParticipatingPlayers = new ArrayList<>();
        for (List<CrewMember> playersForCompany : participatingPlayers.values())
        {
            allParticipatingPlayers.addAll(playersForCompany);
        }
        
        return allParticipatingPlayers;
    }
    
    public double getPlayerDistanceToTarget(Mission mission) throws PWCGException
    {
        double totalPlayerDistanceToTarget = 0.0;
        for (int playersCompanyId : participatingPlayers.keySet())
        {
            Company company = PWCGContext.getInstance().getCompanyManager().getCompany(playersCompanyId);
            totalPlayerDistanceToTarget += MathUtils.calcDist(company.determineCurrentPosition(mission.getCampaign().getDate()), mission.getMissionBorders().getCenter());
        }
        
        double averagePlayerDistanceToTarget = totalPlayerDistanceToTarget / participatingPlayers.size();
        return averagePlayerDistanceToTarget;
    }

    public List<Company> getMissionPlayerCompanys() throws PWCGException
    {
        Map<Integer, Company> playerCompanysMap = new HashMap<>();
        for (int playersCompanyId : participatingPlayers.keySet())
        {
            Company company = PWCGContext.getInstance().getCompanyManager().getCompany(playersCompanyId);
            playerCompanysMap.put(playersCompanyId, company);
        }
        
        List<Company> companys = new ArrayList<>(playerCompanysMap.values());
        return companys;
    }
    
    public List<Company> getParticipatingCompanyIdsForSide (Side side) throws PWCGException
    {
        List<Company> companiedPerSide = new ArrayList<>();
        for(int companyId : getParticipatingCompanyIds())
        {
            Company company = PWCGContext.getInstance().getCompanyManager().getCompany(companyId);
            if(company.determineSide() == side)
            {
                companiedPerSide.add(company);
            }
        }
        return companiedPerSide;
    }

    public List<Side> getMissionPlayerSides() throws PWCGException
    {
        Map<Side, Side> playerSideMap = new HashMap<>();
        for (int playersCompanyId : participatingPlayers.keySet())
        {
            Company company = PWCGContext.getInstance().getCompanyManager().getCompany(playersCompanyId);
            playerSideMap.put(company.determineSide(), company.determineSide());
        }
        
        List<Side> playerSides = new ArrayList<>(playerSideMap.values());
        return playerSides;
    }
}
