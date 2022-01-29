package pwcg.aar.inmission.phase3.reconcile.victories.singleplayer;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTank;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.company.Company;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.crewmember.SerialNumber.SerialNumberClassification;

public class VictoryAssignDecision
{

    public static boolean isAssignPlayer(LogVictory resultVictory)
    {
        if (!(resultVictory.getVictor() instanceof LogTank))
        {
            return false;
        }
                
        LogTank victorTank = (LogTank)resultVictory.getVictor();
        if (!(SerialNumber.getSerialNumberClassification(victorTank.getCrewMemberSerialNumber()) == SerialNumberClassification.PLAYER))
        {
            return false;
        }
        
        return true;
    }

    public static boolean isAssignAi(LogVictory resultVictory)
    {
        if (!(resultVictory.getVictor() instanceof LogTank))
        {
            return false;
        }
                
        LogTank victorTank = (LogTank)resultVictory.getVictor();
        if (SerialNumber.getSerialNumberClassification(victorTank.getCrewMemberSerialNumber()) == SerialNumberClassification.NONE)
        {
            return false;
        }
        
        if (victorTank.getCompanyId() == Company.AI)
        {
            return false;
        }
        
        return true;
    }
}
