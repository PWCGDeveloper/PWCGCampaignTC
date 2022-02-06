package pwcg.mission.ground.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.ground.org.GroundUnitType;
import pwcg.mission.platoon.ITankPlatoon;

public class ArmoredDefenseRouteBuilder
{
    private Mission mission;
    private Side defendingSide;
    private Map<Integer, List<Coordinate>> defenseRoutes = new HashMap<>();

    public ArmoredDefenseRouteBuilder(Mission mission)
    {
        this.mission = mission;
        this.defendingSide = mission.getObjective().getDefendingCountry().getSide();
    }

    public Map<Integer, List<Coordinate>> buildAssaultRoutesForArmor(List<ITankPlatoon> platoons) throws PWCGException
    {
        List<ITankPlatoon> defendingPlatoons = getPlatoonsForSide(platoons, defendingSide);
        List<Coordinate> startPositions = getStartPositions(defendingPlatoons);

        for(int i = 0; i < defendingPlatoons.size(); ++i)
        {
            ITankPlatoon platoon = defendingPlatoons.get(i);
            Coordinate startPosition = startPositions.get(i);
            ArmoredDefensePlatoonRouteBuilder platoonRouteBuilder = new ArmoredDefensePlatoonRouteBuilder(mission);
            List<Coordinate> assaultRoute = platoonRouteBuilder.buildRoutesForArmor(platoon, startPosition);
            defenseRoutes.put(defendingPlatoons.get(i).getIndex(), assaultRoute);
        }

        return defenseRoutes;
    }

    private List<Coordinate> getStartPositions(List<ITankPlatoon> defendingPlatoons) throws PWCGException
    {
        ArmoredStartPositionBuilder startPositionBuilder = new ArmoredStartPositionBuilder(mission);
        return startPositionBuilder.buildStartPositionsForArmor(defendingPlatoons, GroundUnitType.ARTILLERY_UNIT);
    }

    private List<ITankPlatoon> getPlatoonsForSide(List<ITankPlatoon> platoons, Side side)
    {
        List<ITankPlatoon> defendingPlatoons = new ArrayList<>();
        for(ITankPlatoon platoon : platoons)
        {
            if(platoon.getPlatoonInformation().getCountry().getSide() == side)
            {
                defendingPlatoons.add(platoon);
            }
        }
        return defendingPlatoons;
    }
}
