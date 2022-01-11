package pwcg.aar.inmission.phase2.logeval;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogBase;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogCrewMember;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTank;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.core.exception.PWCGException;

public class AARMissionEvaluationData
{
    private Map <String, LogTank> planeAiEntities = new HashMap <>();
    private List<LogVictory> victoryResults = new ArrayList <>();
    private List<LogCrewMember> crewMembersInMission = new ArrayList<>();
    private List<LogBase> chronologicalEvents = new ArrayList<>();
    
    public LogTank getPlaneInMissionBySerialNumber(Integer serialNumber) throws PWCGException
    {
        for (LogTank missionPlane : planeAiEntities.values())
        {
            if (missionPlane.isCrewMember(serialNumber))
            {
                return missionPlane;
            }
        }

        return null;
    }
    
    public boolean wasCrewMemberInMission(Integer serialNumber) throws PWCGException
    {
        for (LogTank missionPlane : planeAiEntities.values())
        {
            if (missionPlane.isCrewMember(serialNumber))
            {
                return true;
            }
        }

        return false;
    }

    public Map<String, LogTank> getPlaneAiEntities()
    {
        return planeAiEntities;
    }

    public void setPlaneAiEntities(Map<String, LogTank> planeAiEntities)
    {
        this.planeAiEntities = planeAiEntities;
    }

    public List<LogVictory> getVictoryResults()
    {
        return victoryResults;
    }

    public void setVictoryResults(List<LogVictory> victoryResults)
    {
        this.victoryResults = victoryResults;
    }

    public List<LogCrewMember> getCrewMembersInMission()
    {
        return crewMembersInMission;
    }

    public void setCrewMembersInMission(List<LogCrewMember> companyCrewsInMission)
    {
        this.crewMembersInMission = companyCrewsInMission;
    }

    public List<LogBase> getChronologicalEvents()
    {
        return chronologicalEvents;
    }

    public void setChronologicalEvents(List<LogBase> chronologicalEvents)
    {
        this.chronologicalEvents = chronologicalEvents;
    }
}
