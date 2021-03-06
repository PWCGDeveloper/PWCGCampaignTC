package pwcg.aar.inmission.phase2.logeval.victory;

import pwcg.aar.inmission.phase2.logeval.AARVehicleBuilder;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogAIEntity;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogUnknown;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.core.exception.PWCGException;

public class AARFuzzyVictoryEvaluator 
{
    private AARVehicleBuilder vehicleBuilder;
    private AARFuzzyByAccumulatedDamaged fuzzyByPlayerDamaged;
    private AARRandomAssignment randomAssignment;
    
    public AARFuzzyVictoryEvaluator(
                    AARVehicleBuilder vehicleBuilder, 
                    AARFuzzyByAccumulatedDamaged fuzzyByPlayerDamaged,
                    AARRandomAssignment randomAssignment)
    {
        this.vehicleBuilder = vehicleBuilder;
        this.fuzzyByPlayerDamaged = fuzzyByPlayerDamaged;
        this.randomAssignment = randomAssignment;
    }

    public void applyFuzzyVictoryMethods(LogVictory victoryResult) throws PWCGException 
    {
        if (shouldFuzzyVictoryMethodsBeUsed(victoryResult))
        {
            LogAIEntity victor = fuzzyByPlayerDamaged.getVictorBasedOnDamage(victoryResult);
            if (victor == null)
            {
                LogAIEntity randomAssignmentEntity = randomAssignment.markForRandomAssignment(victoryResult);
                if (randomAssignmentEntity != null)
                {
                    victoryResult.setVictor(randomAssignmentEntity);
                }
            }
        }
    }

    private boolean shouldFuzzyVictoryMethodsBeUsed (LogVictory victoryResult) throws PWCGException
    {
        boolean hasVictor = victorIsAssigned(victoryResult);
        boolean hasVictim = victimIsAssigned(victoryResult);
        if (!hasVictor && hasVictim)
        {
            return true;
        }
        
        return false;
    }

    private boolean victorIsAssigned(LogVictory victoryResult)
    {
        LogAIEntity victor = victoryResult.getVictor();
        if (victor == null || (victor instanceof LogUnknown))
        {
            return false;
        }
        
        return true;
    }

    private boolean victimIsAssigned(LogVictory victoryResult) throws PWCGException
    {
        LogAIEntity victim = victoryResult.getVictim();
        if (victim == null)
        {
            return false;
        }
        
        LogAIEntity victimEntity = vehicleBuilder.getVehicle(victim.getId());                
        if (victimEntity == null)
        {
            return false;
        }
        
        return true;
    }
}

