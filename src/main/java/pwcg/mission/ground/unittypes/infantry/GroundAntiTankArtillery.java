package pwcg.mission.ground.unittypes.infantry;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.org.GroundUnit;
import pwcg.mission.ground.org.GroundUnitLineAbreastStaggeredPosition;
import pwcg.mission.ground.org.GroundUnitNumberCalculator;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.target.FrontSegmentDefinitionGenerator;

public class GroundAntiTankArtillery extends GroundUnit
{
    public GroundAntiTankArtillery(GroundUnitInformation pwcgGroundUnitInformation)
    {
        super(VehicleClass.ArtilleryAntiTank, pwcgGroundUnitInformation);
    }   

    @Override
    public void createGroundUnit() throws PWCGException
    {
        super.createSpawnTimer();
        int numVehicles = calcNumUnits();
        List<Coordinate> vehicleStartPositions = GroundUnitLineAbreastStaggeredPosition.createVehicleStartPositions(
                groundUnitInformation, numVehicles, FrontSegmentDefinitionGenerator.UNIT_FRONTAGE);
        super.createVehicles(vehicleStartPositions);
        addAspects();
        super.linkElements();
    }

    private int calcNumUnits() throws PWCGException
    {
        return GroundUnitNumberCalculator.calcNumUnits(4, 6);
    }

    private void addAspects() throws PWCGException
    {
        super.addDirectFireAspect();
    }
}	
