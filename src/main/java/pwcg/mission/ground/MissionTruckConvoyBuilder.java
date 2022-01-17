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
import pwcg.campaign.group.Bridge;
import pwcg.campaign.group.BridgeFinder;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.ground.builder.TruckConvoyBuilder;
import pwcg.mission.ground.builder.TruckUnitTransportBuilder;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.GroundUnitType;
import pwcg.mission.ground.org.IGroundUnit;

public class MissionTruckConvoyBuilder extends MissionUnitBuilder
{
    private List<GroundUnitCollection> missionTransportConvoys = new ArrayList<>();

    public MissionTruckConvoyBuilder (Campaign campaign, Mission mission)
    {
        super(campaign, mission);
    }

    
    public List<GroundUnitCollection> generateMissionTrucks(GroundUnitCollection missionBattle) throws PWCGException 
    {
        missionTransportConvoys.addAll(buildOneOneForSide(Side.ALLIED, missionBattle));
        missionTransportConvoys.addAll(buildOneOneForSide(Side.AXIS, missionBattle));
        return missionTransportConvoys;
    }

    private List<GroundUnitCollection> buildOneOneForSide(Side truckSide, GroundUnitCollection missionBattle) throws PWCGException
    {
        BridgeFinder bridgeFinder = PWCGContext.getInstance().getCurrentMap().getGroupManager().getBridgeFinder();
        List<Bridge> bridgesForSide = bridgeFinder.findBridgesForSideWithinRadius(truckSide, campaign.getDate(), mission.getObjective().getPosition(), 15000);
        
        List<GroundUnitCollection> truckUnitsForSide = new ArrayList<>();
        Bridge bridge = getClosestBridgeToObjective(bridgesForSide);
        if (bridge != null)
        {
            GroundUnitCollection truckUnit = makeTruckConvoy(truckSide, bridge);
            truckUnitsForSide.add( truckUnit);
        }
        
        for (IGroundUnit artilleryUnit : missionBattle.getGroundUnitsByTypeAndSide(GroundUnitType.ARTILLERY_UNIT, truckSide))
        {
            GroundUnitCollection truckUnit = makeGroundUnitTransport(truckSide, artilleryUnit);
            truckUnitsForSide.add(truckUnit);
        }
        
        return truckUnitsForSide;
    }
    
    private Bridge getClosestBridgeToObjective(List<Bridge> bridgesForSide) throws PWCGException
    {
        if (bridgesForSide.isEmpty())
        {
            return null;
        }
        
        Map<Double, Bridge> sortedBridgesByDistance = new TreeMap<>();
        Coordinate missionObjective = mission.getObjective().getPosition();
        for (Bridge station : bridgesForSide)
        {
            Double distanceFromMission = MathUtils.calcDist(missionObjective, station.getPosition());
            sortedBridgesByDistance.put(distanceFromMission, station);
        }
        
        List<Bridge> sortedBridgesByDistanceList = new ArrayList<Bridge>(sortedBridgesByDistance.values());        
        return sortedBridgesByDistanceList.get(0);
    }

    private GroundUnitCollection makeTruckConvoy(Side truckSide, Bridge bridge) throws PWCGException
    {
        ICountry truckCountry = CountryFactory.makeMapReferenceCountry(truckSide);
        TruckConvoyBuilder groundUnitFactory =  new TruckConvoyBuilder(campaign, bridge, truckCountry);
        GroundUnitCollection truckUnit = groundUnitFactory.createTruckConvoy();
        return truckUnit;
    }

    private GroundUnitCollection makeGroundUnitTransport(Side truckSide, IGroundUnit artilleryUnit) throws PWCGException
    {
        ICountry truckCountry = CountryFactory.makeMapReferenceCountry(truckSide);
        TruckUnitTransportBuilder groundUnitFactory =  new TruckUnitTransportBuilder(campaign, artilleryUnit, truckCountry);
        GroundUnitCollection truckUnit = groundUnitFactory.createTruckConvoy();
        return truckUnit;
    }
 }
