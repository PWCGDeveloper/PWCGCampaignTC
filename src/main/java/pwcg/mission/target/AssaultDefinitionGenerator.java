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
import pwcg.mission.ground.BattleSize;

public class AssaultDefinitionGenerator
{
    public static final int DISTANCE_FROM_OBJECTIVE = 800;
    public static final int DISTANCE_BETWEEN_COMBATANTS = 200;

    private Campaign campaign;
    private MissionObjective objective;
    private AssaultDefinition assaultDefinition = new AssaultDefinition();

    public AssaultDefinitionGenerator(Mission mission)
    {
        this.campaign = mission.getCampaign();
        this.objective = mission.getObjective();
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
        assaultDefinition.setAssaultPosition(assaultPosition);
        assaultDefinition.setDefensePosition(defensePosition);
        assaultDefinition.setBattleSize(battleSize);
        assaultDefinition.setAssaultingCountry(assaultingCountry);
        assaultDefinition.setDefendingCountry(defendingCountry);
    }

    private Coordinate getDefensePosition(ICountry assaultingCountry) throws PWCGException
    {
        double angleTowardsFront = calculateBattleOrientation(assaultingCountry.getSide());
        double distanceFromObjective = DISTANCE_FROM_OBJECTIVE + RandomNumberGenerator.getRandom(1200);

        Coordinate assaultDefensePosition = MathUtils.calcNextCoord(objective.getPosition(), angleTowardsFront, distanceFromObjective);
        return assaultDefensePosition;
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
