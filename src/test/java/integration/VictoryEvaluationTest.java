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
    private int ENEMY_START_TANK_ID = 101000;
    private int GERMAN_START_TANK_ID = 201000;

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
        int playerTankAid = buildMissionEntities(writer, mission);

        makeDestroyedEntry(writer, playerTankAid, ENEMY_START_TANK_ID+1);
        writer.close();

        PlayerVictoryDeclaration playerVictoryDeclaration1 = new PlayerVictoryDeclaration();
        playerVictoryDeclaration1.setTankType("kv1s");
        
        PlayerDeclarations playerDeclarations = new PlayerDeclarations();
        playerDeclarations.addDeclaration(playerVictoryDeclaration1);

        CrewMember player = mission.getParticipatingPlayers().getAllParticipatingPlayers().get(0);
        Assertions.assertEquals(0, player.getCrewMemberVictories().getTankVictoryCount());

        Map<Integer, PlayerDeclarations> playerDeclarationMap = new HashMap<>();
        playerDeclarationMap.put(player.getSerialNumber(), playerDeclarations);

        AARCoordinator.getInstance().aarPreliminary(campaign);
        AARCoordinator.getInstance().submitAAR(playerDeclarationMap);
    
        CrewMember playerAfter = mission.getParticipatingPlayers().getAllParticipatingPlayers().get(0);
        Assertions.assertEquals(1, playerAfter.getCrewMemberVictories().getTankVictoryCount());
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

        makeDamageEntry(writer, playerAid, ENEMY_START_TANK_ID);
        makeDamageEntry(writer, playerAid, ENEMY_START_TANK_ID+1);

        makeDestroyedEntry(writer, playerAid, ENEMY_START_TANK_ID);
        makeDestroyedEntry(writer, -1, ENEMY_START_TANK_ID+1);
        writer.close();

        PlayerVictoryDeclaration playerVictoryDeclaration1 = new PlayerVictoryDeclaration();
        playerVictoryDeclaration1.setTankType("kv1s");

        PlayerVictoryDeclaration playerVictoryDeclaration2 = new PlayerVictoryDeclaration();
        playerVictoryDeclaration2.setTankType("kv1s");
        
        PlayerDeclarations playerDeclarations = new PlayerDeclarations();
        playerDeclarations.addDeclaration(playerVictoryDeclaration1);
        playerDeclarations.addDeclaration(playerVictoryDeclaration2);

        CrewMember player = mission.getParticipatingPlayers().getAllParticipatingPlayers().get(0);
        Assertions.assertEquals(0, player.getCrewMemberVictories().getTankVictoryCount());

        Map<Integer, PlayerDeclarations> playerDeclarationMap = new HashMap<>();
        playerDeclarationMap.put(player.getSerialNumber(), playerDeclarations);

        AARCoordinator.getInstance().aarPreliminary(campaign);
        AARCoordinator.getInstance().submitAAR(playerDeclarationMap);
    
        CrewMember playerAfter = mission.getParticipatingPlayers().getAllParticipatingPlayers().get(0);
        Assertions.assertEquals(2, playerAfter.getCrewMemberVictories().getTankVictoryCount());
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

        makeDestroyedEntry(writer, playerAid, ENEMY_START_TANK_ID);
        makeDestroyedEntry(writer, -1, ENEMY_START_TANK_ID+1);
        writer.close();

        PlayerVictoryDeclaration playerVictoryDeclaration1 = new PlayerVictoryDeclaration();
        playerVictoryDeclaration1.setTankType("kv1s");

        PlayerVictoryDeclaration playerVictoryDeclaration2 = new PlayerVictoryDeclaration();
        playerVictoryDeclaration2.setTankType("kv1s");
        
        PlayerDeclarations playerDeclarations = new PlayerDeclarations();
        playerDeclarations.addDeclaration(playerVictoryDeclaration1);
        playerDeclarations.addDeclaration(playerVictoryDeclaration2);

        CrewMember player = mission.getParticipatingPlayers().getAllParticipatingPlayers().get(0);
        Assertions.assertEquals(0, player.getCrewMemberVictories().getTankVictoryCount());

        Map<Integer, PlayerDeclarations> playerDeclarationMap = new HashMap<>();
        playerDeclarationMap.put(player.getSerialNumber(), playerDeclarations);

        AARCoordinator.getInstance().aarPreliminary(campaign);
        AARCoordinator.getInstance().submitAAR(playerDeclarationMap);
    
        CrewMember playerAfter = mission.getParticipatingPlayers().getAllParticipatingPlayers().get(0);
        Assertions.assertEquals(1, playerAfter.getCrewMemberVictories().getTankVictoryCount());
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
        
        List<TankMcu> playerTanks = mission.getPlatoons().getPlayerPlatoons().get(0).getTanks();
        Map<Integer, Integer> playerVictories = new HashMap<>();
        for(TankMcu tank : playerTanks)
        {
            playerVictories.put(tank.getTankCommander().getSerialNumber(), tank.getTankCommander().getCrewMemberVictories().getTankVictoryCount());
        }


        BufferedWriter writer = makeLogFileWriter();
        int playerAid = buildMissionEntities(writer, mission);

        makeDestroyedEntry(writer, playerAid, ENEMY_START_TANK_ID+1);
        writer.close();

        PlayerDeclarations playerDeclarations = new PlayerDeclarations();

        CrewMember player = mission.getParticipatingPlayers().getAllParticipatingPlayers().get(0);
        Assertions.assertEquals(0, player.getCrewMemberVictories().getTankVictoryCount());

        Map<Integer, PlayerDeclarations> playerDeclarationMap = new HashMap<>();
        playerDeclarationMap.put(player.getSerialNumber(), playerDeclarations);

        AARCoordinator.getInstance().aarPreliminary(campaign);
        AARCoordinator.getInstance().submitAAR(playerDeclarationMap);
    
        CrewMember playerAfter = mission.getParticipatingPlayers().getAllParticipatingPlayers().get(0);
        Assertions.assertEquals(0, playerAfter.getCrewMemberVictories().getTankVictoryCount());
        
        boolean wasAwardedToCrewMember = false;
        for (int serialNumber : playerVictories.keySet())
        {
            int numVictoriesBefore = playerVictories.get(serialNumber);
            CrewMember crewMember = campaign.getPersonnelManager().getAnyCampaignMember(serialNumber);
            if (numVictoriesBefore < crewMember.getCrewMemberVictories().getTankVictoryCount())
            {
                wasAwardedToCrewMember = true;
            }
        }
        Assertions.assertTrue(wasAwardedToCrewMember);
    }

    private int buildMissionEntities(BufferedWriter writer, Mission mission) throws Exception
    {
        formMissionheader(writer, mission);
        makeEnemyTanks(writer, mission);
        int playerTankAid = makeGermanTanks(writer, mission);
        return playerTankAid;
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

    private void makeEnemyTanks(BufferedWriter writer, Mission mission) throws PWCGException
    {       
        Coordinate location = new Coordinate(500000, 0, 50000);

        List<TankMcu> enemyTanks = findEnemyCrewMembers(mission);
        for (TankMcu enemyTank : enemyTanks)
        {
            int tankId =  ENEMY_START_TANK_ID + enemyTank.getNumberInFormation() - 1;
            int botId =  tankId +100 ;
            
            AType12 aType12 = new AType12(Integer.valueOf(tankId).toString(), enemyTank.getType(), enemyTank.getName(), 
                    enemyTank.getCountry(), "-1", location);
            aType12.write(writer);

            AType12 aType1Bot = new AType12(Integer.valueOf(botId).toString(), "BotCrewMember_" + enemyTank.getType(), "BotCrewMember_" + enemyTank.getType(), 
                    enemyTank.getCountry(), Integer.valueOf(tankId).toString(), location);
            aType1Bot.write(writer);
        }
    }
    
    private List<TankMcu> findEnemyCrewMembers(Mission mission) throws PWCGException
    {
        for(ITankPlatoon unit : mission.getPlatoons().getPlatoonsForSide(Side.ALLIED))
        {
            List<TankMcu> enemyTanks = unit.getTanks();
            if (enemyTanks.size() >= 2)
            {
                return enemyTanks;                    
            }
        }
        
        throw new PWCGException("No enemy platoon large enough");
    }

    private int makeGermanTanks(BufferedWriter writer, Mission mission) throws PWCGException
    {        
        int playerTankAid = -1;
        
        Coordinate location = new Coordinate(500000, 0, 50000);
        List<TankMcu> playerPlatoonTanks = mission.getPlatoons().getPlayerPlatoons().get(0).getTanks();
        
        for (TankMcu friendlyTank : playerPlatoonTanks)
        {
            int tankId =  GERMAN_START_TANK_ID + friendlyTank.getNumberInFormation() - 1;
            int botId =  tankId +100;
            
            AType12 aType12 = new AType12(Integer.valueOf(tankId).toString(), friendlyTank.getType(), friendlyTank.getName(), 
                    CountryFactory.makeCountryByCountry(Country.GERMANY), "-1", location);
            aType12.write(writer);

            AType12 aType1Bot = new AType12(Integer.valueOf(botId).toString(), "BotCrewMember_" + friendlyTank.getType(), "BotCrewMember_" + friendlyTank.getType(), 
                    CountryFactory.makeCountryByCountry(Country.GERMANY), Integer.valueOf(tankId).toString(), location);
            aType1Bot.write(writer);
            
            if (friendlyTank.isActivePlayerTank(campaign))
            {
                playerTankAid = tankId;
            }            
        }
        
        return playerTankAid;
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
