package pwcg.aar.inmission.phase2.logeval;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogCrewMember;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTank;
import pwcg.campaign.context.Country;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.LogEventData;
import pwcg.core.logfiles.event.AType12;
import pwcg.core.logfiles.event.IAType12;
import pwcg.mission.data.PwcgGeneratedMissionVehicleData;
import pwcg.testutils.TestATypeFactory;

public class TestMissionEntityGenerator
{
    private LogEventData logEventData;    
    private List<IAType12> vehicles = new ArrayList<>();
    private Map<String, IAType12> crewMemberBots = new HashMap<>();
    private Map<Integer, PwcgGeneratedMissionVehicleData> missionTanks = new HashMap<>();
    private Map <String, LogTank> tankAiEntities = new HashMap<>();

    public void makeMissionArtifacts(
                    int numRussianPlanes, 
                    int numGermanPlanes,
                    int numRussianTrucks, 
                    int numGermanTrucks) throws PWCGException
    {
        String[] alliedCrewMembers = new String[] { "Russian CrewMemberA","Russian CrewMemberB"};
        String[] germanCrewMembers = new String[] { "German CrewMemberA","German CrewMemberB"};
        Integer[] alliedCrewMembersSerialNumbers= new Integer[] { SerialNumber.AI_STARTING_SERIAL_NUMBER + 1, SerialNumber.AI_STARTING_SERIAL_NUMBER + 2};
        Integer[] alliedPlaneSerialNumbers= new Integer[] { SerialNumber.TANK_STARTING_SERIAL_NUMBER + 1, SerialNumber.TANK_STARTING_SERIAL_NUMBER + 2};
        Integer[] germanCrewMemberSerialNumbers = new Integer[] { SerialNumber.AI_STARTING_SERIAL_NUMBER + 100, SerialNumber.AI_STARTING_SERIAL_NUMBER + 200};
        Integer[] germanPlaneSerialNumbers= new Integer[] { SerialNumber.TANK_STARTING_SERIAL_NUMBER + 100, SerialNumber.TANK_STARTING_SERIAL_NUMBER + 200};
        String[] alliedCrewMemberBotId = new String[] { "1001","1002"};
        String[] germanCrewMemberBotId = new String[] { "2001","2002"};

        makeRussianTanks(numRussianPlanes, alliedCrewMembersSerialNumbers, alliedPlaneSerialNumbers, alliedCrewMembers, alliedCrewMemberBotId);
        makeGermanTanks(numGermanPlanes, germanCrewMemberSerialNumbers, germanPlaneSerialNumbers, germanCrewMembers, germanCrewMemberBotId);
        makeRussianTrucks(numRussianTrucks);
        makeGermanTrucks(numGermanTrucks);
        
        formAARLogParser();
    }

    private void formAARLogParser()
    {
        logEventData = new LogEventData();
        logEventData.setBots(crewMemberBots);
    }

    private void makeRussianTanks(int numRussianPlanes, Integer[] alliedCrewMembersSerialNumbers, Integer[] alliedPlaneSerialNumbers, String[] alliedCrewMembers, String[] alliedCrewMemberBotId) throws PWCGException
    {
        for (int i = 0; i < numRussianPlanes; ++i)
        {            
            AType12 alliedPlane  = TestATypeFactory.makeRussianTank(alliedCrewMembers[i], alliedCrewMemberBotId[i]);
            vehicles.add(alliedPlane);
            
            makePwcgMissionTank(alliedCrewMembers[i], alliedCrewMembersSerialNumbers[i], alliedPlaneSerialNumbers[i], alliedPlane);
        }
    }

    private void makeGermanTanks(int numGermanPlanes, Integer[] germanCrewMemberSerialNumbers, Integer[] germanPlaneSerialNumbers, String[] germanCrewMembers, String[] germanCrewMemberBotId) throws PWCGException
    {
        for (int i = 0; i < numGermanPlanes; ++i)
        {
            AType12 germanPlane  = TestATypeFactory.makeGermanTank(germanCrewMembers[i], germanCrewMemberBotId[i]);
            vehicles.add(germanPlane);

            makePwcgMissionTank(germanCrewMembers[i], germanCrewMemberSerialNumbers[i], germanPlaneSerialNumbers[i], germanPlane);
        }
    }

    private void makeRussianTrucks(int numRussianTrucks) throws PWCGException
    {
        for (int i = 0; i < numRussianTrucks; ++i)
        {
            AType12 alliedTruck  = TestATypeFactory.makeTruck(Country.RUSSIA);
            vehicles.add(alliedTruck);
        }
    }

    private void makeGermanTrucks(int numGermanTrucks) throws PWCGException
    {
        for (int i = 0; i < numGermanTrucks; ++i)
        {
            AType12 germanTruck  = TestATypeFactory.makeTruck(Country.GERMANY);
            vehicles.add(germanTruck);
        }
    }

    private void makePwcgMissionTank(String crewMemberName, Integer crewMemberSerialNumber, Integer tankSerialNumber, IAType12 plane) throws PWCGException
    {
        PwcgGeneratedMissionVehicleData pwcgMissionPlane = new PwcgGeneratedMissionVehicleData();
        pwcgMissionPlane.setVehicleType(plane.getType());
        pwcgMissionPlane.setCrewMemberSerialNumber(crewMemberSerialNumber);
        pwcgMissionPlane.setVehicleSerialNumber(tankSerialNumber);
        missionTanks.put(crewMemberSerialNumber, pwcgMissionPlane);
        
        AType12 crewMemberBot = TestATypeFactory.makeCrewMemberBot(plane);
        crewMemberBots.put(crewMemberBot.getId(), crewMemberBot);
        
        makeMissionResultTank(crewMemberName, crewMemberSerialNumber, tankSerialNumber, plane);
    }

    private void makeMissionResultTank(String crewMemberName, Integer crewMemberSerialNumber, Integer tankSerialNumber, IAType12 plane) throws PWCGException
    {
    	LogTank logTank = new LogTank(1);
        logTank.setVehicleType(plane.getType());
        logTank.setId(plane.getId());
        logTank.setCrewMemberSerialNumber(crewMemberSerialNumber);
        logTank.setTankSerialNumber(tankSerialNumber);
        logTank.setCompanyId(1);

        LogCrewMember crewMemberCrewMember = new LogCrewMember();
        crewMemberCrewMember.setSerialNumber(crewMemberSerialNumber);
        crewMemberCrewMember.setBotId("");
        
        logTank.setCrewMemberSerialNumber(crewMemberSerialNumber);
        tankAiEntities.put(plane.getId(), logTank);
    }

    public List<IAType12> getVehicles()
    {
        return vehicles;
    }

    public Map<Integer, PwcgGeneratedMissionVehicleData> getMissionTanks()
    {
        return missionTanks;
    }

    public PwcgGeneratedMissionVehicleData getMissionTank(Integer key)
    {
        return missionTanks.get(key);
    }

    public Map<String, LogTank> getTankAiEntities()
    {
        return tankAiEntities;
    }

    public LogEventData getAARLogEventData()
    {
        return logEventData;
    }
}
