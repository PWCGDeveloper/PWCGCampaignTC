package pwcg.mission.ground.builder;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.GroundUnitCollectionData;
import pwcg.mission.ground.org.GroundUnitCollectionType;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.target.FrontSegmentDefinition;
import pwcg.mission.target.FrontSegmentDefinitionGenerator;
import pwcg.mission.target.TargetType;

public class FrontFixedUnitSegmentsBuilder
{
    public static GroundUnitCollection generateAssault(Mission mission) throws PWCGException
    {
        GroundUnitCollectionData groundUnitCollectionData = new GroundUnitCollectionData(
                GroundUnitCollectionType.INFANTRY_GROUND_UNIT_COLLECTION, "Battle", TargetType.TARGET_INFANTRY, Coalition.getCoalitions());

        GroundUnitCollection battleUnitCollection = new GroundUnitCollection(mission.getCampaign(), "Assault Fixed Units", groundUnitCollectionData);

        int numFixedSegments = calcNumFixedSegments(mission);
        for (int i = 0; i < numFixedSegments; ++i)
        {
            FrontSegmentDefinitionGenerator assaultDefinitionGenerator = new FrontSegmentDefinitionGenerator(mission, i);
            FrontSegmentDefinition assaultDefinition = assaultDefinitionGenerator.generateBattleDefinition();

            List<IGroundUnit> primaryAssaultSegmentGroundUnits = new ArrayList<>();

            GroundUnitCollection fixedBattleSegmentUnits = buildFixedUnits(mission, assaultDefinition, primaryAssaultSegmentGroundUnits);
            battleUnitCollection.merge(fixedBattleSegmentUnits);
        }
        
        battleUnitCollection.finishGroundUnitCollection();

        return battleUnitCollection;
    }

    public static int calcNumFixedSegments(Mission mission) throws PWCGException
    {
        if (mission.getObjective().getBattleSize() == BattleSize.BATTLE_SIZE_TINY)
        {
            return 2;
        }
        else if (mission.getObjective().getBattleSize() == BattleSize.BATTLE_SIZE_SKIRMISH)
        {
            return 4;
        }
        else if (mission.getObjective().getBattleSize() == BattleSize.BATTLE_SIZE_ASSAULT)
        {
            return 6;
        }
        else if (mission.getObjective().getBattleSize() == BattleSize.BATTLE_SIZE_OFFENSIVE)
        {
            return 8;
        }
        
        return 4;
    }

    private static GroundUnitCollection buildFixedUnits(
            Mission mission, 
            FrontSegmentDefinition assaultDefinition,
            List<IGroundUnit> primaryAssaultSegmentGroundUnits) throws PWCGException
    {
        FrontFixedUnitBuilder assaultUnitBuilder = new FrontFixedUnitBuilder(mission, assaultDefinition);
        GroundUnitCollection assaultUnits = assaultUnitBuilder.generateAssaultUnits();
        primaryAssaultSegmentGroundUnits.add(assaultUnits.getPrimaryGroundUnit());
        mission.registerAssault(assaultDefinition);
        return assaultUnits;
    }
}
