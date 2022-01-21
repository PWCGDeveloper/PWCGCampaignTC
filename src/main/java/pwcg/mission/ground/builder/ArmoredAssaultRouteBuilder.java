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

public class ArmoredAssaultRouteBuilder
{
    private GroundUnitCollection assaultFixedUnitCollection;
    private Mission mission;
    private Side assaultingSide;
    private Map<Integer, List<Coordinate>> assaultRoutes = new HashMap<>();

    public ArmoredAssaultRouteBuilder(Mission mission)
    {
        this.mission = mission;
        this.assaultFixedUnitCollection = mission.getGroundUnitBuilder().getAssault();
        this.assaultingSide = mission.getObjective().getAssaultingCountry().getSide();
    }

    public Map<Integer, List<Coordinate>> buildAssaultRoutesForArmor(List<ITankPlatoon> platoons) throws PWCGException
    {
        List<ITankPlatoon> assaultingPlatoons = getAssaultingCompanies(platoons);
        int numTankPlatoons = assaultingPlatoons.size();
        
        List<Coordinate> startPositions = getStartPositions(numTankPlatoons);
        List<Coordinate> firstTargetPositions = getFirstTargetPositions(numTankPlatoons);
        List<Coordinate> secondTargetPositions = getSecondTargetPositions(numTankPlatoons);
        List<Coordinate> objectivePositions = getObjectiveTargetPositions(numTankPlatoons, startPositions.get(0));

        for(int i = 0; i < numTankPlatoons; ++i)
        {
            List<Coordinate> assaultRoute = new ArrayList<>();
            assaultRoute.add(startPositions.get(i));
            assaultRoute.add(firstTargetPositions.get(i));
            assaultRoute.add(secondTargetPositions.get(i));
            assaultRoute.add(objectivePositions.get(i));
            
            assaultRoutes.put(assaultingPlatoons.get(i).getIndex(),  assaultRoute);
        }
        
        return assaultRoutes;
    }

    private List<ITankPlatoon> getAssaultingCompanies(List<ITankPlatoon> platoons)
    {
        List<ITankPlatoon> assaultingPlatoons = new ArrayList<>();
        for(ITankPlatoon platoon : platoons)
        {
            if(platoon.getPlatoonInformation().getCountry().getSide() == assaultingSide)
            {
                assaultingPlatoons.add(platoon);
            }
        }
        return assaultingPlatoons;
    }

    private List<Coordinate> getStartPositions(int numTankPlatoons) throws PWCGException
    {
        ArmoredAssaultTargetFinder targetFinderInfantry = new ArmoredAssaultTargetFinder(
                mission, assaultFixedUnitCollection, assaultingSide, GroundUnitType.ANTI_TANK_UNIT);
        
        List<Coordinate> startPositionsFriendlyAT = targetFinderInfantry.findInitialTargetForTankPlatoon(numTankPlatoons);        
        List<Coordinate> startPositions = moveStartPositionForwardFromAT(startPositionsFriendlyAT);
        return startPositions;
    }

    private List<Coordinate> moveStartPositionForwardFromAT(List<Coordinate> startPositionsFriendlyAT) throws PWCGException
    {
        List<Coordinate> startPositions = new ArrayList<>();
        for(Coordinate startPositiontAT : startPositionsFriendlyAT)
        {
            Coordinate referenceObjectivePosition = mission.getObjective().getPosition().copy();
            double angleToObjective = MathUtils.calcAngle(startPositiontAT, referenceObjectivePosition);
            Coordinate startPosition = MathUtils.calcNextCoord(startPositiontAT, angleToObjective, 50);
            startPositions.add(startPosition);
        }
        return startPositions;
    }

    private List<Coordinate> getFirstTargetPositions(int numTankPlatoons) throws PWCGException
    {
        ArmoredAssaultTargetFinder targetFinderInfantry = new ArmoredAssaultTargetFinder(
                mission, assaultFixedUnitCollection, assaultingSide.getOppositeSide(), GroundUnitType.INFANTRY_UNIT);
        
        List<Coordinate> positionNearFriendlyATGuns = targetFinderInfantry.findInitialTargetForTankPlatoon(numTankPlatoons);
        
        Coordinate referenceObjectivePosition = mission.getObjective().getPosition().copy();
        double angleTowardsObjective = MathUtils.calcAngle(positionNearFriendlyATGuns.get(0), referenceObjectivePosition);
        List<Coordinate> startPositions = new ArrayList<>();
        for(Coordinate positionNearFriendlyATGun : positionNearFriendlyATGuns)
        {
            Coordinate startPosition = MathUtils.calcNextCoord(positionNearFriendlyATGun, angleTowardsObjective, 50);
            startPositions.add(startPosition);
        }

        return startPositions;
    }

    private List<Coordinate> getSecondTargetPositions(int numTankPlatoons) throws PWCGException
    {
        ArmoredAssaultTargetFinder targetFinderInfantry = new ArmoredAssaultTargetFinder(
                mission, assaultFixedUnitCollection, assaultingSide, GroundUnitType.ANTI_TANK_UNIT);
        
        List<Coordinate> targetPositions = targetFinderInfantry.findInitialTargetForTankPlatoon(numTankPlatoons);
        return targetPositions;
    }

    private List<Coordinate> getObjectiveTargetPositions(int numTankPlatoons, Coordinate linePosition) throws PWCGException
    {
        Coordinate referenceObjectivePosition = mission.getObjective().getPosition().copy();
        double angleBeyondObjective = MathUtils.calcAngle(linePosition, referenceObjectivePosition);
        referenceObjectivePosition = MathUtils.calcNextCoord(referenceObjectivePosition, angleBeyondObjective, 100);

        double objeciveSpacing = 200.0;
        double objectiveSpreadInMeters = (numTankPlatoons * 200 / 2);
        double angleLeftOfObjective = MathUtils.adjustAngle(angleBeyondObjective, 270);
        double angleRightOfObjective = MathUtils.adjustAngle(angleBeyondObjective, 90);
        
        List<Coordinate> objectivePositions = new ArrayList<>();
        Coordinate firstObjectivePosition = MathUtils.calcNextCoord(referenceObjectivePosition, angleLeftOfObjective, objectiveSpreadInMeters);
        objectivePositions.add(firstObjectivePosition);
        
        for(int i = 1; i < numTankPlatoons; ++i)
        {
            Coordinate nextObjectivePosition = MathUtils.calcNextCoord(firstObjectivePosition, angleRightOfObjective, (objeciveSpacing * i));
            objectivePositions.add(nextObjectivePosition);
        }
        return objectivePositions;
    }
}
