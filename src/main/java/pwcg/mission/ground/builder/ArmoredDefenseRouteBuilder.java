package pwcg.mission.ground.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.GroundUnitType;
import pwcg.mission.platoon.ITankPlatoon;

public class ArmoredDefenseRouteBuilder
{
    private GroundUnitCollection defenseFixedUnitCollection;
    private Mission mission;
    private Side defendingSide;
    private Map<Integer, List<Coordinate>> defenseRoutes = new HashMap<>();

    public ArmoredDefenseRouteBuilder(Mission mission)
    {
        this.mission = mission;
        this.defenseFixedUnitCollection = mission.getGroundUnitBuilder().getAssault();
        this.defendingSide = mission.getObjective().getAssaultingCountry().getSide();
    }

    public Map<Integer, List<Coordinate>> buildAssaultRoutesForArmor(List<ITankPlatoon> platoons) throws PWCGException
    {
        List<ITankPlatoon> defendingPlatoons = getDefendingPlatoons(platoons);
        int numTankPlatoons = defendingPlatoons.size();
        
        List<Coordinate> startPositions = getStartPositions(numTankPlatoons);
        List<Coordinate> objectivePositions = getFirstTargetPositions(startPositions);

        for(int i = 0; i < numTankPlatoons; ++i)
        {
            List<Coordinate> defenseRoute = new ArrayList<>();
            defenseRoute.add(startPositions.get(i));
            defenseRoute.add(objectivePositions.get(i));
            
            defenseRoutes.put(defendingPlatoons.get(i).getIndex(),  defenseRoute);
        }
        
        return defenseRoutes;
    }

    private List<ITankPlatoon> getDefendingPlatoons(List<ITankPlatoon> platoons)
    {
        List<ITankPlatoon> defendingPlatoons = new ArrayList<>();
        for(ITankPlatoon platoon : platoons)
        {
            if(platoon.getPlatoonInformation().getCountry().getSide() == defendingSide)
            {
                defendingPlatoons.add(platoon);
            }
        }
        return defendingPlatoons;
    }

    private List<Coordinate> getStartPositions(int numTankPlatoons) throws PWCGException
    {
        ArmoredDefenseTargetFinder targetFinder = new ArmoredDefenseTargetFinder(
                mission, defenseFixedUnitCollection, defendingSide, GroundUnitType.ANTI_TANK_UNIT);
        
        List<Coordinate> startPositionsFriendlyAT = targetFinder.findInitialTargetForTankPlatoon(numTankPlatoons);        
        List<Coordinate> startPositions = moveStartPositionBackFromAT(startPositionsFriendlyAT, 400);
        return startPositions;
    }

    private List<Coordinate> moveStartPositionBackFromAT(List<Coordinate> startPositionsFriendlyAT, int distance) throws PWCGException
    {
        List<Coordinate> startPositions = new ArrayList<>();
        for(Coordinate startPositiontAT : startPositionsFriendlyAT)
        {
            Coordinate referenceObjectivePosition = mission.getObjective().getPosition().copy();
            double angleBackFromATLine = MathUtils.calcAngle(startPositiontAT, referenceObjectivePosition);
            Coordinate startPosition = MathUtils.calcNextCoord(startPositiontAT, angleBackFromATLine, distance);
            startPositions.add(startPosition);
        }
        return startPositions;
    }

    private List<Coordinate> getFirstTargetPositions(List<Coordinate> startPositions) throws PWCGException
    {
        List<Coordinate> defensePositions = new ArrayList<>();
        for(Coordinate startPosition : startPositions)
        {
            Coordinate referenceObjectivePosition = mission.getObjective().getPosition().copy();
            double angleToEnemy = MathUtils.calcAngle(referenceObjectivePosition, startPosition);
            Coordinate defensePosition = MathUtils.calcNextCoord(startPosition, angleToEnemy, 250);
            defensePositions.add(defensePosition);
        }
        return defensePositions;
    }
}
