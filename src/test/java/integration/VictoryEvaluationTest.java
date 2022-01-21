package integration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pwcg.aar.AARCoordinator;
import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.PlayerDeclarations;
import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.PlayerVictoryDeclaration;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.core.logfiles.event.AType0;
import pwcg.core.logfiles.event.AType12;
import pwcg.core.logfiles.event.AType2;
import pwcg.core.logfiles.event.AType3;
import pwcg.core.utils.DateUtils;
import pwcg.mission.Mission;
import pwcg.mission.MissionBorderBuilder;
import pwcg.mission.MissionHumanParticipants;
import pwcg.mission.io.MissionFileNameBuilder;
import pwcg.mission.platoon.ITankPlatoon;
import pwcg.mission.platoon.tank.TankMcu;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

public class VictoryEvaluationTest
{
    private int ENEMY_START_PLANE_ID = 101000;
    private int GERMAN_START_PLANE_ID = 201000;

    private Campaign campaign;
    
    @BeforeEach
    public void testSetup() throws Exception
    {
        
        campaign = CampaignCache.makeCampaignOnDisk(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);
    }
    
    @Test
    public void testSingleVictoryClaimedAndAwarded() throws Exception
    {
        MissionHumanParticipants participatingPlayers = TestMissionBuilderUtility.buildTestParticipatingHumans(campaign);
        
        MissionBorderBuilder missionBorderBuilder = new MissionBorderBuilder(campaign, participatingPlayers, null);
        CoordinateBox missionBorders = missionBorderBuilder.buildCoordinateBox();

        Mission mission = TestMissionBuilderUtility.createTestMission(campaign, participatingPlayers, missionBorders);
        mission.generate();
        mission.finalizeMission();
        mission.write();

        BufferedWriter writer = makeLogFileWriter();
        int playerAid = buildMissionEntities(writer, mission);

        makeDestroyedEntry(writer, playerAid, ENEMY_START_PLANE_ID+1);
        writer.close();

        PlayerVictoryDeclaration playerVictoryDeclaration1 = new PlayerVictoryDeclaration();
        playerVictoryDeclaration1.setAircraftType("spad13");
        
        PlayerDeclarations playerDeclarations = new PlayerDeclarations();
        playerDeclarations.addDeclaration(playerVictoryDeclaration1);

        CrewMember player = mission.getParticipatingPlayers().getAllParticipatingPlayers().get(0);
        Assertions.assertEquals(0, player.getCrewMemberVictories().getAirVictories().size());

        Map<Integer, PlayerDeclarations> playerDeclarationMap = new HashMap<>();
        playerDeclarationMap.put(player.getSerialNumber(), playerDeclarations);

        AARCoordinator.getInstance().aarPreliminary(campaign);
        AARCoordinator.getInstance().submitAAR(playerDeclarationMap);
    
        CrewMember playerAfter = mission.getParticipatingPlayers().getAllParticipatingPlayers().get(0);
        Assertions.assertEquals(1, playerAfter.getCrewMemberVictories().getAirVictories().size());
    }

    @Test
    public void testTwoVictoriesClaimedAndAwardedWithOneFuzzyVictory() throws Exception
    {
        MissionHumanParticipants participatingPlayers = TestMissionBuilderUtility.buildTestParticipatingHumans(campaign);
        
        MissionBorderBuilder missionBorderBuilder = new MissionBorderBuilder(campaign, participatingPlayers, null);
        CoordinateBox missionBorders = missionBorderBuilder.buildCoordinateBox();

        Mission mission = TestMissionBuilderUtility.createTestMission(campaign, participatingPlayers, missionBorders);
        mission.generate();
        mission.finalizeMission();
        mission.write();

        BufferedWriter writer = makeLogFileWriter();
        int playerAid = buildMissionEntities(writer, mission);

        makeDamageEntry(writer, playerAid, ENEMY_START_PLANE_ID);
        makeDamageEntry(writer, playerAid, ENEMY_START_PLANE_ID+1);

        makeDestroyedEntry(writer, playerAid, ENEMY_START_PLANE_ID);
        makeDestroyedEntry(writer, -1, ENEMY_START_PLANE_ID+1);
        writer.close();

        PlayerVictoryDeclaration playerVictoryDeclaration1 = new PlayerVictoryDeclaration();
        playerVictoryDeclaration1.setAircraftType("sopcamel");

        PlayerVictoryDeclaration playerVictoryDeclaration2 = new PlayerVictoryDeclaration();
        playerVictoryDeclaration2.setAircraftType("sopcamel");
        
        PlayerDeclarations playerDeclarations = new PlayerDeclarations();
        playerDeclarations.addDeclaration(playerVictoryDeclaration1);
        playerDeclarations.addDeclaration(playerVictoryDeclaration2);

        CrewMember player = mission.getParticipatingPlayers().getAllParticipatingPlayers().get(0);
        Assertions.assertEquals(0, player.getCrewMemberVictories().getAirVictories().size());

        Map<Integer, PlayerDeclarations> playerDeclarationMap = new HashMap<>();
        playerDeclarationMap.put(player.getSerialNumber(), playerDeclarations);

        AARCoordinator.getInstance().aarPreliminary(campaign);
        AARCoordinator.getInstance().submitAAR(playerDeclarationMap);
    
        CrewMember playerAfter = mission.getParticipatingPlayers().getAllParticipatingPlayers().get(0);
        Assertions.assertEquals(2, playerAfter.getCrewMemberVictories().getAirVictories().size());
    }

    @Test
    public void testTwoVictoriesClaimedOneAwardedBecauseNoDamage() throws Exception
    {
        MissionHumanParticipants participatingPlayers = TestMissionBuilderUtility.buildTestParticipatingHumans(campaign);

        MissionBorderBuilder missionBorderBuilder = new MissionBorderBuilder(campaign, participatingPlayers, null);
        CoordinateBox missionBorders = missionBorderBuilder.buildCoordinateBox();

        Mission mission = TestMissionBuilderUtility.createTestMission(campaign, participatingPlayers, missionBorders);
        mission.generate();
        mission.finalizeMission();
        mission.write();

        BufferedWriter writer = makeLogFileWriter();
        int playerAid = buildMissionEntities(writer, mission);

        makeDestroyedEntry(writer, playerAid, ENEMY_START_PLANE_ID);
        makeDestroyedEntry(writer, -1, ENEMY_START_PLANE_ID+1);
        writer.close();

        PlayerVictoryDeclaration playerVictoryDeclaration1 = new PlayerVictoryDeclaration();
        playerVictoryDeclaration1.setAircraftType("sopcamel");

        PlayerVictoryDeclaration playerVictoryDeclaration2 = new PlayerVictoryDeclaration();
        playerVictoryDeclaration2.setAircraftType("sopcamel");
        
        PlayerDeclarations playerDeclarations = new PlayerDeclarations();
        playerDeclarations.addDeclaration(playerVictoryDeclaration1);
        playerDeclarations.addDeclaration(playerVictoryDeclaration2);

        CrewMember player = mission.getParticipatingPlayers().getAllParticipatingPlayers().get(0);
        Assertions.assertEquals(0, player.getCrewMemberVictories().getAirVictories().size());

        Map<Integer, PlayerDeclarations> playerDeclarationMap = new HashMap<>();
        playerDeclarationMap.put(player.getSerialNumber(), playerDeclarations);

        AARCoordinator.getInstance().aarPreliminary(campaign);
        AARCoordinator.getInstance().submitAAR(playerDeclarationMap);
    
        CrewMember playerAfter = mission.getParticipatingPlayers().getAllParticipatingPlayers().get(0);
        Assertions.assertEquals(1, playerAfter.getCrewMemberVictories().getAirVictories().size());
    }

    @Test
    public void testNoVictoriesClaimedAndAwarded() throws Exception
    {
        MissionHumanParticipants participatingPlayers = TestMissionBuilderUtility.buildTestParticipatingHumans(campaign);
        
        MissionBorderBuilder missionBorderBuilder = new MissionBorderBuilder(campaign, participatingPlayers, null);
        CoordinateBox missionBorders = missionBorderBuilder.buildCoordinateBox();

        Mission mission = TestMissionBuilderUtility.createTestMission(campaign, participatingPlayers, missionBorders);
        mission.generate();
        mission.finalizeMission();
        mission.write();
        
        List<TankMcu> playerTanks = mission.getUnits().getPlayerUnits().get(0).getTanks();
        Map<Integer, Integer> playerVictories = new HashMap<>();
        for(TankMcu tank : playerTanks)
        {
            playerVictories.put(tank.getTankCommander().getSerialNumber(), tank.getTankCommander().getCrewMemberVictories().getAirVictories().size());
        }


        BufferedWriter writer = makeLogFileWriter();
        int playerAid = buildMissionEntities(writer, mission);

        makeDestroyedEntry(writer, playerAid, ENEMY_START_PLANE_ID+1);
        writer.close();

        PlayerDeclarations playerDeclarations = new PlayerDeclarations();

        CrewMember player = mission.getParticipatingPlayers().getAllParticipatingPlayers().get(0);
        Assertions.assertEquals(0, player.getCrewMemberVictories().getAirVictories().size());

        Map<Integer, PlayerDeclarations> playerDeclarationMap = new HashMap<>();
        playerDeclarationMap.put(player.getSerialNumber(), playerDeclarations);

        AARCoordinator.getInstance().aarPreliminary(campaign);
        AARCoordinator.getInstance().submitAAR(playerDeclarationMap);
    
        CrewMember playerAfter = mission.getParticipatingPlayers().getAllParticipatingPlayers().get(0);
        Assertions.assertEquals(0, playerAfter.getCrewMemberVictories().getAirVictories().size());
        
        boolean wasAwardedToCrewMember = false;
        for (int serialNumber : playerVictories.keySet())
        {
            int numVictoriesBefore = playerVictories.get(serialNumber);
            CrewMember crewMember = campaign.getPersonnelManager().getAnyCampaignMember(serialNumber);
            if (numVictoriesBefore < crewMember.getCrewMemberVictories().getAirVictories().size())
            {
                wasAwardedToCrewMember = true;
            }
        }
        Assertions.assertTrue(wasAwardedToCrewMember);
    }

    private int buildMissionEntities(BufferedWriter writer, Mission mission) throws Exception
    {
        formMissionheader(writer, mission);
        makeEnemyPlanes(writer, mission);
        int playerAid = makeGermanPlanes(writer, mission);
        return playerAid;
    }

    private BufferedWriter makeLogFileWriter() throws Exception
    {
        String missionLogFileName = buildFileName();

        String filePath = "..\\data\\" + missionLogFileName;
        File logFile = new File(filePath);     
        BufferedWriter writer = new BufferedWriter(new FileWriter(logFile));
        return writer;
    }

    private String buildFileName()
    {
        String dateTimeString = DateUtils.getDateStringDashDelimitedYYYYMMDD(new Date());
        dateTimeString += "_12-00-00";
        String filename = "missionReport(" + dateTimeString +  ")[0].txt";
        return filename;
    }

    private void formMissionheader(BufferedWriter writer, Mission mission) throws PWCGException
    {        
        String missionFileName = MissionFileNameBuilder.buildMissionFileName(mission.getCampaign()) + ".mission" ;

        AType0 aType0 = new AType0(mission.getCampaign().getName());
        aType0.setMissionFileName(missionFileName);
        aType0.write(writer);
    }

    private void makeEnemyPlanes(BufferedWriter writer, Mission mission) throws PWCGException
    {       
        Coordinate location = new Coordinate(500000, 0, 50000);

        List<TankMcu> enemyPlanes = findEnemyCrewMembers(mission);
        for (TankMcu enemyPlane : enemyPlanes)
        {
            int tankId =  ENEMY_START_PLANE_ID + enemyPlane.getNumberInFormation() - 1;
            int botId =  tankId +100 ;
            
            AType12 aType12 = new AType12(Integer.valueOf(tankId).toString(), enemyPlane.getType(), enemyPlane.getName(), 
                    enemyPlane.getCountry(), "-1", location);
            aType12.write(writer);

            AType12 aType1Bot = new AType12(Integer.valueOf(botId).toString(), "BotCrewMember_" + enemyPlane.getType(), "BotCrewMember_" + enemyPlane.getType(), 
                    enemyPlane.getCountry(), Integer.valueOf(tankId).toString(), location);
            aType1Bot.write(writer);
        }
    }
    
    private List<TankMcu> findEnemyCrewMembers(Mission mission) throws PWCGException
    {
        for(ITankPlatoon unit : mission.getUnits().getUnitsForSide()(Side.ALLIED))
        {
            List<TankMcu> enemyTanks = unit.getTanks();
            if (enemyTanks.size() >= 2)
            {
                return enemyTanks;                    
            }
        }
        
        throw new PWCGException("No enemy flight large enough");
    }

    private int makeGermanPlanes(BufferedWriter writer, Mission mission) throws PWCGException
    {        
        int playerAid = -1;
        
        Coordinate location = new Coordinate(500000, 0, 50000);
        List<TankMcu> playerFlightPlanes = mission.getFlights().getUnits().get(0).getFlightPlanes().getPlanes();
        
        for (TankMcu friendlyPlane : playerFlightPlanes)
        {
            int tankId =  GERMAN_START_PLANE_ID + friendlyPlane.getNumberInFormation() - 1;
            int botId =  tankId +100;
            
            AType12 aType12 = new AType12(Integer.valueOf(tankId).toString(), friendlyPlane.getType(), friendlyPlane.getName(), 
                    CountryFactory.makeCountryByCountry(Country.GERMANY), "-1", location);
            aType12.write(writer);

            AType12 aType1Bot = new AType12(Integer.valueOf(botId).toString(), "BotCrewMember_" + friendlyPlane.getType(), "BotCrewMember_" + friendlyPlane.getType(), 
                    CountryFactory.makeCountryByCountry(Country.GERMANY), Integer.valueOf(tankId).toString(), location);
            aType1Bot.write(writer);
            
            if (friendlyPlane.isActivePlayerPlane())
            {
                playerAid = tankId;
            }            
        }
        
        return playerAid;
    }

    private void makeDamageEntry(BufferedWriter writer, int shooter, int victim) throws PWCGException
    {        
        Coordinate location = new Coordinate(500000, 0, 50000);
        AType2 aType2 = new AType2(Integer.valueOf(shooter).toString(), Integer.valueOf(victim).toString(), 0.50, location);
        aType2.write(writer);
    }

    private void makeDestroyedEntry(BufferedWriter writer, int victor, int victim) throws PWCGException
    {        
        Coordinate location = new Coordinate(500000, 0, 50000);
        AType3 aType3 = new AType3(Integer.valueOf(victor).toString(), Integer.valueOf(victim).toString(), location);
        aType3.write(writer);
    }
}
