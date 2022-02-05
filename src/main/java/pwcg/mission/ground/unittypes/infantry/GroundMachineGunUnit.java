package pwcg.mission.ground.unittypes.infantry;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.org.GroundUnit;
import pwcg.mission.ground.org.GroundUnitLineAbreastPosition;
import pwcg.mission.ground.org.GroundUnitNumberCalculator;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.target.FrontSegmentDefinitionGenerator;

public class GroundMachineGunUnit extends GroundUnit
{
    public GroundMachineGunUnit(GroundUnitInformation pwcgGroundUnitInformation)
    {
        super(VehicleClass.MachineGun, pwcgGroundUnitInformation);
    }   

    @Override
    public void createGroundUnit() throws PWCGException
    {
        super.createSpawnTimer();
        int numVehicles = calcNumUnits();
        List<Coordinate> vehicleStartPositions = GroundUnitLineAbreastPosition.createVehicleStartPositions(
                groundUnitInformation, numVehicles, FrontSegmentDefinitionGenerator.UNIT_FRONTAGE);
        super.createVehicles(vehicleStartPositions);
        addAspects();
        super.linkElements();
    }

    protected int calcNumUnits() throws PWCGException
    {
        return GroundUnitNumberCalculator.calcNumUnits(6, 10);
    }

    private void addAspects() throws PWCGException
    {
        super.addDirectFireAspect();
    }
}	
