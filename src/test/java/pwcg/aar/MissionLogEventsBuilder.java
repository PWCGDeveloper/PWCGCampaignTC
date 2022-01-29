package pwcg.aar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.aar.prelim.AARPreliminaryData;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.tank.CompanyTankAssignment;
import pwcg.campaign.tank.ITankTypeFactory;
import pwcg.campaign.tank.TankTypeInformation;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.logfiles.LogEventData;
import pwcg.core.logfiles.event.AType12;
import pwcg.core.logfiles.event.AType3;
import pwcg.core.logfiles.event.IAType12;
import pwcg.core.utils.RandomNumberGenerator;

public class MissionLogEventsBuilder
{
    private Campaign campaign;
    private AARPreliminaryData preliminaryData;
    private LogEventData logEventData = new LogEventData();
    private int nextMissionLogId = 400000;
    private Map<Integer, String> serialNumberToPlaneId = new HashMap<>();
    private List<String> destroyedPlanes = new ArrayList<>();
    private CrewMember companyMate;
    private ExpectedResults expectedResults;

    public MissionLogEventsBuilder(Campaign campaign, AARPreliminaryData preliminaryData, ExpectedResults expectedResults)
    {
        this.campaign = campaign;
        this.preliminaryData = preliminaryData;
        this.expectedResults = expectedResults;
    }

    public LogEventData makeLogEvents() throws PWCGException
    {
        makeCrewMembersForVictories();
        makePlayerCompanyTanks();
        makeAiTanks();
        makeTrucks();
        makewaypointEvents();
        makePlayerDestroyedEvents();
        makeOtherDestroyedEvents();
        makeCompanyMateLostEvents();
        makeDamagedEvents();
        makeLandingEvents();
        return logEventData;
    }

    private void makeCrewMembersForVictories() throws PWCGException
    {
        for (CrewMember crewMember : preliminaryData.getCampaignMembersInMission().getCrewMemberCollection().values())
        {
            if (!crewMember.isPlayer())
            {
                companyMate = crewMember;
            }
        }
    }

    private void makePlayerCompanyTanks() throws PWCGException
    {
        for (CrewMember crewMember : preliminaryData.getCampaignMembersInMission().getCrewMemberCollection().values())
        {
            CompanyTankAssignment tankAssignment = AARCoordinatorInMissionTest.getPlaneForCompany(crewMember.getCompanyId());
            ITankTypeFactory tankTypeFactory = PWCGContext.getInstance().getFullTankTypeFactory();
            List<TankTypeInformation> tankTypesForCompany = tankTypeFactory.createActiveTankTypesForArchType(tankAssignment.getArchType(), campaign.getDate());
            int index = RandomNumberGenerator.getRandom(tankTypesForCompany.size());
            TankTypeInformation tankType = tankTypesForCompany.get(index);
            AType12 tankSpawn = new AType12(
                    makeNextId(),
                    tankType.getDisplayName(),
                    crewMember.getNameAndRank(),
                    crewMember.determineCountry(campaign.getDate()),
                    "-1",
                    new Coordinate(500000, 0, 50000));
            
            logEventData.addVehicle(tankSpawn.getId(), tankSpawn);
            serialNumberToPlaneId.put(crewMember.getSerialNumber(), tankSpawn.getId());

            makeBot(tankSpawn);
        }
    }

    private void makeAiTanks() throws PWCGException
    {
        for(int i = 0; i < 4; ++i)
        {
            AType12 tankSpawn = new AType12(
                    makeNextId(),
                    "t34-76stz",
                    "T34 76 STZ",
                    CountryFactory.makeCountryByCountry(Country.RUSSIA),
                    "-1",
                    new Coordinate(500000, 0, 50000));

            logEventData.addVehicle(tankSpawn.getId(), tankSpawn);
        }
        
        for(int i = 0; i < 4; ++i)
        {
            AType12 tankSpawn = new AType12(
                    makeNextId(),
                    "bt7m",
                    "BT-7",
                    CountryFactory.makeCountryByCountry(Country.RUSSIA),
                    "-1",
                    new Coordinate(500000, 0, 50000));

            logEventData.addVehicle(tankSpawn.getId(), tankSpawn);
        }
    }

    private void makeTrucks() throws PWCGException
    {
        ICountry russia = CountryFactory.makeCountryByCountry(Country.RUSSIA);
        for (int i = 0; i < 10; ++i)
        {
            AType12 truckSpawn = new AType12(
                    makeNextId(),
                    "Truck",
                    "",
                    russia,
                    "-1",
                    new Coordinate(500000, 0, 50000));
            
            logEventData.addVehicle(truckSpawn.getId(), truckSpawn);
        }
    }
    
    private void makeBot(AType12 tankSpawn) throws PWCGException
    {
        AType12 botSpawn = new AType12(
                makeNextId(),
                "BotCrewMember",
                tankSpawn.getName(),
                tankSpawn.getCountry(),
                tankSpawn.getId(),
                new Coordinate(500000, 0, 50000));
        
        logEventData.addBot(botSpawn.getId(), botSpawn);
    }

    private void makewaypointEvents()
    {
    }

    private void makePlayerDestroyedEvents() throws PWCGException
    {
        Coordinate crashLocation = new Coordinate(100000, 0, 100000);

        CrewMember player = campaign.getPersonnelManager().getPlayersInMission().getCrewMemberList().get(0);
        String playerPlaneId = serialNumberToPlaneId.get(player.getSerialNumber());
        IAType12 playerPlane = logEventData.getVehicle(playerPlaneId);

        IAType12 playerVictoryPlane1 = getTankVictimByType("t34-76stz");
        AType3 playerVictoryTankEvent1 = new AType3(playerPlane.getId(), playerVictoryPlane1.getId(), crashLocation);

        IAType12 playerVictoryPlane2 = getTankVictimByType("bt7m");
        AType3 playerVictoryTankEvent2 = new AType3(playerPlane.getId(), playerVictoryPlane2.getId(), crashLocation);

        IAType12 playerVictoryVehicle = getGroundVictim();
        AType3 playerVictoryVehicleEvent = new AType3(playerPlane.getId(), playerVictoryVehicle.getId(), crashLocation);

        logEventData.addDestroyedEvent(playerVictoryTankEvent1);
        expectedResults.addPlayerTankVictories();
        
        logEventData.addDestroyedEvent(playerVictoryTankEvent2);
        expectedResults.addPlayerTankVictories();

        logEventData.addDestroyedEvent(playerVictoryVehicleEvent);
        expectedResults.addPlayerGroundVictories();
    }

    private void makeOtherDestroyedEvents() throws PWCGException
    {
        Coordinate crashLocation = new Coordinate(100000, 0, 100000);

        String friendlyCrewMemberPlaneId = serialNumberToPlaneId.get(companyMate.getSerialNumber());
        IAType12 friendlyCrewMemberPlane = logEventData.getVehicle(friendlyCrewMemberPlaneId);
        
        IAType12 enemyPlaneLost = getTankVictimByType("bt7m");
        AType3 enemyPlaneLostEvent = new AType3(friendlyCrewMemberPlane.getId(), enemyPlaneLost.getId(), crashLocation);

        IAType12 enemyVehicleLost = getGroundVictim();
        AType3 enemyVehicleLostEvent = new AType3(friendlyCrewMemberPlane.getId(), enemyVehicleLost.getId(), crashLocation);

        logEventData.addDestroyedEvent(enemyPlaneLostEvent);
        expectedResults.addCrewMemberAirVictories();

        logEventData.addDestroyedEvent(enemyVehicleLostEvent);
        expectedResults.addCrewMemberGroundVictories();
    }

    private void makeCompanyMateLostEvents() throws PWCGException
    {
        Coordinate crashLocation = new Coordinate(100000, 0, 100000);

        String companyMatePlaneId = serialNumberToPlaneId.get(companyMate.getSerialNumber());
        IAType12 companyMatePlane = logEventData.getVehicle(companyMatePlaneId);

        IAType12 enemyVictoryPlane = getTankVictimByType("t34-76stz");
        AType3 companyMatePlaneDestroyedEvent = new AType3(enemyVictoryPlane.getId(), companyMatePlane.getId(), crashLocation);

        logEventData.addDestroyedEvent(companyMatePlaneDestroyedEvent);
    }
    
    private IAType12 getTankVictimByType(String type) throws PWCGException
    {
        for (IAType12 tankSpawn : logEventData.getVehicles())
        {
            if (tankSpawn.getType().equals(type))
            {
                if (!destroyedPlanes.contains(tankSpawn.getId()))
                {
                    destroyedPlanes.add(tankSpawn.getId());
                    killCrewMember(tankSpawn.getId());
                    
                    return tankSpawn;
                }
            }
        }
        
        throw new PWCGException("failed to find spawn for type " + type);
    }
    
    private void killCrewMember(String tankId) throws PWCGException
    {
        Coordinate crashLocation = new Coordinate(100000, 0, 100000);
        for (IAType12 botSpawn : logEventData.getBots())
        {
            if (botSpawn.getPid().equals(tankId))
            {
                AType3 crewMemberKilled = new AType3("-1", botSpawn.getId(), crashLocation);
                logEventData.addDestroyedEvent(crewMemberKilled);
            }
        }
    }

    private IAType12 getGroundVictim() throws PWCGException
    {
        for (IAType12 spawn : logEventData.getVehicles())
        {
            if (spawn.getType().equals("Truck"))
            {
                if (!destroyedPlanes.contains(spawn.getId()))
                {
                    destroyedPlanes.add(spawn.getId());
                    return spawn;
                }
            }
        }
        
        throw new PWCGException("failed to find spawn for truck ");
    }

    private void makeDamagedEvents()
    {
    }

    private void makeLandingEvents()
    {
    }

    private String makeNextId()
    {
        String nextId = Integer.valueOf(nextMissionLogId).toString();
        ++nextMissionLogId;
        return nextId;
    }
}
