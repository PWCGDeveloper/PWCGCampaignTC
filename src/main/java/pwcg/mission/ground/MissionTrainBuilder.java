package pwcg.mission.ground;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.RailroadStationFinder;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.ground.builder.TrainUnitBuilder;
import pwcg.mission.ground.org.GroundUnitCollection;

public class MissionTrainBuilder extends MissionUnitBuilder
{
    private List<GroundUnitCollection> missionTrains = new ArrayList<>();

    public MissionTrainBuilder (Campaign campaign, Mission mission)
    {
        super(campaign, mission);
    }
    
    public List<GroundUnitCollection> generateMissionTrains() throws PWCGException 
    {
        missionTrains.addAll(buildOneTrainForSide(Side.ALLIED));
        missionTrains.addAll(buildOneTrainForSide(Side.AXIS));
        return missionTrains;
    }

    private List<GroundUnitCollection> buildOneTrainForSide(Side trainSide) throws PWCGException
    {
        RailroadStationFinder railroadFinder = PWCGContext.getInstance().getCurrentMap().getGroupManager().getRailroadStationFinder();
        List<Block> stationsForSide = railroadFinder.getTrainPositionWithinRadiusBySide(trainSide, campaign.getDate(), mission.getObjective().getPosition(), 8000);
        
        List<GroundUnitCollection> missionTrainsForSide = new ArrayList<>();
        Block station = getClosestStationToObjective(stationsForSide);
        if (station != null)
        {
            GroundUnitCollection trainUnit = makeTrain(trainSide, station);
            missionTrainsForSide.add(trainUnit);
        }
        return missionTrainsForSide;
    }
    
    private Block getClosestStationToObjective(List<Block> stationsForSide) throws PWCGException
    {
        if (stationsForSide.isEmpty())
        {
            return null;
        }
        
        Map<Double, Block> sortedStationsByDistance = new TreeMap<>();
        Coordinate missionObjective = mission.getObjective().getPosition();
        for (Block station : stationsForSide)
        {
            Double distanceFromMission = MathUtils.calcDist(missionObjective, station.getPosition());
            sortedStationsByDistance.put(distanceFromMission, station);
        }
        
        List<Block> sortedStationsByDistanceList = new ArrayList<Block>(sortedStationsByDistance.values());        
        return sortedStationsByDistanceList.get(0);
    }

    private GroundUnitCollection makeTrain(Side trainSide, Block station) throws PWCGException
    {
        ICountry trainCountry = CountryFactory.makeMapReferenceCountry(trainSide);
        TrainUnitBuilder trainfactory =  new TrainUnitBuilder(campaign, station, trainCountry);
        GroundUnitCollection trainUnit = trainfactory.createTrainUnit();
        return trainUnit;
    }

 }
