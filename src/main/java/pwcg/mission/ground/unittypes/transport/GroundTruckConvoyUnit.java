package pwcg.mission.ground.unittypes.transport;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.org.GroundUnit;
import pwcg.mission.ground.org.GroundUnitNumberCalculator;
import pwcg.mission.ground.vehicle.VehicleClass;

public class GroundTruckConvoyUnit extends GroundUnit
{
    public GroundTruckConvoyUnit(GroundUnitInformation pwcgGroundUnitInformation)
    {
        super(VehicleClass.Truck, pwcgGroundUnitInformation);
    }   

    @Override
    public void createGroundUnit() throws PWCGException 
    {
        super.createSpawnTimer();
        int numvehicles = calcNumUnits();
        List<Coordinate> vehicleStartPositions = createVehicleStartPositions(numvehicles);
        super.createVehicles(vehicleStartPositions);
        List<Coordinate> destinations =  createVehicleDestinationPositions(numvehicles);
        addAspects(destinations);
        super.linkElements();
    }

    private int calcNumUnits() throws PWCGException
    {
        return GroundUnitNumberCalculator.calcNumUnits(2, 4);
    }

    private List<Coordinate> createVehicleStartPositions(int numvehicles) throws PWCGException 
    {
        return createVehiclePositions(groundUnitInformation.getPosition().copy(), numvehicles);        
    }

    private List<Coordinate> createVehicleDestinationPositions(int numvehicles) throws PWCGException 
    {
        return createVehiclePositions(groundUnitInformation.getDestination(), numvehicles);
    }

    private List<Coordinate> createVehiclePositions(Coordinate firstVehicleCoordinate, int numvehicles) throws PWCGException
    {
        double placementOrientation = MathUtils.adjustAngle (groundUnitInformation.getOrientation().getyOri(), 180);       
        List<Coordinate> vehiclePositions = new ArrayList<>();
        Coordinate vehicleCoordinate = MathUtils.calcNextCoord(firstVehicleCoordinate, placementOrientation, 100.0);
        for (int i = 0; i < numvehicles; ++i)
        {   
            vehicleCoordinate = MathUtils.calcNextCoord(vehicleCoordinate.copy(), placementOrientation, 10.0);
            vehiclePositions.add(vehicleCoordinate);
        }       
        return vehiclePositions;
    }

    private void addAspects(List<Coordinate> destinations) throws PWCGException
    {
        int unitSpeed = 10;
        super.addMovementAspect(unitSpeed, destinations, GroundFormationType.FORMATION_TYPE_ON_ROAD);
    }
}	

