package pwcg.mission.ground.builder;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.GroundUnitCollectionData;
import pwcg.mission.ground.org.GroundUnitCollectionType;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.target.AssaultDefinition;
import pwcg.mission.target.AssaultDefinitionGenerator;
import pwcg.mission.target.TargetType;

public class AssaultFixedUnitSegmentBuilder
{
    public static GroundUnitCollection generateAssault(Mission mission) throws PWCGException
    {
        GroundUnitCollectionData groundUnitCollectionData = new GroundUnitCollectionData(
                GroundUnitCollectionType.INFANTRY_GROUND_UNIT_COLLECTION, "Battle", TargetType.TARGET_INFANTRY, Coalition.getCoalitions());

        GroundUnitCollection battleUnitCollection = new GroundUnitCollection(mission.getCampaign(), "Assault", groundUnitCollectionData);

        int numFixedSegments = calcNumFixedSegments(mission.getCampaign());
        for (int i = 0; i < numFixedSegments; ++i)
        {
            AssaultDefinitionGenerator assaultDefinitionGenerator = new AssaultDefinitionGenerator(mission, i);
            AssaultDefinition assaultDefinition = assaultDefinitionGenerator.generateAssaultDefinition();

            List<IGroundUnit> primaryAssaultSegmentGroundUnits = new ArrayList<>();

            GroundUnitCollection fixedBattleSegmentUnits = buildFixedUnits(mission, assaultDefinition, primaryAssaultSegmentGroundUnits);
            battleUnitCollection.merge(fixedBattleSegmentUnits);
        }
        
        battleUnitCollection.finishGroundUnitCollection();

        return battleUnitCollection;
    }

    public static int calcNumFixedSegments(Campaign campaign) throws PWCGException
    {
        ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
        String currentGroundSetting = configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigGroundKey);
        if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_HIGH))
        {
            return 2;
        }
        else if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_MED))
        {
            return 4;
        }
        else
        {
            return 6;
        }
    }

    private static GroundUnitCollection buildFixedUnits(
            Mission mission, 
            AssaultDefinition assaultDefinition,
            List<IGroundUnit> primaryAssaultSegmentGroundUnits) throws PWCGException
    {
        AssaultFixedUnitBuilder assaultUnitBuilder = new AssaultFixedUnitBuilder(mission, assaultDefinition);
        GroundUnitCollection assaultUnits = assaultUnitBuilder.generateAssaultUnits();
        primaryAssaultSegmentGroundUnits.add(assaultUnits.getPrimaryGroundUnit());
        mission.registerAssault(assaultDefinition);
        return assaultUnits;
    }
}
