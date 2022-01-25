package pwcg.aar.outofmission.phase1.elapsedtime;

import java.util.List;
import java.util.Map;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.AAREquipmentLosses;
import pwcg.aar.data.AARPersonnelLosses;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTank;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;

public class OutOfMissionLossHandler
{
    private Campaign campaign;
    private AARContext aarContext;
    private AARPersonnelLosses outOfMissionPersonnelLosses = new AARPersonnelLosses();
    private AAREquipmentLosses outOfMissionEquipmentLosses = new AAREquipmentLosses();

    public OutOfMissionLossHandler(Campaign campaign, AARContext aarContext)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
    }

    public void lossesOutOfMission(Map<Integer, CrewMember> shotDownCrewMembers, Map<Integer, LogTank> destroyedTanks) throws PWCGException
    {
        calculateHistoricalAceLosses();
        calculateDestroyedPersonnelLosses(shotDownCrewMembers);
        calculateDestroyedEquipmentLosses(destroyedTanks);
    }

    private void calculateHistoricalAceLosses() throws PWCGException, PWCGException
    {
        OutOfMissionAceLossCalculator aceLossHandler = new OutOfMissionAceLossCalculator(campaign, aarContext);
        List<CrewMember> acesKilled = aceLossHandler.acesKilledHistorically();
        for (CrewMember deadAce : acesKilled)
        {
            outOfMissionPersonnelLosses.addPersonnelKilled(deadAce);
        }
    }    

    private void calculateDestroyedPersonnelLosses(Map<Integer, CrewMember> shotDownCrewMembers) throws PWCGException
    {
        PersonnelOutOfMissionStatusHandler personnelOutOfMissionHandler = new PersonnelOutOfMissionStatusHandler();
        AARPersonnelLosses outOfMissionPersonnelLossesForTheDay = personnelOutOfMissionHandler.determineFateOfCrewMembers(shotDownCrewMembers);        
        outOfMissionPersonnelLosses.merge(outOfMissionPersonnelLossesForTheDay);
    }

    private void calculateDestroyedEquipmentLosses(Map<Integer, LogTank> destroyedTanks)
    {
        AAREquipmentLosses equipmentLosses = new AAREquipmentLosses();
        for (LogTank tankDestroyed : destroyedTanks.values())
        {
            if (tankDestroyed.isEquippedTank())
            {
                equipmentLosses.addTankDestroyed(tankDestroyed);
            }
        }
        outOfMissionEquipmentLosses.merge(equipmentLosses);
    }

	public AARPersonnelLosses getOutOfMissionPersonnelLosses()
	{
		return outOfMissionPersonnelLosses;
	}

    public AAREquipmentLosses getOutOfMissionEquipmentLosses()
    {
        return outOfMissionEquipmentLosses;
    }
}
