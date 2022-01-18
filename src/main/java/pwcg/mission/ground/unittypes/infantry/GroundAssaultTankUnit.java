package pwcg.mission.ground.unittypes.infantry;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.org.GroundUnit;
import pwcg.mission.ground.org.GroundUnitNumberCalculator;
import pwcg.mission.ground.vehicle.VehicleClass;

public class GroundAssaultTankUnit extends GroundUnit
{
    public GroundAssaultTankUnit(GroundUnitInformation pwcgGroundUnitInformation)
    {
        super(VehicleClass.Tank, pwcgGroundUnitInformation);
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
        return GroundUnitNumberCalculator.calcNumUnits(6, 10);
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
        double tankFacingAngle = MathUtils.calcAngle(groundUnitInformation.getPosition(), groundUnitInformation.getDestination());
        double placementOrientation = MathUtils.adjustAngle (tankFacingAngle, 90.0);        
        
        double tankSpacing = 75.0;
        Coordinate tankCoords = firstVehicleCoordinate.copy();
        List<Coordinate> vehicleLocations = new ArrayList<>();
		for (int i = 0; i < numvehicles; ++i)
		{	
            vehicleLocations.add(tankCoords);
			tankCoords = MathUtils.calcNextCoord(tankCoords, placementOrientation, tankSpacing);
		}
        return vehicleLocations;		
	}

    private void addAspects(List<Coordinate> destinations) throws PWCGException
    {       
        super.addDirectFireAspect();
        
        int unitSpeed = 10;
        super.addMovementAspect(unitSpeed, destinations, GroundFormationType.FORMATION_TYPE_OFF_ROAD);
    }
}	

