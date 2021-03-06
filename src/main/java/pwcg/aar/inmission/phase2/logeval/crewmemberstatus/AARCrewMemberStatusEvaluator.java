package pwcg.aar.inmission.phase2.logeval.crewmemberstatus;

import java.util.List;

import pwcg.aar.inmission.phase2.logeval.AARDestroyedStatusEvaluator;
import pwcg.aar.inmission.phase2.logeval.AARVehicleBuilder;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogCrewMember;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTank;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.crewmember.SerialNumber.SerialNumberClassification;
import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.LogEventData;
import pwcg.core.logfiles.event.IAType2;
import pwcg.core.logfiles.event.IAType3;

public class AARCrewMemberStatusEvaluator 
{
    private Campaign campaign;
    private LogEventData logEventData;
    private AARVehicleBuilder aarVehicleBuilder;
    private AARCrewMemberStatusDeadEvaluator crewMemberStatusDeadEvaluator;
    private AARCrewMemberStatusWoundedEvaluator crewMemberStatusWoundedEvaluator;
    
    public AARCrewMemberStatusEvaluator(Campaign campaign, AARDestroyedStatusEvaluator destroyedStatusEvaluator, LogEventData logEventData, AARVehicleBuilder aarVehicleBuilder)
    {
        this.campaign = campaign;
        this.logEventData = logEventData;
        this.aarVehicleBuilder = aarVehicleBuilder;

        crewMemberStatusDeadEvaluator = new AARCrewMemberStatusDeadEvaluator(destroyedStatusEvaluator);
        crewMemberStatusWoundedEvaluator = new AARCrewMemberStatusWoundedEvaluator();
    }

    public void determineFateOfCrewsInMission () throws PWCGException 
    {        
        for (LogTank resultTank : aarVehicleBuilder.getLogTanks().values())
        {
            if (resultTank.isEquippedTank())
            {
                LogCrewMember resultCrewmember = resultTank.getLogCrewMember();
                if (resultCrewmember.getSerialNumber() < SerialNumber.NO_SERIAL_NUMBER)
                {
                    determineCrewMemberStatus(resultTank, resultCrewmember);
                }
            }
        }
    }


    private void determineCrewMemberStatus(
                    LogTank resultTank,
                    LogCrewMember resultCrewmember) throws PWCGException
    {
        setCrewMemberWounded(resultCrewmember);
        setCrewMemberDead(resultTank, resultCrewmember);
        adjustForPlayer(resultCrewmember);
    }

    private void adjustForPlayer(LogCrewMember resultCrewmember) throws PWCGException
	{
    	AARPlayerStatusAdjuster playerStatusAdjuster = new AARPlayerStatusAdjuster(campaign);
        if (SerialNumber.getSerialNumberClassification(resultCrewmember.getSerialNumber()) == SerialNumberClassification.PLAYER)
    	{
        	playerStatusAdjuster.adjustForPlayer(resultCrewmember);
    	}
	}

	private void setCrewMemberWounded(LogCrewMember resultCrewmember) throws PWCGException 
    {        
        List<IAType2> damageForBot = logEventData.getDamageForBot(resultCrewmember.getBotId());
        int woundLevel = crewMemberStatusWoundedEvaluator.getCrewMemberWoundedLevel(damageForBot);
        if (woundLevel == CrewMemberStatus.STATUS_WOUNDED)
        {
            resultCrewmember.setStatus(woundLevel);
        }
        else if (woundLevel == CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED)
        {
            resultCrewmember.setStatus(woundLevel);
        }
    }

    private void setCrewMemberDead(LogTank resultTank, LogCrewMember resultCrewmember) throws PWCGException 
    {        
        IAType3 destroyedEventForPlane = logEventData.getDestroyedEventForPlaneByBot(resultCrewmember.getBotId());
        if (destroyedEventForPlane == null)
        {
            return;
        }
        
        crewMemberStatusDeadEvaluator.initialize(
                        resultCrewmember,
                        destroyedEventForPlane);

        boolean isDead = crewMemberStatusDeadEvaluator.isCrewMemberDead();
        if (isDead)
        {
            resultCrewmember.setStatus(CrewMemberStatus.STATUS_KIA);
        }
    }

    public void setAarVehicleBuilder(AARVehicleBuilder aarVehicleBuilder)
    {
        this.aarVehicleBuilder = aarVehicleBuilder;
    }

    public void setAarCrewMemberStatusDeadEvaluator(AARCrewMemberStatusDeadEvaluator aarCrewMemberStatusDeadEvaluator)
    {
        this.crewMemberStatusDeadEvaluator = aarCrewMemberStatusDeadEvaluator;
    }

    public void setAarCrewMemberStatusWoundedEvaluator(AARCrewMemberStatusWoundedEvaluator aarCrewMemberStatusWoundedEvaluator)
    {
        this.crewMemberStatusWoundedEvaluator = aarCrewMemberStatusWoundedEvaluator;
    }
}

