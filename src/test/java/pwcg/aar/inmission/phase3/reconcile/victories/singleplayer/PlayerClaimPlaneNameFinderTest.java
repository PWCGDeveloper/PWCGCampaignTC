package pwcg.aar.inmission.phase3.reconcile.victories.singleplayer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTank;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.core.exception.PWCGException;

@ExtendWith(MockitoExtension.class)
public class PlayerClaimPlaneNameFinderTest
{
    public PlayerClaimPlaneNameFinderTest() throws PWCGException
    {
        
    }

    @Test
    public void testPlaneNameFoundByVictoryResult() throws PWCGException
    {
        LogTank victim = new LogTank(1);
        victim.setVehicleType("m4a2");

        LogVictory victory = new LogVictory(2);
        victory.setVictim(victim);
        
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();

        PlayerClaimPlaneNameFinder planeNameFinder = new PlayerClaimPlaneNameFinder();
        String planeDisplayName = planeNameFinder.getShotDownPlaneDisplayName(playerDeclaration, victory);
        
        Assertions.assertTrue (planeDisplayName.equals("Sherman"));
    }

    @Test
    public void testPlaneNameFoundByDeclaration() throws PWCGException
    {
        LogVictory victory = new LogVictory(1);
         
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();
        playerDeclaration.setTankType("m4a2");

        PlayerClaimPlaneNameFinder planeNameFinder = new PlayerClaimPlaneNameFinder();
        String planeDisplayName = planeNameFinder.getShotDownPlaneDisplayName(playerDeclaration, victory);
        
        Assertions.assertTrue (planeDisplayName.equals("Sherman"));
    }

    @Test
    public void testPlaneNameNotFound() throws PWCGException
    {
        LogVictory victory = new LogVictory(1);
         
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();

        PlayerClaimPlaneNameFinder planeNameFinder = new PlayerClaimPlaneNameFinder();
        String planeDisplayName = planeNameFinder.getShotDownPlaneDisplayName(playerDeclaration, victory);
        
        Assertions.assertTrue (planeDisplayName.isEmpty());
    }

}
