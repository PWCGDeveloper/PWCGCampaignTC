package pwcg.aar.outofmission.phase1.elapsedtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTank;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.Victory;

public class OutOfMissionVictoryData
{
    private Map<Integer, List<Victory>> victoryAwardsByCrewMember = new HashMap<>();
    private Map<Integer, CrewMember> shotDownCrewMembers = new HashMap<>();
    private Map<Integer, LogTank> destroyedTanks = new HashMap<>();
    
    public void merge(OutOfMissionVictoryData victoryData)
    {
        for (Integer crewMemberSerialNumber : victoryData.getVictoryAwardsByCrewMember().keySet())
        {
            List<Victory> victories = victoryData.getVictoryAwardsByCrewMember().get(crewMemberSerialNumber);
            for (Victory victory : victories)
            {
                addVictoryAwards(crewMemberSerialNumber, victory);
            }
        }
        
        for (CrewMember shotDownCrewMember : victoryData.getShotDownCrewMembers().values())
        {
            addShotDownCrewMember(shotDownCrewMember);
        }
        
        for (LogTank shotDownPlane : victoryData.getDestroyedTanks().values())
        {
            addShotDownPlane(shotDownPlane);
        }
    }
    
    public void addVictoryAwards(Integer crewMemberSerialNumber, Victory victory)
    {
        if (!victoryAwardsByCrewMember.containsKey(crewMemberSerialNumber))
        {
            List<Victory> victoriesForCrewMember = new ArrayList<>();
            victoryAwardsByCrewMember.put(crewMemberSerialNumber, victoriesForCrewMember);
        }
        
        List<Victory> victoriesForCrewMember = victoryAwardsByCrewMember.get(crewMemberSerialNumber);
        victoriesForCrewMember.add(victory);
    }

    public void addVictoryEvents(OutOfMissionVictoryData victoryEvents)
    {
        victoryAwardsByCrewMember.putAll(victoryEvents.getVictoryAwardsByCrewMember());
    }

    public void addShotDownCrewMember(CrewMember shotDownCrewMember)
    {
        shotDownCrewMembers.put(shotDownCrewMember.getSerialNumber(), shotDownCrewMember);
    }

    public void addShotDownPlane(LogTank shotDownPlane)
    {
        destroyedTanks.put(shotDownPlane.getTankSerialNumber(), shotDownPlane);
    }
    
    public Map<Integer, CrewMember> getShotDownCrewMembers()
    {
        return shotDownCrewMembers;
    }

    public Map<Integer, List<Victory>> getVictoryAwardsByCrewMember()
    {
        return victoryAwardsByCrewMember;
    }

    public Map<Integer, LogTank> getDestroyedTanks()
    {
        return destroyedTanks;
    }
}
