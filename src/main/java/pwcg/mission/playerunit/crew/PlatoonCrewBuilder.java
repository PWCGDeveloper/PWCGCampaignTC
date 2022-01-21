package pwcg.mission.playerunit.crew;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.company.Company;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberSorter;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.crewmember.SerialNumber.SerialNumberClassification;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.platoon.PlatoonInformation;

public class PlatoonCrewBuilder
{
    private PlatoonInformation platoonInformation;

    private Map <Integer, CrewMember> assignedCrewMap = new HashMap <>();
    private Map <Integer, CrewMember> unassignedCrewMap = new HashMap <>();
    
	public PlatoonCrewBuilder(PlatoonInformation platoonInformation)
	{
        this.platoonInformation = platoonInformation;
	}

    public List<CrewMember> createCrewAssignmentsForPlatoon(int numCrewNeeded) throws PWCGException 
    {
        CrewFactory crewFactory = new CrewFactory(platoonInformation);
        unassignedCrewMap = crewFactory.createCrews();
        
        assignPlayersToCrew();
        assignAiCrewMembersToTanks(numCrewNeeded);
        
        List<CrewMember> sortedByRank = sortCrewsByRank();
        return sortedByRank;
    }

    private void assignPlayersToCrew() throws PWCGException
    {
        if (Company.isPlayerCompany(platoonInformation.getCampaign(), platoonInformation.getCompany().getCompanyId()))
        {
            List<CrewMember> participatingPlayerCrews = new ArrayList<>();
            for (CrewMember crewMember : platoonInformation.getParticipatingPlayersForCompany())
            {
            	participatingPlayerCrews.add(crewMember);
            }

            for (CrewMember  participatingPlayerCrew : participatingPlayerCrews)
            {
                assignedCrewMap.put(participatingPlayerCrew.getSerialNumber(), participatingPlayerCrew);
                unassignedCrewMap.remove(participatingPlayerCrew.getSerialNumber());
            }
        }
    }

	private void assignAiCrewMembersToTanks(int numCrewNeeded) throws PWCGException
    {
        while (assignedCrewMap.size() < numCrewNeeded)
        {
            List<Integer> unassignedAiCrewSerialNumbers = buildUnassignedAiCrewMembers();
            
            int crewIndex = RandomNumberGenerator.getRandom(unassignedAiCrewSerialNumbers.size());
            int selectedSerialNumber = unassignedAiCrewSerialNumbers.get(crewIndex);
            CrewMember crewToAssign = unassignedCrewMap.get(selectedSerialNumber);
            assignedCrewMap.put(crewToAssign.getSerialNumber(), crewToAssign);
            unassignedCrewMap.remove(crewToAssign.getSerialNumber());
        }
    }
    
	private List<Integer> buildUnassignedAiCrewMembers()
	{
        List<Integer> unassignedAiCrewSerialNumbers = new ArrayList<>();
        List<Integer> unassignedCrewSerialNumbers = new ArrayList<>(unassignedCrewMap.keySet());
        for (int unassignedCrewSerialNumber : unassignedCrewSerialNumbers)
        {
        	if (shouldAssignAICrewMember(unassignedCrewSerialNumber))
        	{
        		unassignedAiCrewSerialNumbers.add(unassignedCrewSerialNumber);
        	}
        }
        return unassignedAiCrewSerialNumbers;
	}
	
	private boolean shouldAssignAICrewMember(int unassignedCrewSerialNumber)
	{
        if (SerialNumber.getSerialNumberClassification(unassignedCrewSerialNumber) == SerialNumberClassification.AI)
        {
            return true;
        }
        else if (SerialNumber.getSerialNumberClassification(unassignedCrewSerialNumber) == SerialNumberClassification.ACE)
        {
            return true;
        }
        else
        {
            return false;
        }
	}
	
    private List<CrewMember> sortCrewsByRank() throws PWCGException
    {
        return CrewMemberSorter.sortCrewMembers(platoonInformation.getCampaign(), assignedCrewMap);
    }
}
