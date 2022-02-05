package pwcg.mission.ground.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.GroundUnitType;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.platoon.ITankPlatoon;

public class ArmoredStartPositionBuilder
{
    private GroundUnitCollection assaultFixedUnitCollection;
    private Mission mission;
    private List<Coordinate> assaultStartPositions = new ArrayList<>();

    public ArmoredStartPositionBuilder(Mission mission)
    {
        this.mission = mission;
        this.assaultFixedUnitCollection = mission.getGroundUnitBuilder().getAssault();
    }

    public List<Coordinate> buildStartPositionsForArmor(List<ITankPlatoon> platoons, GroundUnitType groundUnitType) throws PWCGException
    {
        buildStartPositions(platoons, groundUnitType);
        return assaultStartPositions;
    }

    private void buildStartPositions(List<ITankPlatoon> platoons, GroundUnitType groundUnitType) throws PWCGException
    {
        List<IGroundUnit> positionsFriendlyAT = assaultFixedUnitCollection.getGroundUnitsByTypeAndSide(groundUnitType, platoons.get(0).getPlatoonInformation().getCountry().getSide());
        Collections.shuffle(positionsFriendlyAT);
        for (int i = 0; i < platoons.size(); ++i)
        {
            int iterationThroughPositions = i / positionsFriendlyAT.size() + 1;
            Coordinate startPositionAtFriendlyAT = positionsFriendlyAT.get(i % positionsFriendlyAT.size()).getPosition();
            Coordinate startPosition = moveStartPositionForwardFromAT(startPositionAtFriendlyAT, iterationThroughPositions);
            assaultStartPositions.add(startPosition);
        }
    }

    private Coordinate moveStartPositionForwardFromAT(Coordinate startPositionAtFriendlyAT, int iterationThroughPositions) throws PWCGException
    {
        Coordinate referenceObjectivePosition = mission.getObjective().getPosition().copy();
        double angleToObjective = MathUtils.calcAngle(startPositionAtFriendlyAT, referenceObjectivePosition);
        Coordinate startPosition = MathUtils.calcNextCoord(startPositionAtFriendlyAT, angleToObjective, 50 * iterationThroughPositions);
        return startPosition;
    }
}
