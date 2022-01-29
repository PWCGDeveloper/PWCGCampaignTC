package pwcg.aar.inmission.phase2.logeval;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogCrewMember;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTank;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.crewmember.SerialNumber.SerialNumberClassification;
import pwcg.core.exception.PWCGException;

public class AARCrewBuilder
{
    private Map <String, LogTank> tankAiEntities = new HashMap <>();

    public AARCrewBuilder(Map <String, LogTank> planeAiEntities)
    {
        this.tankAiEntities = planeAiEntities;
    }
    
    public List<LogCrewMember> buildCrewMembersFromLogTanks() throws PWCGException
    {
        List<LogCrewMember> crewMembersInMission = new ArrayList<LogCrewMember>();
        for (LogTank logTank : tankAiEntities.values())
        {
            if (logTank.isEquippedTank())
            {
                crewMembersInMission.add(logTank.getLogCrewMember());
            }
        }
        
        return crewMembersInMission;
     }

    public List<LogCrewMember> buildAcesFromLogTanks() throws PWCGException
    {
        List<LogCrewMember> aceCrewsInMission = new ArrayList<LogCrewMember>();
        for (LogTank logTank : tankAiEntities.values())
        {
            if (SerialNumber.getSerialNumberClassification(logTank.getCrewMemberSerialNumber()) == SerialNumberClassification.ACE)
            {
                aceCrewsInMission.add(logTank.getLogCrewMember());
            }
        }
        
        return aceCrewsInMission;
    }
}
