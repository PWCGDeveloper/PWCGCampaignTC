package pwcg.mission;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.AirfieldManager;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.Bridge;
import pwcg.campaign.group.BridgeFinder;
import pwcg.campaign.group.RailroadStationFinder;
import pwcg.campaign.group.TownFinder;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.skirmish.Skirmish;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.PWCGLocation;
import pwcg.mission.ground.builder.BattleSize;
import pwcg.mission.target.FrontBattleSizeGenerator;

public class MissionObjectiveBuilder
{
    private Campaign campaign;
    private Company company;
    private Skirmish skirmish;
    
    private MissionObjective objective = null;

    private List<Block> trainStationObjectives = new ArrayList<>();
    private List<Airfield> airfieldObjectives = new ArrayList<>();
    private List<PWCGLocation> townObjectives = new ArrayList<>();
    private List<Bridge> bridgeObjectives = new ArrayList<>();

    public MissionObjectiveBuilder(Campaign campaign, Company company, Skirmish skirmish)
    {
        this.campaign = campaign;
        this.company = company;
        this.skirmish = skirmish;
    }
    
    public MissionObjective buildMissionObjective() throws PWCGException
    {
        MissionSidesGenerator sidesGenerator = new MissionSidesGenerator(campaign, skirmish);
        ICountry defendingCountry = sidesGenerator.getDefendingCountry();
        ICountry assaultingCountry = sidesGenerator.getAssaultingCountry(defendingCountry);

        findMissionObjective(defendingCountry.getSide());
        setSidesForBattle(defendingCountry, assaultingCountry);
        setBattleSize();
        
        return objective;
    }

    private void setSidesForBattle(ICountry defendingCountry, ICountry assaultingCountry) throws PWCGException
    {
        objective.setDefendingCountry(defendingCountry);
        objective.setAssaultingCountry(assaultingCountry);
    }

    private void setBattleSize() throws PWCGException
    {
        BattleSize battleSize = FrontBattleSizeGenerator.createAssaultBattleSize(campaign);
        objective.setBattleSize(battleSize);
    }

    private void findMissionObjective(Side defendingSide) throws PWCGException
    {
        if (skirmish != null)
        {
            createObjectiveForSkirmish(defendingSide);
        }
        else
        {
            createObjectiveForBattle(defendingSide);
        }
        
        if(objective == null)
        {
            throw new PWCGException("Failed to generate mission objective");
        }
    }

    private void createObjectiveForSkirmish(Side defendingSide) throws PWCGException
    {
        int radius = 15000;
        Coordinate referenceCoordinate = skirmish.getCenter();
        while (objective == null && radius < 25000)
        {
            findMissionObjectiveForGenericBattle(defendingSide, referenceCoordinate, radius);
            radius += 5000;
        }
        
        if (objective == null)
        {
            objective = new MissionObjective(skirmish);
        }
    }

    private void createObjectiveForBattle(Side defendingSide) throws PWCGException
    {
        Coordinate referenceCoordinate = findfrontPositionNearCompany();
        int radius = 50000;
        while (objective == null)
        {
            findMissionObjectiveForGenericBattle(defendingSide, referenceCoordinate, radius);
            radius += 10000;
        }
    }

    private Coordinate findfrontPositionNearCompany() throws PWCGException
    {
        FrontLinesForMap frontLinesForMap = PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
        Coordinate companyCoordinate = company.determineCurrentPosition(campaign.getDate());
        Coordinate closestFront = frontLinesForMap.findClosestFrontCoordinateForSide(companyCoordinate, company.getCountry().getSide());
        List<FrontLinePoint> nearbyFrontPositions = frontLinesForMap.findClosestFrontPositionsForSide(closestFront, 20000, company.getCountry().getSide());
        Collections.shuffle(nearbyFrontPositions);
        Coordinate referenceCoordinate = nearbyFrontPositions.get(0).getPosition();
        return referenceCoordinate;
    }

    private void findMissionObjectiveForGenericBattle(Side defendingSide, Coordinate referenceCoordinate, int radius) throws PWCGException
    {
        
        findRailroadObjectives(defendingSide, referenceCoordinate, radius);
        findAirfieldObjectives(defendingSide, referenceCoordinate, radius);
        findTownObjectives(defendingSide, referenceCoordinate, radius);
        findBridgeObjectives(defendingSide, referenceCoordinate, radius);
        
        List<MissionObjectiveType> objectiveTypes = getObjectiveTypes();
        for (MissionObjectiveType objectiveType : objectiveTypes)
        {
            if (objectiveType == MissionObjectiveType.RAILROAD_STATION)
            {
                objective = findTrainStationObjective();
                if (objective != null)
                {
                    break;
                }
            }
            else if (objectiveType == MissionObjectiveType.TOWN)
            {
                objective = findTownObjective();
                if (objective != null)
                {
                    break;
                }
            }
            else if (objectiveType == MissionObjectiveType.AIRFIELD)
            {
                objective = findAirfieldObjective();
                if (objective != null)
                {
                    break;
                }
            }
            else if (objectiveType == MissionObjectiveType.BRIDGE)
            {
                objective = findBridgeObjective();
                if (objective != null)
                {
                    break;
                }
            }
        }
    }

    private MissionObjective findTrainStationObjective()
    {
        if(!townObjectives.isEmpty())
        {
           return new MissionObjective(townObjectives.get(0));
        }
        return null; 
    }
    
    private MissionObjective findAirfieldObjective()
    {
        if(!airfieldObjectives.isEmpty())
        {
           return new MissionObjective(airfieldObjectives.get(0));
        }
        return null; 
    }
    
    private MissionObjective findTownObjective()
    {
        if(!trainStationObjectives.isEmpty())
        {
           return new MissionObjective(trainStationObjectives.get(0));
        }
        return null; 
    }
    
    private MissionObjective findBridgeObjective()
    {
        if(!bridgeObjectives.isEmpty())
        {
           return new MissionObjective(bridgeObjectives.get(0));
        }
        return null; 
    }

    private void findRailroadObjectives(Side defendingSide, Coordinate referenceCoordinate, int radius) throws PWCGException
    {
        RailroadStationFinder railroadFinder = PWCGContext.getInstance().getCurrentMap().getGroupManager().getRailroadStationFinder();
        for (Block trainStation : railroadFinder.getTrainPositionWithinRadiusBySide(defendingSide, campaign.getDate(), referenceCoordinate, radius))
        {
            if(isCloseToFront(trainStation.getPosition(), defendingSide))
            {
                trainStationObjectives.add(trainStation);
            }
        }

        Collections.shuffle(trainStationObjectives);
    }
    
    private void findAirfieldObjectives(Side defendingSide, Coordinate referenceCoordinate, int radius) throws PWCGException
    {
        AirfieldManager airfieldManager = PWCGContext.getInstance().getCurrentMap().getAirfieldManager();
        for (Airfield airfield : airfieldManager.getAirfieldsWithinRadiusBySide(defendingSide, campaign.getDate(), referenceCoordinate,radius))
        {
            if(isCloseToFront(airfield.getPosition(), defendingSide))
            {
                airfieldObjectives.add(airfield);
            }
        }

        Collections.shuffle(airfieldObjectives);
    }
    
    private void findTownObjectives(Side defendingSide, Coordinate referenceCoordinate, int radius) throws PWCGException
    {
        TownFinder townFinder = PWCGContext.getInstance().getCurrentMap().getGroupManager().getTownFinder();
        for (PWCGLocation town : townFinder.findTownsForSideWithinRadius(defendingSide, campaign.getDate(), referenceCoordinate, radius))
        {
            if(isCloseToFront(town.getPosition(), defendingSide))
            {
                townObjectives.add(town);
            }
        }

        Collections.shuffle(townObjectives);
    }
    
    private void findBridgeObjectives(Side defendingSide, Coordinate referenceCoordinate, int radius) throws PWCGException
    {
        BridgeFinder bridgeFinder = PWCGContext.getInstance().getCurrentMap().getGroupManager().getBridgeFinder();
        for (Bridge bridge : bridgeFinder.findBridgesForSideWithinRadius(defendingSide, campaign.getDate(), referenceCoordinate, radius))
        {
            if(isCloseToFront(bridge.getPosition(), defendingSide))
            {
                bridgeObjectives.add(bridge);
            }
        }
        
        Collections.shuffle(bridgeObjectives);

    }

    private boolean isCloseToFront(Coordinate position, Side defendingSide) throws PWCGException
    {
        double distance = PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate()).findClosestFriendlyPositionDistance(position, defendingSide);
        if (distance < 20000)
        {
            return true;
        }
        return false;
    }
    
    private List<MissionObjectiveType> getObjectiveTypes()
    {
        List<MissionObjectiveType> objectiveTypes = new ArrayList<>();
        addObjectiveType(objectiveTypes, MissionObjectiveType.RAILROAD_STATION, 10);
        addObjectiveType(objectiveTypes, MissionObjectiveType.AIRFIELD, 10);
        addObjectiveType(objectiveTypes, MissionObjectiveType.TOWN, 6);
        addObjectiveType(objectiveTypes, MissionObjectiveType.BRIDGE, 3);
        
        Collections.shuffle(objectiveTypes);
        
        return objectiveTypes;
    }

    private void addObjectiveType(List<MissionObjectiveType> objectiveTypes, MissionObjectiveType objectiveType, int weight)
    {
        for(int i = 0; i < weight; ++i)
        {
            objectiveTypes.add(objectiveType);
        }
    }
}
