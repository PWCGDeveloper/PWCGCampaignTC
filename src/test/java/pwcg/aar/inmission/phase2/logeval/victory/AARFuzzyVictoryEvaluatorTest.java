package pwcg.aar.inmission.phase2.logeval.victory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.inmission.phase2.logeval.AARVehicleBuilder;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTank;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogUnknown;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.core.exception.PWCGException;

@ExtendWith(MockitoExtension.class)
public class AARFuzzyVictoryEvaluatorTest
{
    @Mock
    private AARVehicleBuilder vehicleBuilder;

    @Mock
    private AARFuzzyByAccumulatedDamaged fuzzyByPlayerDamaged;
    
    @Mock
    private AARRandomAssignment randomAssignment;
    
    private LogVictory victoryResult;
    
    @Mock
    private LogTank victor;
    
    private LogTank victim;
    
    private LogUnknown unknownVictorEntity = new LogUnknown();
    
    @BeforeEach
    public void setupTest()
    {
        victoryResult = new LogVictory(10);
        victim = new LogTank(1);
    }
    
    @Test
    public void testUseFuzzyVictoryTrue () throws PWCGException
    {
        victoryResult.setVictim(new LogTank(2));
        victim.setId("11111");
        Mockito.when(vehicleBuilder.getVehicle(ArgumentMatchers.<String>any())).thenReturn(victim);
        Mockito.when(randomAssignment.markForRandomAssignment(victoryResult)).thenReturn(unknownVictorEntity);
        unknownVictorEntity.setId("11111");
        
        AARFuzzyVictoryEvaluator fuzzyVictoryEvaluator= new AARFuzzyVictoryEvaluator(
                        vehicleBuilder, 
                        fuzzyByPlayerDamaged,
                        randomAssignment);
        
        fuzzyVictoryEvaluator.applyFuzzyVictoryMethods(victoryResult);
        
        assert(victoryResult.getVictor().getId() == "11111");
    }
    
    @Test
    public void testUseFuzzyVictoryFalseBecauseNullVictim () throws PWCGException
    {
        victoryResult.setVictim(null);
        victim.setId("11111");
        unknownVictorEntity.setId("11111");
        
        AARFuzzyVictoryEvaluator fuzzyVictoryEvaluator= new AARFuzzyVictoryEvaluator(
                        vehicleBuilder, 
                        fuzzyByPlayerDamaged,
                        randomAssignment);
        
        fuzzyVictoryEvaluator.applyFuzzyVictoryMethods(victoryResult);
        
        assert(victoryResult.getVictor() instanceof LogUnknown);
    }
    
    @Test
    public void testUseFuzzyVictoryFalseBecauseUnknownVictor () throws PWCGException
    {
        victoryResult.setVictim(null);
        victim.setId("11111");
        unknownVictorEntity.setId("11111");
       
        AARFuzzyVictoryEvaluator fuzzyVictoryEvaluator= new AARFuzzyVictoryEvaluator(
                        vehicleBuilder, 
                        fuzzyByPlayerDamaged,
                        randomAssignment);
        
        fuzzyVictoryEvaluator.applyFuzzyVictoryMethods(victoryResult);
        
        assert(victoryResult.getVictor() instanceof LogUnknown);
    }
    
    @Test
    public void testUseFuzzyVictoryFalseBecauseNotNullVictor () throws PWCGException
    {
        victoryResult.setVictor(new LogTank(3));
        victoryResult.setVictim(new LogTank(4));
        victim.setId("11111");
        unknownVictorEntity.setId("11111");

        Mockito.when(vehicleBuilder.getVehicle(ArgumentMatchers.<String>any())).thenReturn(victim);
        
        AARFuzzyVictoryEvaluator fuzzyVictoryEvaluator= new AARFuzzyVictoryEvaluator(
                        vehicleBuilder, 
                        fuzzyByPlayerDamaged,
                        randomAssignment);
        
        fuzzyVictoryEvaluator.applyFuzzyVictoryMethods(victoryResult);
        
        assert(victoryResult.getVictor().getId() == "");
    }
    
    @Test
    public void testUseFuzzyVictoryFalseBecauseVehicleBuilderCouldNotIdentifyVictim () throws PWCGException
    {
        victoryResult.setVictim(new LogTank(5));
        victim.setId("11111");
        Mockito.when(vehicleBuilder.getVehicle(ArgumentMatchers.<String>any())).thenReturn(null);
        unknownVictorEntity.setId("11111");
        
        AARFuzzyVictoryEvaluator fuzzyVictoryEvaluator= new AARFuzzyVictoryEvaluator(
                        vehicleBuilder, 
                        fuzzyByPlayerDamaged,
                        randomAssignment);
        
        fuzzyVictoryEvaluator.applyFuzzyVictoryMethods(victoryResult);
        
        assert(victoryResult.getVictor() instanceof LogUnknown);
    }
    
}
