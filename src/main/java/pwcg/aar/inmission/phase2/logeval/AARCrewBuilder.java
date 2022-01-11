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
    private Map <String, LogTank> planeAiEntities = new HashMap <>();

    public AARCrewBuilder(Map <String, LogTank> planeAiEntities)
    {
        this.planeAiEntities = planeAiEntities;
    }
    
    public List<LogCrewMember> buildCrewMembersFromLogPlanes() throws PWCGException
    {
        List<LogCrewMember> crewMembersInMission = new ArrayList<LogCrewMember>();
        for (LogTank logPlane : planeAiEntities.values())
        {
            crewMembersInMission.add(logPlane.getLogCrewMember());
        }
        
        if (crewMembersInMission.isEmpty())
        {
            throw new PWCGException("No squadron members found for mission");
        }
        
        return crewMembersInMission;
     }

    public List<LogCrewMember> buildAcesFromLogPlanes()
    {
        List<LogCrewMember> aceCrewsInMission = new ArrayList<LogCrewMember>();
        for (LogTank logPlane : planeAiEntities.values())
        {
            if (SerialNumber.getSerialNumberClassification(logPlane.getCrewMemberSerialNumber()) == SerialNumberClassification.ACE)
            {
                aceCrewsInMission.add(logPlane.getLogCrewMember());
            }
        }
        
        return aceCrewsInMission;
    }
}
