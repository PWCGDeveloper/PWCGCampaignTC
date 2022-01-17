package pwcg.mission.ground;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.core.utils.PositionFinder;
import pwcg.mission.Mission;
import pwcg.mission.ground.builder.IBattleBuilder;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.target.TargetType;

public class MissionGroundUnitBuilder
{
    private Mission mission = null;
    private Campaign campaign = null;

    private GroundUnitCollection missionBattle;
    private List<GroundUnitCollection> missionTrains = new ArrayList<>();
    private List<GroundUnitCollection> missionTrucks = new ArrayList<>();
    private List<GroundUnitCollection> missionDrifters = new ArrayList<>();

    public MissionGroundUnitBuilder (Mission mission)
    {
        this.mission = mission;
        this.campaign = mission.getCampaign();
    }

    public void generateGroundUnitsForMission() throws PWCGException 
    {
        generateBattle();
        generateTrains();
        generateTrucks();
        generateDrifters();
    }

    private void generateBattle() throws PWCGException 
    {
        IBattleBuilder battleBuilder = MissionBattleBuilderFactory.getBattleBuilder(mission);
        missionBattle = battleBuilder.generateBattle();
    }

    private void generateTrains() throws PWCGException 
    {
        MissionTrainBuilder trainBuilder = new MissionTrainBuilder(campaign, mission);
        missionTrains = trainBuilder.generateMissionTrains();
    }

    private void generateTrucks() throws PWCGException 
    {
        MissionTruckConvoyBuilder truckConvoyBuilder = new MissionTruckConvoyBuilder(campaign, mission);
        missionTrucks = truckConvoyBuilder.generateMissionTrucks(missionBattle);
    }

    private void generateDrifters() throws PWCGException
    {
        MissionDrifterBuilder drifterBuilder = new MissionDrifterBuilder(campaign, mission);
        missionDrifters = drifterBuilder.generateMissionDrifters();
    }

    public void write(BufferedWriter writer) throws PWCGException
    {
        for (GroundUnitCollection groundUnit : getAllMissionGroundUnits())
        {
            groundUnit.write(writer);
        }
    }
    
    public List<GroundUnitCollection> getAllMissionGroundUnits()
    {
        List<GroundUnitCollection> allMissionGroundUnits = new ArrayList<>();
        allMissionGroundUnits.add(missionBattle);
        allMissionGroundUnits.addAll(missionTrains);
        allMissionGroundUnits.addAll(missionTrucks);
        allMissionGroundUnits.addAll(missionDrifters);
        return allMissionGroundUnits;
    }
    
    public void finalizeGroundUnits() throws PWCGException
    {
        eliminateDuplicateGroundUnits();
    }
    
    private void eliminateDuplicateGroundUnits() throws PWCGException
    {
        eliminateDuplicateGroundUnitsFromCollection(Arrays.asList(missionBattle));
        eliminateDuplicateGroundUnitsFromCollection(missionTrucks);
        eliminateDuplicateGroundUnitsFromCollection(missionTrains);
        eliminateDuplicateGroundUnitsFromCollection(missionDrifters);
    }
    
    private void eliminateDuplicateGroundUnitsFromCollection(List<GroundUnitCollection> groundUnitCollections) throws PWCGException
    {
        while (eliminateDuplicateGroundUnitFromCollection(groundUnitCollections))
        {
            
        }
    }
    
    private boolean eliminateDuplicateGroundUnitFromCollection(List<GroundUnitCollection> groundUnitCollections) throws PWCGException
    {
        GroundUnitPositionDuplicateDetector groundUnitPositionVerifier = new GroundUnitPositionDuplicateDetector();        
        for (GroundUnitCollection groundUnitCollection : groundUnitCollections)
        {
            if (!groundUnitPositionVerifier.verifyGroundCollectionUnitPositionsNotDuplicated(getAllMissionGroundUnits(), groundUnitCollection))
            {
                groundUnitCollections.remove(groundUnitCollection);
                return true;
            }
        }
                        
        return false;
    }

    public List<GroundUnitCollection> getBattleMissionGroundUnits()
    {
        List<GroundUnitCollection> allMissionGroundUnits = new ArrayList<>();
        allMissionGroundUnits.add(missionBattle);
        return allMissionGroundUnits;
    }
    
    public List<TargetType> getAvailableGroundUnitTargetTypesForMissionForSide(Side side) throws PWCGException
    {
        Map <TargetType, TargetType> uniqueTargetTypesForSide = new HashMap<>();
        for (GroundUnitCollection groundUnitCollection : getAllMissionGroundUnits())
        {
            if (groundUnitCollection.getTargetType() != TargetType.TARGET_NONE)
            {
                if (groundUnitCollection.getGroundUnitsForSide(side).size() > 0)
                {
                    uniqueTargetTypesForSide.put(groundUnitCollection.getTargetType(), groundUnitCollection.getTargetType());
                }
            }
        }
        return new ArrayList<>(uniqueTargetTypesForSide.values());
    }    

    public List<GroundUnitCollection> getGroundUnitsForSide(Side side) throws PWCGException
    {
        List<GroundUnitCollection> groundUnitsForSide = new ArrayList<>();
        for (GroundUnitCollection groundUnitCollection : getAllMissionGroundUnits())
        {
            if (groundUnitCollection.getGroundUnitsForSide(side).size() > 0)
            {
                groundUnitsForSide.add(groundUnitCollection);
            }
        }
        return groundUnitsForSide;
    }

    public int getUnitCount()
    {
        int trainCount = 0;
        int truckCount = 0;
        
        for (GroundUnitCollection groundUnitCollection : missionTrains)
        {
            trainCount += groundUnitCollection.getUnitCount();
        }
        for (GroundUnitCollection groundUnitCollection : missionTrucks)
        {
            truckCount += groundUnitCollection.getUnitCount();
        }
 
        PWCGLogger.log(LogLevel.INFO, "Mission unit count train : " + trainCount);
        PWCGLogger.log(LogLevel.INFO, "Mission unit count truck : " + truckCount);
        
        int missionUnitCount = trainCount + truckCount;
        PWCGLogger.log(LogLevel.INFO, "Mission unit count total : " + missionUnitCount);
        return missionUnitCount;

    }

    public GroundUnitCollection getClosestGroundUnitForSide(Coordinate position, Side side) throws PWCGException
    {
        GroundUnitCollection closestGroundUnitForSide = null;
        
        double closestDistanceToPosition = PositionFinder.ABSURDLY_LARGE_DISTANCE;
        for (GroundUnitCollection groundUnitCollection : getAllMissionGroundUnits())
        {
            if (!groundUnitCollection.getGroundUnitsForSide(side).isEmpty())
            {
                double distanceToPosition = MathUtils.calcDist(position, groundUnitCollection.getPosition());
                if (distanceToPosition < closestDistanceToPosition)
                {
                    closestDistanceToPosition = distanceToPosition;
                    closestGroundUnitForSide = groundUnitCollection;
                    
                }
            }
        }
        return closestGroundUnitForSide;
    }

    public GroundUnitCollection getAssault()
    {
        return missionBattle;
    }
 }
