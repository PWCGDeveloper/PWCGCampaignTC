package pwcg.aar.inmission.phase2.logeval.victory;

import java.util.ArrayList;
import java.util.List;

import pwcg.aar.inmission.phase2.logeval.AARDestroyedStatusEvaluator;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.core.exception.PWCGException;

/**
 * Evaluate and assign victories
 * The rules:
 * 1. If it was definitely shot down by a known entity, credit that entity
 * 2. If the victor is unknown but the player damaged it, credit the player.
 * 3. If the victor is unknown, if the entity was destroyed near the companys route,
 * mark it for random assignment.
 * 
 * @author Patrick Wilson
 *
 */
public class AARVictoryEvaluator 
{
    private List <LogVictory> victoryResults = new ArrayList <LogVictory>();
    
    private AARDestroyedStatusEvaluator aarDestroyedStatusEvaluator;
    private AARFuzzyVictoryEvaluator fuzzyVictoryEvaluator;
    
    public AARVictoryEvaluator(
    		AARFuzzyVictoryEvaluator fuzzyVictoryEvaluator,
    		AARDestroyedStatusEvaluator aarDestroyedStatusEvaluator)
    {
        this.fuzzyVictoryEvaluator = fuzzyVictoryEvaluator;
        this.aarDestroyedStatusEvaluator = aarDestroyedStatusEvaluator;
    }

    public void evaluateVictories() throws PWCGException
    {
        applyFuzzyVictoryMethods();
        victoryResults.addAll(aarDestroyedStatusEvaluator.getDeadLogVehicleList());
    }
    
    private void applyFuzzyVictoryMethods() throws PWCGException
    {
        for (LogVictory victoryResult : victoryResults)
        {
            fuzzyVictoryEvaluator.applyFuzzyVictoryMethods(victoryResult);
        }
    }

    public List<LogVictory> getVictoryResults()
    {
        return victoryResults;
    }

    public void setVictoryResults(List<LogVictory> victoryResults)
    {
        this.victoryResults = victoryResults;
    }

}

