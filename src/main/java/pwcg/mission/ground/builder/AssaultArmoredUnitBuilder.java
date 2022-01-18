package pwcg.mission.ground.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitInformationFactory;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.GroundUnitCollectionData;
import pwcg.mission.ground.org.GroundUnitCollectionType;
import pwcg.mission.ground.org.GroundUnitType;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.unittypes.infantry.AssaultGroundUnitFactory;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.target.AssaultDefinition;
import pwcg.mission.target.AssaultDefinitionGenerator;
import pwcg.mission.target.TargetType;

public class AssaultArmoredUnitBuilder
{
    private Mission mission;
    private AssaultDefinition assaultDefinition;
    private AssaultGroundUnitFactory assaultFactory =  new AssaultGroundUnitFactory();

    private GroundUnitCollection battleSegmentUnitCollection;
   
    public AssaultArmoredUnitBuilder(Mission mission, AssaultDefinition assaultDefinition)
	{
        this.mission = mission;
        this.assaultDefinition = assaultDefinition;
        
        GroundUnitCollectionData groundUnitCollectionData = new GroundUnitCollectionData(
                GroundUnitCollectionType.INFANTRY_GROUND_UNIT_COLLECTION, 
                "Battle Segment", 
                TargetType.TARGET_INFANTRY,
                Coalition.getCoalitions());

        this.battleSegmentUnitCollection = new GroundUnitCollection (mission.getCampaign(), "Assault Segment", groundUnitCollectionData);
	}


    public GroundUnitCollection generateArmoredAssaultUnit() throws PWCGException
    {
        createAssaultArmoredUnits();
        createDefenders();        
        return battleSegmentUnitCollection;
    }

    /////////////////////////////////////////   Assault  ///////////////////////////////////

    private void createAssaultArmoredUnit() throws PWCGException
    {
        assaultingTanks();
    }
    
    private void assaultingTanks() throws PWCGException
    {         
        Coordinate tankAssaultStartPosition = MathUtils.calcNextCoord(
                assaultDefinition.getDefensePosition(), 
                assaultDefinition.getTowardsAttackerOrientation().getyOri(), AssaultDefinitionGenerator.DISTANCE_BETWEEN_COMBATANTS + 1000.0);  
        
        GroundUnitInformation groundUnitInformation = buildAssaultGroundUnitInformation(tankAssaultStartPosition, "Tank", TargetType.TARGET_ARMOR);
        IGroundUnit assaultTankUnit = assaultFactory.createAssaultTankUnit (groundUnitInformation);
        battleSegmentUnitCollection.addGroundUnit(assaultTankUnit);
    }

    private GroundUnitInformation buildAssaultGroundUnitInformation(Coordinate unitPosition, String unitName,TargetType targetType) throws PWCGException
    {        
        ArmoredAssaultRouteBuilder assaultRouteBuilder = new ArmoredAssaultRouteBuilder(
                mission, battleSegmentUnitCollection, assaultDefinition.getAssaultingCountry().getSide().getOppositeSide());
        
        Map<Integer, List<Coordinate>> assaultRoutes= assaultRouteBuilder.buildAssaultRoutesForArmor(numTankPlatoons);
        
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                mission.getCampaign(), 
                assaultDefinition.getAssaultingCountry(),
                targetType, 
                unitPosition, 
                waypointCoordinates, 
                assaultDefinition.getTowardsDefenderOrientation());
        
        return groundUnitInformation;
    }

    
    /////////////////////////////////////////   Defense  ///////////////////////////////////


    private void createDefenders() throws PWCGException
    {
        defendingTanks();
    }

    private GroundUnitInformation buildDefenseGroundUnitInformation(Coordinate unitPosition, String unitName,TargetType targetType) throws PWCGException
    {
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                mission.getCampaign(), 
                assaultDefinition.getDefendingCountry(),
                targetType, 
                unitPosition, 
                assaultDefinition.getAssaultPosition(), 
                assaultDefinition.getTowardsAttackerOrientation());
        return groundUnitInformation;
    }

    public IGroundUnit getPrimaryGroundUnit() throws PWCGException
    {
        for (IGroundUnit groundUnit : battleSegmentUnitCollection.getGroundUnits())
        {
            if (groundUnit.getName().endsWith("Machine Gun"))
            {
                return groundUnit;
            }
        }
        
        return battleSegmentUnitCollection.getGroundUnits().get(0);
    }

    private void defendingTanks() throws PWCGException
    {         
        Coordinate tankDefenseStartPosition = MathUtils.calcNextCoord(
                assaultDefinition.getAssaultPosition(), 
                assaultDefinition.getTowardsDefenderOrientation().getyOri(), AssaultDefinitionGenerator.DISTANCE_BETWEEN_COMBATANTS + 1000.0);  
        
        GroundUnitInformation groundUnitInformation = buildAssaultGroundUnitInformation(tankDefenseStartPosition, "Tank", TargetType.TARGET_ARMOR);
        IGroundUnit assaultTankUnit = assaultFactory.createAssaultTankUnit (groundUnitInformation);
        battleSegmentUnitCollection.addGroundUnit(assaultTankUnit);
    }
}
