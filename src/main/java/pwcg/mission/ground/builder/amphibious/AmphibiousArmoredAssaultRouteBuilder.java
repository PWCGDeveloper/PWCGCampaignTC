package pwcg.mission.ground.builder.amphibious;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.ground.builder.ArmoredAssaultPlatoonRouteBuilder;
import pwcg.mission.ground.builder.ArmoredStartPositionBuilder;
import pwcg.mission.ground.org.GroundUnitType;
import pwcg.mission.platoon.ITankPlatoon;

public class AmphibiousArmoredAssaultRouteBuilder
{
    private Mission mission;
    private Side assaultingSide;
    private Map<Integer, List<Coordinate>> assaultRoutes = new HashMap<>();

    public AmphibiousArmoredAssaultRouteBuilder(Mission mission)
    {
        this.mission = mission;
        this.assaultingSide = mission.getObjective().getAssaultingCountry().getSide();
    }

    public Map<Integer, List<Coordinate>> buildAssaultRoutesForArmor(List<ITankPlatoon> platoons) throws PWCGException
    {
        List<ITankPlatoon> assaultingPlatoons = getAssaultingCompanies(platoons);
        List<Coordinate> startPositions = getStartPositions(assaultingPlatoons);

        for(int i = 0; i < assaultingPlatoons.size(); ++i)
        {
            ITankPlatoon platoon = assaultingPlatoons.get(i);
            Coordinate startPosition = startPositions.get(i);
            ArmoredAssaultPlatoonRouteBuilder platoonRouteBuilder = new ArmoredAssaultPlatoonRouteBuilder(mission);
            List<Coordinate> assaultRoute = platoonRouteBuilder.buildRoutesForArmor(platoon, startPosition);
            assaultRoutes.put(assaultingPlatoons.get(i).getIndex(), assaultRoute);
        }

        return assaultRoutes;
    }

    private List<ITankPlatoon> getAssaultingCompanies(List<ITankPlatoon> platoons)
    {
        List<ITankPlatoon> assaultingPlatoons = new ArrayList<>();
        for (ITankPlatoon platoon : platoons)
        {
            if (platoon.getPlatoonInformation().getCountry().getSide() == assaultingSide)
            {
                assaultingPlatoons.add(platoon);
            }
        }
        return assaultingPlatoons;
    }

    private List<Coordinate> getStartPositions(List<ITankPlatoon> assaultingPlatoons) throws PWCGException
    {
        ArmoredStartPositionBuilder startPositionBuilder = new ArmoredStartPositionBuilder(mission);
        return startPositionBuilder.buildStartPositionsForArmor(assaultingPlatoons, GroundUnitType.INFANTRY_UNIT);
    }
}
