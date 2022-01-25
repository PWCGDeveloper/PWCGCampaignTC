package pwcg.aar.inmission.phase2.logeval.crewmemberstatus;

import pwcg.aar.inmission.phase2.logeval.AARDestroyedStatusEvaluator;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogCrewMember;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.crewmember.SerialNumber.SerialNumberClassification;
import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.LogParser;
import pwcg.core.logfiles.event.IAType3;

public class AARCrewMemberStatusDeadEvaluator 
{
    private IAType3 destroyedEventForCrewmembersPlane = null;
    private LogCrewMember logCrewMember = null;
    private AARDestroyedStatusEvaluator destroyedStatusEvaluator;

    public AARCrewMemberStatusDeadEvaluator(AARDestroyedStatusEvaluator destroyedStatusEvaluator)
    {
    	this.destroyedStatusEvaluator = destroyedStatusEvaluator;
    }

    public void initialize(LogCrewMember resultCrewmember,
                      	   IAType3 destroyedEventForCrewMember)
    {
        this.logCrewMember = resultCrewmember;
        this.destroyedEventForCrewmembersPlane = destroyedEventForCrewMember;
    }

    public boolean isCrewMemberDead() throws PWCGException
    {
    	boolean isCrewMemberDead = destroyedStatusEvaluator.didCrewMemberDie(logCrewMember.getSerialNumber());
    	if (isCrewMemberDead)
    	{
    	    if (SerialNumber.getSerialNumberClassification(logCrewMember.getSerialNumber()) != SerialNumberClassification.PLAYER)
    	    {
    	        isCrewMemberDead = didCompanyAiMemberDieAfterSpecialConsiderations();
    	    }
    	}
        
        return isCrewMemberDead;
    }

    private boolean didCompanyAiMemberDieAfterSpecialConsiderations() throws PWCGException
    {
        boolean isDead = false;
        
        if (wasCrewMemberDestroyed())
        {
            isDead = true;
        }
        else
        {
            isDead = false;
        }

        return isDead;
    }
    
    private boolean wasCrewMemberDestroyed()
    {
        if (destroyedEventForCrewmembersPlane != null)
        {
            if (destroyedEventForCrewmembersPlane.getVictor() != null)
            {
                if (!destroyedEventForCrewmembersPlane.getVictor().equals(LogParser.UNKNOWN_MISSION_LOG_ENTITY))
                {
                    return true;
                }
            }
        }

        return false;
    }
}

