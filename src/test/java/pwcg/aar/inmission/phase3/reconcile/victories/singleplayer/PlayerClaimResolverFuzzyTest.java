package pwcg.aar.inmission.phase3.reconcile.victories.singleplayer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTank;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.context.Country;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.core.exception.PWCGException;
import pwcg.product.bos.country.BoSCountry;

@ExtendWith(MockitoExtension.class)
public class PlayerClaimResolverFuzzyTest
{
    public PlayerClaimResolverFuzzyTest() throws PWCGException
    {
        
    }

    @Test
    public void testPlayerFuzzyVictoryFound() throws PWCGException
    {
        LogTank victim = new LogTank(1);
        victim.setVehicleType("m4a2");
        victim.setCountry(new BoSCountry(Country.BRITAIN));

        LogTank victor = new LogTank(2);
        victor.setVehicleType("pziv-g");
        victor.setCrewMemberSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        victor.setCountry(new BoSCountry(Country.GERMANY));

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictim(victim);
        resultVictory.setVictor(victor);
        
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();
        playerDeclaration.setTankType("m4a2");

        PlayerClaimResolverFuzzy claimResolverFuzzy = new PlayerClaimResolverFuzzy();
        String planeDisplayName = claimResolverFuzzy.getDestroyedTankDisplayNameAsFuzzy(playerDeclaration, resultVictory);
        
        Assertions.assertTrue (planeDisplayName.equals("Sherman"));
    }
    

    @Test
    public void testPlayerFuzzyVictoryNotFoundBecausePlaneMismatch() throws PWCGException
    {
        LogTank victim = new LogTank(1);
        victim.setVehicleType("m4a2");
        victim.setCountry(new BoSCountry(Country.BRITAIN));

        LogTank victor = new LogTank(2);
        victor.setVehicleType("pziv-g");
        victor.setCrewMemberSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        victor.setCountry(new BoSCountry(Country.GERMANY));

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictim(victim);
        resultVictory.setVictor(victor);
        
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();
        playerDeclaration.setTankType("t34-76-43");

        PlayerClaimResolverFuzzy claimResolverFuzzy = new PlayerClaimResolverFuzzy();
        String planeDisplayName = claimResolverFuzzy.getDestroyedTankDisplayNameAsFuzzy(playerDeclaration, resultVictory);
        
        Assertions.assertTrue (planeDisplayName.isEmpty());
    }

    @Test
    public void testPlayerFuzzyVictoryNotFoundBecauseVictoryPlaneNotFound() throws PWCGException
    {
        LogTank victim = new LogTank(1);
        victim.setVehicleType("notarealplane");
        victim.setCountry(new BoSCountry(Country.BRITAIN));

        LogTank victor = new LogTank(2);
        victor.setVehicleType("pziv-g");
        victor.setCrewMemberSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        victor.setCountry(new BoSCountry(Country.GERMANY));

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictim(victim);
        resultVictory.setVictor(victor);
        
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();
        playerDeclaration.setTankType("m4a2");

        PlayerClaimResolverFuzzy claimResolverFuzzy = new PlayerClaimResolverFuzzy();
        String planeDisplayName = claimResolverFuzzy.getDestroyedTankDisplayNameAsFuzzy(playerDeclaration, resultVictory);
        
        Assertions.assertTrue (planeDisplayName.isEmpty());
    }

    @Test
    public void testPlayerFuzzyVictoryNotFoundBecauseClaimPlaneNotFound() throws PWCGException
    {
        LogTank victim = new LogTank(1);
        victim.setVehicleType("m4a2");
        victim.setCountry(new BoSCountry(Country.BRITAIN));

        LogTank victor = new LogTank(2);
        victor.setVehicleType("pziv-g");
        victor.setCrewMemberSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        victor.setCountry(new BoSCountry(Country.GERMANY));

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictim(victim);
        resultVictory.setVictor(victor);
        
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();
        playerDeclaration.setTankType("notarealplane");

        PlayerClaimResolverFuzzy claimResolverFuzzy = new PlayerClaimResolverFuzzy();
        String planeDisplayName = claimResolverFuzzy.getDestroyedTankDisplayNameAsFuzzy(playerDeclaration, resultVictory);
        
        Assertions.assertTrue (planeDisplayName.isEmpty());
    }

    @Test
    public void testPlayerFuzzyVictoryNotFoundBecauseVictoryAlreadyConfirmed() throws PWCGException
    {
        LogTank victim = new LogTank(1);
        victim.setVehicleType("m4a2");
        victim.setCountry(new BoSCountry(Country.BRITAIN));

        LogTank victor = new LogTank(2);
        victor.setVehicleType("pziv-g");
        victor.setCrewMemberSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        victor.setCountry(new BoSCountry(Country.GERMANY));

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictim(victim);
        resultVictory.setVictor(victor);
        resultVictory.setConfirmed(true);
        
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();
        playerDeclaration.setTankType("m4a2");

        PlayerClaimResolverFuzzy claimResolverFuzzy = new PlayerClaimResolverFuzzy();
        String planeDisplayName = claimResolverFuzzy.getDestroyedTankDisplayNameAsFuzzy(playerDeclaration, resultVictory);
        
        Assertions.assertTrue (planeDisplayName.isEmpty());
    }

    
    @Test
    public void testNoFriendlyVictories () throws PWCGException
    {   
        LogTank victim = new LogTank(1);
        victim.setVehicleType("pziv-g");
        victim.setCountry(new BoSCountry(Country.GERMANY));

        LogTank victor = new LogTank(2);
        victor.setVehicleType("pziv-g");
        victor.setCrewMemberSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        victor.setCountry(new BoSCountry(Country.GERMANY));

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictim(victim);
        resultVictory.setVictor(victor);

        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();
        playerDeclaration.setTankType("m4a2");

        PlayerClaimResolverFuzzy claimResolverFuzzy = new PlayerClaimResolverFuzzy();
        String planeDisplayName = claimResolverFuzzy.getDestroyedTankDisplayNameAsFuzzy(playerDeclaration, resultVictory);
        
        Assertions.assertTrue (planeDisplayName.isEmpty());
    }

}
