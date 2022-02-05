package pwcg.mission.target;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.MissionObjective;
import pwcg.mission.ground.builder.BattleSize;

public class AssaultDefinitionGenerator
{
    public static final int DISTANCE_FROM_OBJECTIVE = 2500;
    public static final int DISTANCE_BETWEEN_COMBATANTS = 600;
    public static final int UNIT_FRONTAGE = 2500;

    private Campaign campaign;
    private int segmentNumber;
    private MissionObjective objective;
    private AssaultDefinition assaultDefinition = new AssaultDefinition();

    public AssaultDefinitionGenerator(Mission mission, int segmentNumber)
    {
        this.campaign = mission.getCampaign();
        this.objective = mission.getObjective();
        this.segmentNumber = segmentNumber;
    }

    public AssaultDefinition generateAssaultDefinition() throws PWCGException
    {
        generateMiniAssaultOnEachIndex();
        return assaultDefinition;
    }

    private void generateMiniAssaultOnEachIndex() throws PWCGException
    {
        BattleSize battleSize = AssaultBattleSizeGenerator.createAssaultBattleSize(campaign);
        ICountry defendingCountry = objective.getDefendingCountry();
        ICountry assaultingCountry = objective.getAssaultingCountry();

        Coordinate defensePosition = getDefensePosition(defendingCountry);
        Coordinate assaultPosition = getAssaultPositionAcrossFromAssaultingUnit(assaultingCountry.getSide(), defensePosition);
        completeBattleDefinition(defendingCountry, assaultingCountry, battleSize, defensePosition, assaultPosition);
    }

    private void completeBattleDefinition(ICountry defendingCountry, ICountry assaultingCountry, BattleSize battleSize, Coordinate defensePosition, Coordinate assaultPosition) throws PWCGException
    {
        assaultDefinition.setObjectivePosition(objective.getPosition().copy());
        assaultDefinition.setAssaultPosition(assaultPosition);
        assaultDefinition.setDefensePosition(defensePosition);
        assaultDefinition.setBattleSize(battleSize);
        assaultDefinition.setAssaultingCountry(assaultingCountry);
        assaultDefinition.setDefendingCountry(defendingCountry);
    }

    private Coordinate getDefensePosition(ICountry assaultingCountry) throws PWCGException
    {
        double angleTowardsFront = calculateBattleOrientation(assaultingCountry.getSide());
        double distanceFromObjective = DISTANCE_FROM_OBJECTIVE + RandomNumberGenerator.getRandom(500);

        Coordinate defensePosition = MathUtils.calcNextCoord(objective.getPosition(), angleTowardsFront, distanceFromObjective);
        defensePosition = offsetForSegmentNumber(assaultingCountry, defensePosition);
        return defensePosition;
    }

    private Coordinate offsetForSegmentNumber(ICountry assaultingCountry, Coordinate defensePosition) throws PWCGException
    {
        double angleTowardsFront = calculateBattleOrientation(assaultingCountry.getSide());
        double angleForSegment = MathUtils.adjustAngle(angleTowardsFront, 90);
        if(segmentNumber%2 > 0)
        {
            angleForSegment = MathUtils.adjustAngle(angleTowardsFront, 270);
        }
        Coordinate segmentDefensePosition = MathUtils.calcNextCoord(defensePosition, angleForSegment, AssaultDefinitionGenerator.UNIT_FRONTAGE + 100);
        return segmentDefensePosition;
    }

    private Coordinate getAssaultPositionAcrossFromAssaultingUnit(Side assaultingSide, Coordinate defensePosition) throws PWCGException
    {
        double angleTowardsFront = calculateBattleOrientation(assaultingSide);
        Coordinate assaultStartPosition = MathUtils.calcNextCoord(defensePosition, angleTowardsFront, DISTANCE_BETWEEN_COMBATANTS);
        return assaultStartPosition;
    }

    private double calculateBattleOrientation(Side assaultingSide) throws PWCGException
    {
        FrontLinesForMap frontLines = PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
        int closestFrontLines = frontLines.findIndexForClosestPosition(objective.getPosition(), assaultingSide);
        Coordinate frontLinePosition = frontLines.getCoordinates(closestFrontLines, assaultingSide);
        
        double angleTowardsFront = MathUtils.calcAngle(objective.getPosition(), frontLinePosition);
        return angleTowardsFront;
    }
}
