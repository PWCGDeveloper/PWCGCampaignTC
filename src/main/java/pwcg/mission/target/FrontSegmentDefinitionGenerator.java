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

public class FrontSegmentDefinitionGenerator
{
    public static final int DISTANCE_FROM_OBJECTIVE = 4000;
    public static final int DISTANCE_FROM_OBJECTIVE_RANDOM = 500;
    public static final int DISTANCE_BETWEEN_COMBATANTS = 400;
    public static final int DISTANCE_BETWEEN_COMBATANTS_RANDOM = 1600;
    public static final int DISTANCE_FOR_AT_GUNS = 600;
    public static final int DISTANCE_FOR_ARTILLERY = 3000;
    public static final int DISTANCE_FOR_AAA_MG = 800;
    public static final int DISTANCE_FOR_AAA_ARTILLERY = 2000;
    public static final int UNIT_FRONTAGE = 2000;

    private Campaign campaign;
    private int segmentNumber;
    private MissionObjective objective;
    private FrontSegmentDefinition frontSegmentDefinition = new FrontSegmentDefinition();

    public FrontSegmentDefinitionGenerator(Mission mission, int segmentNumber)
    {
        this.campaign = mission.getCampaign();
        this.objective = mission.getObjective();
        this.segmentNumber = segmentNumber;
    }

    public FrontSegmentDefinition generateBattleDefinition() throws PWCGException
    {
        ICountry defendingCountry = objective.getDefendingCountry();
        ICountry assaultingCountry = objective.getAssaultingCountry();

        Coordinate defensePosition = getDefensePosition(defendingCountry);
        Coordinate assaultPosition = getAssaultPositionAcrossFromAssaultingUnit(assaultingCountry.getSide(), defensePosition);
        completeBattleDefinition(defendingCountry, assaultingCountry, defensePosition, assaultPosition);

        return frontSegmentDefinition;
    }

    private void completeBattleDefinition(ICountry defendingCountry, ICountry assaultingCountry, Coordinate defensePosition, Coordinate assaultPosition) throws PWCGException
    {
        frontSegmentDefinition.setAssaultPosition(assaultPosition);
        frontSegmentDefinition.setDefensePosition(defensePosition);
        frontSegmentDefinition.setAssaultingCountry(assaultingCountry);
        frontSegmentDefinition.setDefendingCountry(defendingCountry);
    }

    private Coordinate getDefensePosition(ICountry assaultingCountry) throws PWCGException
    {
        double angleTowardsFront = calculateBattleOrientation(assaultingCountry.getSide());
        double distanceFromObjective = DISTANCE_FROM_OBJECTIVE + RandomNumberGenerator.getRandom(DISTANCE_FROM_OBJECTIVE_RANDOM);

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
        Coordinate segmentDefensePosition = MathUtils.calcNextCoord(defensePosition, angleForSegment, FrontSegmentDefinitionGenerator.UNIT_FRONTAGE + 500);
        return segmentDefensePosition;
    }

    private Coordinate getAssaultPositionAcrossFromAssaultingUnit(Side assaultingSide, Coordinate defensePosition) throws PWCGException
    {
        double angleTowardsFront = calculateBattleOrientation(assaultingSide);
        Coordinate assaultStartPosition = MathUtils.calcNextCoord(defensePosition, angleTowardsFront, getDisdtanceBetweenCombatants());
        return assaultStartPosition;
    }
    
    private int getDisdtanceBetweenCombatants()
    {
        return DISTANCE_BETWEEN_COMBATANTS + (RandomNumberGenerator.getRandom(DISTANCE_BETWEEN_COMBATANTS_RANDOM));
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
