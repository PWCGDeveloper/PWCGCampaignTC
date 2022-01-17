package pwcg.mission;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
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

public class MissionObjectiveBuilder
{
    private Campaign campaign;
    private Skirmish skirmish;
    
    private MissionObjective objective = null;

    private List<Block> trainStationObjectives = new ArrayList<>();
    private List<Airfield> airfieldObjectives = new ArrayList<>();
    private List<PWCGLocation> townObjectives = new ArrayList<>();
    private List<Bridge> bridgeObjectives = new ArrayList<>();

    public MissionObjectiveBuilder(Campaign campaign, Skirmish skirmish)
    {
        this.campaign = campaign;
        this.skirmish = skirmish;
    }
    
    public MissionObjective buildMissionObjective() throws PWCGException
    {
        MissionSidesGenerator sidesGenerator = new MissionSidesGenerator(campaign, skirmish);
        ICountry defendingCountry = sidesGenerator.getDefendingCountry();
        ICountry assaultingCountry = sidesGenerator.getAssaultingCountry(defendingCountry);
        
        findMissionObjective(defendingCountry.getSide());
        
        objective.setDefendingCountry(defendingCountry);
        objective.setAssaultingCountry(assaultingCountry);
        return objective;
    }
    
    
    private void findMissionObjective(Side defendingSide) throws PWCGException
    {
        
        findRailroadObjectives(defendingSide);
        findAirfieldObjectives(defendingSide);
        findTownObjectives(defendingSide);
        findBridgeObjectives(defendingSide);
        
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
        
        if(objective == null)
        {
            throw new PWCGException("Failed to generate mission objective");
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

    private void findRailroadObjectives(Side defendingSide) throws PWCGException
    {
        RailroadStationFinder railroadFinder = PWCGContext.getInstance().getCurrentMap().getGroupManager().getRailroadStationFinder();
        for (Block trainStation : railroadFinder.getTrainPositionsBySide(defendingSide, campaign.getDate()))
        {
            if(isCloseToFront(trainStation.getPosition(), defendingSide))
            {
                trainStationObjectives.add(trainStation);
            }
        }

        Collections.shuffle(trainStationObjectives);
    }
    
    private void findAirfieldObjectives(Side defendingSide) throws PWCGException
    {
        AirfieldManager airfieldManager = PWCGContext.getInstance().getCurrentMap().getAirfieldManager();
        for (Airfield airfield : airfieldManager.getAirFieldsForSide(campaign.getDate(), defendingSide))
        {
            if(isCloseToFront(airfield.getPosition(), defendingSide))
            {
                airfieldObjectives.add(airfield);
            }
        }

        Collections.shuffle(airfieldObjectives);
    }
    
    private void findTownObjectives(Side defendingSide) throws PWCGException
    {
        TownFinder townFinder = PWCGContext.getInstance().getCurrentMap().getGroupManager().getTownFinder();
        for (PWCGLocation town : townFinder.findAllTownsForSide(defendingSide, campaign.getDate()))
        {
            if(isCloseToFront(town.getPosition(), defendingSide))
            {
                townObjectives.add(town);
            }
        }

        Collections.shuffle(townObjectives);
    }
    
    private void findBridgeObjectives(Side defendingSide) throws PWCGException
    {
        BridgeFinder bridgeFinder = PWCGContext.getInstance().getCurrentMap().getGroupManager().getBridgeFinder();
        for (Bridge bridge : bridgeFinder.findAllBridgesForSide(defendingSide, campaign.getDate()))
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
