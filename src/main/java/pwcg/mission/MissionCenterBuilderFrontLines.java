package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.MapArea;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;

public class MissionCenterBuilderFrontLines implements IMissionCenterBuilder
{
    private Campaign campaign;
    private MissionHumanParticipants participatingPlayers;

    public MissionCenterBuilderFrontLines(Campaign campaign, MissionHumanParticipants participatingPlayers)
    {
        this.campaign = campaign;
        this.participatingPlayers = participatingPlayers;
    }

    public Coordinate findMissionCenter(int missionBoxRadius) throws PWCGException
    {
        Coordinate missionCenterCoordinateAxis = findAxisFrontCoordinateWithinSingleMissionParameters();
        Coordinate missionCenterCoordinateAllied = findAlliedCoordinateNearAxisCoordinate(missionCenterCoordinateAxis);

        double angle = MathUtils.calcAngle(missionCenterCoordinateAxis, missionCenterCoordinateAllied);
        double distance = MathUtils.calcDist(missionCenterCoordinateAxis, missionCenterCoordinateAllied) / 2;
        Coordinate missionCenterCoordinate = MathUtils.calcNextCoord(missionCenterCoordinateAxis, angle, distance);
        
        MapArea usableMapArea = PWCGContext.getInstance().getCurrentMap().getUsableMapArea();
        missionCenterCoordinate = MissionCenterAdjuster.keepWithinMap(missionCenterCoordinate.copy(), missionBoxRadius, usableMapArea);

        return missionCenterCoordinate;
    }

    private Coordinate findAxisFrontCoordinateWithinSingleMissionParameters() throws PWCGException
    {
        List<FrontLinePoint> selectedFrontPointsAxis = new ArrayList<>();

        MissionCenterDistanceCalculator distanceCalculator = new MissionCenterDistanceCalculator(campaign);
        int missionCenterMaxDistanceForMission = distanceCalculator.determineMaxDistanceForMissionCenter();

        while (selectedFrontPointsAxis.isEmpty())
        {
            selectedFrontPointsAxis = findFrontLinePointsForMissionCenter(missionCenterMaxDistanceForMission);
            missionCenterMaxDistanceForMission += 10000;
        }
        return selectMissionCenter(selectedFrontPointsAxis);
    }

    private Coordinate selectMissionCenter(List<FrontLinePoint> selectedFrontPointsAxis)
    {
        int frontLinePointIndex = RandomNumberGenerator.getRandom(selectedFrontPointsAxis.size());
        FrontLinePoint axisFrontLinePointForMissionCenter = selectedFrontPointsAxis.get(frontLinePointIndex);
        return axisFrontLinePointForMissionCenter.getPosition();
    }

    private List<FrontLinePoint> findFrontLinePointsForMissionCenter(int missionCenterMaxDistanceForMission)
            throws PWCGException
    {
        Side frontSide = determineFrontSide();

        List<FrontLinePoint> selectedFrontPoints = new ArrayList<>();
        int missionCenterMaxDistanceForMissionStop = missionCenterMaxDistanceForMission + 50000;
        while (selectedFrontPoints.size() < 15 && missionCenterMaxDistanceForMission <= missionCenterMaxDistanceForMissionStop)
        {
            selectedFrontPoints = findFrontLinePointsForMissionCenterByRange(missionCenterMaxDistanceForMission, frontSide);
            missionCenterMaxDistanceForMission += 5000;
        }
        return selectedFrontPoints;
    }

    private Side determineFrontSide()
    {
        int roll = RandomNumberGenerator.getRandom(100);
        if (roll < 50)
        {
            return Side.ALLIED;
        }
        else
        {
            return Side.AXIS;
        }
    }

    private List<FrontLinePoint> findFrontLinePointsForMissionCenterByRange(int missionCenterMaxDistanceForMission,
            Side frontSide) throws PWCGException
    {
        List<Coordinate> companyPositions = determineCompanyCoordinates();
        FrontLinesForMap frontLinesForMap = PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
        List<FrontLinePoint> frontPoints = frontLinesForMap.findAllFrontLinesForSide(frontSide);
        List<FrontLinePoint> selectedFrontPoints = new ArrayList<>();

        if (!hasFrontLinesFarEnoughFromBase(missionCenterMaxDistanceForMission, companyPositions, frontPoints))
        {
            selectedFrontPoints.addAll(frontPoints);
        }
        else
        {
            selectedFrontPoints = getFrontLinePositionsFromMaxDistance(missionCenterMaxDistanceForMission,
                    companyPositions, frontPoints);
        }

        return selectedFrontPoints;
    }

    private boolean hasFrontLinesFarEnoughFromBase(int missionCenterMinDistanceFromBase, List<Coordinate> companyPositions, List<FrontLinePoint> frontPoints)
            throws PWCGException
    {
        boolean hasFrontPointsFarEnough = false;
        for (FrontLinePoint frontLinePoint : frontPoints)
        {
            for (Coordinate companyPosition : companyPositions)
            {
                double distanceFromCompany = MathUtils.calcDist(companyPosition, frontLinePoint.getPosition());
                if (distanceFromCompany > missionCenterMinDistanceFromBase)
                {
                    hasFrontPointsFarEnough = true;
                    return hasFrontPointsFarEnough;
                }
            }
        }

        return hasFrontPointsFarEnough;
    }

    private List<FrontLinePoint> getFrontLinePositionsFromMaxDistance(
            int missionCenterMaxDistanceForMission,
            List<Coordinate> companyPositions, 
            List<FrontLinePoint> frontPoints)
    {
        List<FrontLinePoint> selectedFrontPoints = new ArrayList<>();
        for (FrontLinePoint frontLinePoint : frontPoints)
        {
            for (Coordinate companyPosition : companyPositions)
            {
                double distanceFromCompany = MathUtils.calcDist(companyPosition, frontLinePoint.getPosition());
                if (distanceFromCompany < missionCenterMaxDistanceForMission)
                {
                    selectedFrontPoints.add(frontLinePoint);
                }
            }
        }
        return selectedFrontPoints;
    }

    private List<Coordinate> determineCompanyCoordinates() throws PWCGException
    {
        List<Coordinate> playerCompanyCoordinates = new ArrayList<>();
        for (int playerCompanyId : participatingPlayers.getParticipatingCompanyIds())
        {
            ICompanyMission playerCompany = PWCGContext.getInstance().getCompanyManager().getCompany(playerCompanyId);
            Coordinate playerCompanyCoordinate = playerCompany.determinePosition(campaign.getDate());
            playerCompanyCoordinates.add(playerCompanyCoordinate);
        }
        return playerCompanyCoordinates;
    }

    private Coordinate findAlliedCoordinateNearAxisCoordinate(Coordinate axisCoordinate) throws PWCGException
    {
        FrontLinesForMap frontLinesForMap = PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
        Coordinate alliedCoordinateCloseToAxisCoordinate = frontLinesForMap.findClosestFrontCoordinateForSide(axisCoordinate, Side.ALLIED);
        return alliedCoordinateCloseToAxisCoordinate;
    }
}
