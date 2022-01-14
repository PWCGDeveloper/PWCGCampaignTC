package pwcg.mission.ground.builder;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.GroundUnitCollectionData;
import pwcg.mission.ground.org.GroundUnitCollectionType;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.target.AssaultDefinition;
import pwcg.mission.target.AssaultDefinitionGenerator;
import pwcg.mission.target.TargetType;

public class AssaultBuilder
{
    public static GroundUnitCollection generateAssault(Mission mission, Coordinate assaultPosition) throws PWCGException
    {
        AssaultDefinitionGenerator assaultDefinitionGenerator = new AssaultDefinitionGenerator(mission);
        AssaultDefinition assaultDefinition = assaultDefinitionGenerator.generateAssaultDefinition();
        
        GroundUnitCollectionData groundUnitCollectionData = new GroundUnitCollectionData(
                GroundUnitCollectionType.INFANTRY_GROUND_UNIT_COLLECTION, 
                "Battle", 
                TargetType.TARGET_INFANTRY,
                Coalition.getCoalitions());

        List<IGroundUnit> primaryAssaultSegmentGroundUnits = new ArrayList<>();
        
        GroundUnitCollection assaultUnits = buildAssaultUnits(mission, assaultDefinition, primaryAssaultSegmentGroundUnits);

        GroundUnitCollection battleUnitCollection = new GroundUnitCollection (mission.getCampaign(), "Assault", groundUnitCollectionData);
        battleUnitCollection.merge(assaultUnits);
        battleUnitCollection.setPrimaryGroundUnit(primaryAssaultSegmentGroundUnits.get(0));
        battleUnitCollection.finishGroundUnitCollection();
        return battleUnitCollection;
    }

    private static GroundUnitCollection buildAssaultUnits(Mission mission, AssaultDefinition assaultDefinition, List<IGroundUnit> primaryAssaultSegmentGroundUnits) throws PWCGException
    {
        AssaultUnitBuilder assaultUnitBuilder = new AssaultUnitBuilder(mission, assaultDefinition);
        GroundUnitCollection assaultUnits = assaultUnitBuilder.generateAssaultUnits();
        primaryAssaultSegmentGroundUnits.add(assaultUnits.getPrimaryGroundUnit());
        mission.registerAssault(assaultDefinition);
        return assaultUnits;
    }
 }
