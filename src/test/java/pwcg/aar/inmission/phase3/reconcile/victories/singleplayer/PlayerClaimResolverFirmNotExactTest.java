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
public class PlayerClaimResolverFirmNotExactTest
{
    @Mock private CrewMember player;

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        
        Mockito.when(player.isPlayer()).thenReturn(true);
        Mockito.when(player.getSerialNumber()).thenReturn(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        Mockito.when(player.getCountry()).thenReturn(Country.GERMANY);
    }

    @Test
    public void testPlayerFirmNotExactVictoryFoundWithExactMatch() throws PWCGException
    {
        LogTank victim = new LogTank(1);
        victim.setRoleCategory(PwcgRoleCategory.MAIN_BATTLE_TANK);
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

        PlayerClaimResolverFirm claimResolverFirm = new PlayerClaimResolverFirm();
        String planeDisplayName = claimResolverFirm.getDestroyedTankDisplayNameAsFirmNotExact(player, playerDeclaration, resultVictory);
        
        Assertions.assertTrue (planeDisplayName.equals("Sherman"));
    }

    @Test
    public void testPlayerFirmNotExactVictoryFoundWithNotExactMatch() throws PWCGException
    {
        LogTank victim = new LogTank(1);
        victim.setRoleCategory(PwcgRoleCategory.MAIN_BATTLE_TANK);
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

        PlayerClaimResolverFirm claimResolverFirm = new PlayerClaimResolverFirm();
        String planeDisplayName = claimResolverFirm.getDestroyedTankDisplayNameAsFirmNotExact(player, playerDeclaration, resultVictory);
        
        Assertions.assertTrue (planeDisplayName.equals("Sherman"));
    }

    @Test
    public void testPlayerFirmNotExactVictoryFoundWithoutRole() throws PWCGException
    {
        LogTank victim = new LogTank(1);
        victim.setRoleCategory(PwcgRoleCategory.MAIN_BATTLE_TANK);
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

        PlayerClaimResolverFirm claimResolverFirm = new PlayerClaimResolverFirm();
        String planeDisplayName = claimResolverFirm.getDestroyedTankDisplayNameAsFirmNotExact(player, playerDeclaration, resultVictory);
        
        Assertions.assertTrue (planeDisplayName.equals("Sherman"));
    }

    @Test
    public void testPlayerFirmNotExactVictoryNotFoundBecauseClaimPlaneNotFound() throws PWCGException
    {
        LogTank victim = new LogTank(1);
        victim.setRoleCategory(PwcgRoleCategory.FIGHTER);
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

        PlayerClaimResolverFirm claimResolverFirm = new PlayerClaimResolverFirm();
        String planeDisplayName = claimResolverFirm.getDestroyedTankDisplayNameAsFirmNotExact(player, playerDeclaration, resultVictory);
        
        Assertions.assertTrue (planeDisplayName.isEmpty());
    }

    @Test
    public void testPlayerFirmNotExactVictoryNotFoundBecauseVictoryAlreadyConfirmed() throws PWCGException
    {
        LogTank victim = new LogTank(1);
        victim.setRoleCategory(PwcgRoleCategory.FIGHTER);
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

        PlayerClaimResolverFirm claimResolverFirm = new PlayerClaimResolverFirm();
        String planeDisplayName = claimResolverFirm.getDestroyedTankDisplayNameAsFirmNotExact(player, playerDeclaration, resultVictory);
        
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
        playerDeclaration.setTankType("m4a2");

        PlayerClaimResolverFirm claimResolverFirm = new PlayerClaimResolverFirm();
        String planeDisplayName = claimResolverFirm.getDestroyedTankDisplayNameAsFirmNotExact(player, playerDeclaration, resultVictory);
        
        Assertions.assertTrue (planeDisplayName.isEmpty());
    }
}
