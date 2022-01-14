package pwcg.mission.ground;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.DrifterManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.PWCGLocation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.ground.builder.DrifterUnitBuilder;
import pwcg.mission.ground.org.GroundUnitCollection;

public class MissionDrifterBuilder extends MissionUnitBuilder
{
    private List<GroundUnitCollection> missionDrifterUnits = new ArrayList<>();

    public MissionDrifterBuilder (Campaign campaign, Mission mission)
    {
        super(campaign, mission);
    }

    
    public List<GroundUnitCollection> generateMissionDrifters() throws PWCGException 
    {
        missionDrifterUnits.addAll(buildDriftersForSide(Side.ALLIED));
        missionDrifterUnits.addAll(buildDriftersForSide(Side.AXIS));
        return missionDrifterUnits;
    }

    private List<GroundUnitCollection> buildDriftersForSide(Side drifterSide) throws PWCGException 
    {
        List<GroundUnitCollection> missionDrifterUnitsForSide = new ArrayList<>();
        
        List<PWCGLocation> drifterPositionsForSide = getRiversForDrifters(drifterSide);
        List<PWCGLocation> sortedCoordinatesByDistance = sortCoordinatesDistanceFromMission(drifterPositionsForSide);
        if (!sortedCoordinatesByDistance.isEmpty())
        {
            PWCGLocation drifterPosition = sortedCoordinatesByDistance.get(0);
            GroundUnitCollection drifterUnit = makeDrifter(drifterSide, drifterPosition);
            missionDrifterUnitsForSide.add(drifterUnit);
        }
        
        return missionDrifterUnitsForSide;
    }

    private List<PWCGLocation> getRiversForDrifters(Side drifterSide) throws PWCGException 
    {
        List<PWCGLocation> drifterPositionsForSide = new ArrayList<>();
        Campaign campaign = mission.getCampaign();

        DrifterManager drifterManager =  PWCGContext.getInstance().getCurrentMap().getDrifterManager();
        for (PWCGLocation drifterPosition : drifterManager.getBargePositionsForSide(campaign, drifterSide))
        {
            drifterPositionsForSide.add(drifterPosition);
        }
        
        return drifterPositionsForSide;
    }
    
    private List<PWCGLocation> sortCoordinatesDistanceFromMission(List<PWCGLocation> drifterPositionsForSide) throws PWCGException
    {
        Map<Double, PWCGLocation> sortedStationsByDistance = new TreeMap<>();
        
        Coordinate missionCenter = mission.getMissionBorders().getCenter();
        for (PWCGLocation drifterPosition : drifterPositionsForSide)
        {
            Double distanceFromMission = MathUtils.calcDist(missionCenter, drifterPosition.getPosition());
            sortedStationsByDistance.put(distanceFromMission, drifterPosition);
        }
        return new ArrayList<PWCGLocation>(sortedStationsByDistance.values());
    }

    private GroundUnitCollection makeDrifter(Side drifterSide, PWCGLocation drifterPosition) throws PWCGException
    {
        ICountry drifterCountry = CountryFactory.makeMapReferenceCountry(drifterSide);
        DrifterUnitBuilder drifterUnitBuilder =  new DrifterUnitBuilder(mission.getCampaign(), drifterPosition, drifterCountry);
        GroundUnitCollection drifterUnit = drifterUnitBuilder.createDrifterUnit();
        return drifterUnit;
    }
 }
