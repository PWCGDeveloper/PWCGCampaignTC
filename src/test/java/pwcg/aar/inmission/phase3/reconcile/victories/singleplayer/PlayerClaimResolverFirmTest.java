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

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.plane.PwcgRoleCategory;
import pwcg.core.exception.PWCGException;
import pwcg.product.bos.country.BoSCountry;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PlayerClaimResolverFirmTest
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
    public void testPlayerFirmVictoryFound() throws PWCGException
    {
        LogPlane victim = new LogPlane(1);
        victim.setVehicleType("se5a");
        victim.setCountry(new BoSCountry(Country.BRITAIN));

        LogPlane victor = new LogPlane(2);
        victor.setVehicleType("albatrosd5");
        victor.setCrewMemberSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        victor.setCountry(new BoSCountry(Country.GERMANY));

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictim(victim);
        resultVictory.setVictor(victor);
        
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();
        playerDeclaration.setAircraftType("se5a");

        PlayerClaimResolverFirm claimResolverFirm = new PlayerClaimResolverFirm();
        String planeDisplayName = claimResolverFirm.getShotDownPlaneDisplayNameAsFirm(player, playerDeclaration, resultVictory);
        
        Assertions.assertTrue (planeDisplayName.equals("S.E.5a"));
    }

    @Test
    public void testPlayerFirmVictoryNotFoundBecausePlaneMismatch() throws PWCGException
    {
        LogPlane victim = new LogPlane(1);
        victim.setVehicleType("se5a");
        victim.setCountry(new BoSCountry(Country.BRITAIN));

        LogPlane victor = new LogPlane(2);
        victor.setVehicleType("albatrosd5");
        victor.setCrewMemberSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        victor.setCountry(new BoSCountry(Country.GERMANY));

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictim(victim);
        resultVictory.setVictor(victor);
        
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();
        playerDeclaration.setAircraftType("sopcamel");

        PlayerClaimResolverFirm claimResolverFirm = new PlayerClaimResolverFirm();
        String planeDisplayName = claimResolverFirm.getShotDownPlaneDisplayNameAsFirm(player, playerDeclaration, resultVictory);
        
        Assertions.assertTrue (planeDisplayName.isEmpty());
    }

    @Test
    public void testPlayerFirmVictoryNotFoundBecauseVictoryPlaneNotFound() throws PWCGException
    {
        LogPlane victim = new LogPlane(1);
        victim.setVehicleType("notarealplane");
        victim.setCountry(new BoSCountry(Country.BRITAIN));

        LogPlane victor = new LogPlane(2);
        victor.setVehicleType("albatrosd5");
        victor.setCrewMemberSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        victor.setCountry(new BoSCountry(Country.GERMANY));

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictim(victim);
        resultVictory.setVictor(victor);
        
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();
        playerDeclaration.setAircraftType("se5a");

        PlayerClaimResolverFirm claimResolverFirm = new PlayerClaimResolverFirm();
        String planeDisplayName = claimResolverFirm.getShotDownPlaneDisplayNameAsFirm(player, playerDeclaration, resultVictory);
        
        Assertions.assertTrue (planeDisplayName.isEmpty());
    }

    @Test
    public void testPlayerFirmVictoryNotFoundBecauseClaimPlaneNotFound() throws PWCGException
    {
        LogPlane victim = new LogPlane(1);
        victim.setVehicleType("se5a");
        victim.setCountry(new BoSCountry(Country.BRITAIN));

        LogPlane victor = new LogPlane(2);
        victor.setVehicleType("albatrosd5");
        victor.setCrewMemberSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        victor.setCountry(new BoSCountry(Country.GERMANY));

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictim(victim);
        resultVictory.setVictor(victor);
        
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();
        playerDeclaration.setAircraftType("notarealplane");

        PlayerClaimResolverFirm claimResolverFirm = new PlayerClaimResolverFirm();
        String planeDisplayName = claimResolverFirm.getShotDownPlaneDisplayNameAsFirm(player, playerDeclaration, resultVictory);
        
        Assertions.assertTrue (planeDisplayName.isEmpty());
    }

    @Test
    public void testPlayerFirmVictoryNotFoundBecauseVictoryAlreadyConfirmed() throws PWCGException
    {
        LogPlane victim = new LogPlane(1);
        victim.setVehicleType("se5a");
        victim.setCountry(new BoSCountry(Country.BRITAIN));

        LogPlane victor = new LogPlane(2);
        victor.setVehicleType("albatrosd5");
        victor.setCrewMemberSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        victor.setCountry(new BoSCountry(Country.GERMANY));

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictim(victim);
        resultVictory.setVictor(victor);
        resultVictory.setConfirmed(true);
        
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();
        playerDeclaration.setAircraftType("se5a");

        PlayerClaimResolverFirm claimResolverFirm = new PlayerClaimResolverFirm();
        String planeDisplayName = claimResolverFirm.getShotDownPlaneDisplayNameAsFirm(player, playerDeclaration, resultVictory);
        
        Assertions.assertTrue (planeDisplayName.isEmpty());
    }
    
    @Test
    public void testNoFriendlyVictories () throws PWCGException
    {   
        LogPlane victim = new LogPlane(1);
        victim.setRoleCategory(PwcgRoleCategory.FIGHTER);
        victim.setVehicleType("albatrosd5");
        victim.setCountry(new BoSCountry(Country.GERMANY));

        LogPlane victor = new LogPlane(2);
        victor.setVehicleType("albatrosd5");
        victor.setCrewMemberSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        victim.setCountry(new BoSCountry(Country.GERMANY));

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictim(victim);
        resultVictory.setVictor(victor);
        
        PlayerVictoryDeclaration playerDeclaration = new PlayerVictoryDeclaration();
        playerDeclaration.setAircraftType("se5a");

        PlayerClaimResolverFirm claimResolverFirm = new PlayerClaimResolverFirm();
        String planeDisplayName = claimResolverFirm.getShotDownPlaneDisplayNameAsFirm(player, playerDeclaration, resultVictory);
        
        Assertions.assertTrue (planeDisplayName.isEmpty());
    }

}
