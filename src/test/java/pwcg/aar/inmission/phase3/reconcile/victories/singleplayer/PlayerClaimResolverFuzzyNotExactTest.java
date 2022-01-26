package pwcg.aar.inmission.phase3.reconcile.victories.singleplayer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTank;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.context.Country;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.tank.PwcgRoleCategory;
import pwcg.core.exception.PWCGException;
import pwcg.product.bos.country.BoSCountry;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PlayerClaimResolverFuzzyNotExactTest
{
    @Mock private CrewMember player;

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        
        Mockito.when(player.getCountry()).thenReturn(Country.GERMANY);
    }

    @Test
    public void testPlayerFuzzyNotExactVictoryFoundWithExactMatch() throws PWCGException
    {
        LogTank victim = new LogTank(1);
        victim.setRoleCategory(PwcgRoleCategory.FIGHTER);
        victim.setVehicleType("se5a");
        victim.setCountry(new BoSCountry(Country.BRITAIN));

        LogTank victor = new LogTank(2);
        victor.setVehicleType("pziv-g");
        victor.setCrewMemberSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        victor.setCountry(new BoSCountry(Country.GERMANY));

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictim(victim);
        resultVictory.setVictor(victor);
        
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();
        playerDeclaration.setTankType("se5a");

        PlayerClaimResolverFuzzy claimResolverFuzzy = new PlayerClaimResolverFuzzy();
        String planeDisplayName = claimResolverFuzzy.getShotDownPlaneDisplayNameAsFuzzyNotExact(player, playerDeclaration, resultVictory);
        
        Assertions.assertTrue (planeDisplayName.equals("S.E.5a"));
    }

    @Test
    public void testPlayerFuzzyNotExactVictoryFoundWithNotExactMatch() throws PWCGException
    {
        LogTank victim = new LogTank(1);
        victim.setRoleCategory(PwcgRoleCategory.FIGHTER);
        victim.setVehicleType("se5a");
        victim.setCountry(new BoSCountry(Country.BRITAIN));

        LogTank victor = new LogTank(2);
        victor.setVehicleType("pziv-g");
        victor.setCrewMemberSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        victor.setCountry(new BoSCountry(Country.GERMANY));

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictim(victim);
        resultVictory.setVictor(victor);
        
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();
        playerDeclaration.setTankType("sopcamel");

        PlayerClaimResolverFuzzy claimResolverFuzzy = new PlayerClaimResolverFuzzy();
        String planeDisplayName = claimResolverFuzzy.getShotDownPlaneDisplayNameAsFuzzyNotExact(player, playerDeclaration, resultVictory);
        
        Assertions.assertTrue (planeDisplayName.equals("S.E.5a"));
    }

    @Test
    public void testPlayerFuzzyNotExactVictoryNotFoundBecauseRoleIsDifferent() throws PWCGException
    {
        LogTank victim = new LogTank(1);
        victim.setRoleCategory(PwcgRoleCategory.BOMBER);
        victim.setVehicleType("notarealplane");

        LogTank victor = new LogTank(2);
        victor.setVehicleType("pziv-g");
        victor.setCrewMemberSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictim(victim);
        resultVictory.setVictor(victor);
        
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();
        playerDeclaration.setTankType("se5a");

        PlayerClaimResolverFuzzy claimResolverFuzzy = new PlayerClaimResolverFuzzy();
        String planeDisplayName = claimResolverFuzzy.getShotDownPlaneDisplayNameAsFuzzyNotExact(player, playerDeclaration, resultVictory);
        
        Assertions.assertTrue (planeDisplayName.isEmpty());
    }

    @Test
    public void testPlayerFuzzyNotExactVictoryNotFoundBecauseClaimPlaneNotFound() throws PWCGException
    {
        LogTank victim = new LogTank(1);
        victim.setRoleCategory(PwcgRoleCategory.FIGHTER);
        victim.setVehicleType("se5a");

        LogTank victor = new LogTank(2);
        victor.setVehicleType("pziv-g");
        victor.setCrewMemberSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictim(victim);
        resultVictory.setVictor(victor);
        
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();
        playerDeclaration.setTankType("notarealplane");

        PlayerClaimResolverFuzzy claimResolverFuzzy = new PlayerClaimResolverFuzzy();
        String planeDisplayName = claimResolverFuzzy.getShotDownPlaneDisplayNameAsFuzzyNotExact(player, playerDeclaration, resultVictory);
        
        Assertions.assertTrue (planeDisplayName.isEmpty());
    }

    @Test
    public void testPlayerFuzzyNotExactVictoryNotFoundBecauseVictoryAlreadyConfirmed() throws PWCGException
    {
        LogTank victim = new LogTank(1);
        victim.setRoleCategory(PwcgRoleCategory.FIGHTER);
        victim.setVehicleType("se5a");

        LogTank victor = new LogTank(2);
        victor.setVehicleType("pziv-g");
        victor.setCrewMemberSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictim(victim);
        resultVictory.setVictor(victor);
        resultVictory.setConfirmed(true);
        
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();
        playerDeclaration.setTankType("se5a");

        PlayerClaimResolverFuzzy claimResolverFuzzy = new PlayerClaimResolverFuzzy();
        String planeDisplayName = claimResolverFuzzy.getShotDownPlaneDisplayNameAsFuzzyNotExact(player, playerDeclaration, resultVictory);
        
        Assertions.assertTrue (planeDisplayName.isEmpty());
    }
    
    
    @Test
    public void testNoFriendlyVictories () throws PWCGException
    {   
        LogTank victim = new LogTank(1);
        victim.setRoleCategory(PwcgRoleCategory.FIGHTER);
        victim.setVehicleType("pziv-g");
        victim.setCountry(new BoSCountry(Country.GERMANY));

        LogTank victor = new LogTank(2);
        victor.setVehicleType("pziv-g");
        victor.setCrewMemberSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        victim.setCountry(new BoSCountry(Country.GERMANY));

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictim(victim);
        resultVictory.setVictor(victor);
        
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();
        playerDeclaration.setTankType("se5a");

        PlayerClaimResolverFuzzy claimResolverFuzzy = new PlayerClaimResolverFuzzy();
        String planeDisplayName = claimResolverFuzzy.getShotDownPlaneDisplayNameAsFuzzyNotExact(player, playerDeclaration, resultVictory);
        
        Assertions.assertTrue (planeDisplayName.isEmpty());
    }

}
