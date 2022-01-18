package pwcg.mission.ground;

import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.ground.builder.AssaultArmoredPlatoonBuilder;
import pwcg.mission.ground.builder.AssaultFixedUnitSegmentBuilder;
import pwcg.mission.ground.builder.IBattleBuilder;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.GroundUnitCollectionData;
import pwcg.mission.ground.org.GroundUnitCollectionType;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.target.TargetType;

public class MissionBattleBuilder implements IBattleBuilder
{
    private Mission mission = null;

    public MissionBattleBuilder (Mission mission)
    {
        this.mission = mission;
    }

    @Override
    public GroundUnitCollection generateBattle() throws PWCGException
    {
        GroundUnitCollection battleUnitCollection = buildFullBattleCollection();

        GroundUnitCollection assaultFixedUnitCollection = AssaultFixedUnitSegmentBuilder.generateAssault(mission);
        battleUnitCollection.merge(assaultFixedUnitCollection);

        GroundUnitCollection assaultArmoredUnitCollection = AssaultArmoredPlatoonBuilder.generateAssault(mission, assaultFixedUnitCollection);
        battleUnitCollection.merge(assaultArmoredUnitCollection);

        GroundUnitCollection defendingArmoredUnitCollection = DefendingArmoredUnitSegmentBuilder.generateAssault(mission, mission.getObjective().getPosition());
        battleUnitCollection.merge(defendingArmoredUnitCollection);
        
        return battleUnitCollection;
    }

    private GroundUnitCollection buildFullBattleCollection()
    {
        GroundUnitCollectionData groundUnitCollectionData = new GroundUnitCollectionData(
                GroundUnitCollectionType.INFANTRY_GROUND_UNIT_COLLECTION, 
                "Battle", 
                TargetType.TARGET_INFANTRY,
                Coalition.getCoalitions());

        GroundUnitCollection battleUnitCollection = new GroundUnitCollection (mission.getCampaign(), "Assault", groundUnitCollectionData);
        return battleUnitCollection;
    }
}
