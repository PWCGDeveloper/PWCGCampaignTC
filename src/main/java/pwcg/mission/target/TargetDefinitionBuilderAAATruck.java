package pwcg.mission.target;

import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.unit.UnitInformation;

public class TargetDefinitionBuilderAAATruck implements ITargetDefinitionBuilder
{
    private UnitInformation unitInformation;

    public TargetDefinitionBuilderAAATruck(UnitInformation unitInformation) throws PWCGException
    {
        this.unitInformation = unitInformation;
    }

    public TargetDefinition buildTargetDefinition() throws PWCGException
    {

        Coordinate truckCoordinate = unitInformation.getMission().getUnits().getReferencePlayerUnit().getLeadVehicle().getPosition();
        IGroundUnit targetGroundUnit = getBestTargetGroundUnit(unitInformation.getCountry().getSide().getOppositeSide(), truckCoordinate);

        TargetDefinition targetDefinition = new TargetDefinition(targetGroundUnit.getVehicleClass().getTargetType(), truckCoordinate, targetGroundUnit.getCountry(), targetGroundUnit.getVehicleClass().getName());
        return targetDefinition;
    }


    private IGroundUnit getBestTargetGroundUnit(Side enemyUnitSide, Coordinate truckPosition) throws PWCGException
    {
        double closestUnitDistance = 1000000000.0;
        IGroundUnit unitClosestToMissionCenter = null;
        GroundUnitCollection assault = unitInformation.getMission().getGroundUnitBuilder().getAssault();
        for (IGroundUnit friendlyGroundUnit : assault.getGroundUnitsForSide(enemyUnitSide))
        {
            double distanceBetweenCenterAndUnit = MathUtils.calcDist(truckPosition, friendlyGroundUnit.getPosition());
            if (distanceBetweenCenterAndUnit < closestUnitDistance)
            {
                closestUnitDistance = distanceBetweenCenterAndUnit;
                unitClosestToMissionCenter = friendlyGroundUnit;
            }
        }

        return unitClosestToMissionCenter;
    }

}
