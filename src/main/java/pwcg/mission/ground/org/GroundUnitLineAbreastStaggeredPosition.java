package pwcg.mission.ground.org;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.ground.GroundUnitInformation;

public class GroundUnitLineAbreastStaggeredPosition
{

    public static List<Coordinate> createVehicleStartPositions(GroundUnitInformation groundUnitInformation, 
            int numVehicles, 
            int unitFrontage) throws PWCGException 
    {
        List<Coordinate> spawnerLocations = new ArrayList<>();

        // Face towards enemy
        double facingAngle = getFacingAngle(groundUnitInformation);
        
        // Units are behind the lines
        double initialPlacementAngle = MathUtils.adjustAngle (facingAngle, 180.0);      
        Coordinate lineCoords = MathUtils.calcNextCoord(groundUnitInformation.getPosition(), initialPlacementAngle, 25.0);

        // Locate the target such that startCoords is the edge of the unit frontage
        double alongLineOrientation = MathUtils.adjustAngle (facingAngle, 270);             
        double unitSpacing = unitFrontage / numVehicles + 2;
        Coordinate startCoords = MathUtils.calcNextCoord(lineCoords, alongLineOrientation, (unitFrontage / 2));       
        
        // Direction in which subsequent units will be placed
        double placementOrientation = MathUtils.adjustAngle (facingAngle, 90.0);        
        for (int i = 0; i < numVehicles; ++i)
        {   
            Coordinate vehicleCoords = MathUtils.calcNextCoord(startCoords, placementOrientation, unitSpacing * i);
            vehicleCoords = adjustAlongFront(groundUnitInformation, vehicleCoords, numVehicles, unitFrontage);
            vehicleCoords = adjustDepth(groundUnitInformation, vehicleCoords);
            spawnerLocations.add(vehicleCoords);
        }
        return spawnerLocations;       
    }

    private static double getFacingAngle(GroundUnitInformation groundUnitInformation) throws PWCGException
    {
        double facingAngle = MathUtils.calcAngle(groundUnitInformation.getPosition(), groundUnitInformation.getDestination());
        Orientation atGunOrient = new Orientation();
        atGunOrient.setyOri(facingAngle);
        return facingAngle;
    }

    private static Coordinate adjustAlongFront(GroundUnitInformation groundUnitInformation, Coordinate vehicleCoords, int numVehicles, int unitFrontage) throws PWCGException
    {
        int frontagePerUnit = unitFrontage / numVehicles;
        int maxMovement = frontagePerUnit / 2;
        int movement = RandomNumberGenerator.getRandom(maxMovement);
        double moveUnitOrientation = getMovementAngleAlongFront(groundUnitInformation);
        Coordinate vehicleCoordsMovedSideToSide = MathUtils.calcNextCoord(vehicleCoords, moveUnitOrientation, movement);       

        return vehicleCoordsMovedSideToSide;
    }   

    private static Coordinate adjustDepth(GroundUnitInformation groundUnitInformation, Coordinate vehicleCoords) throws PWCGException
    {
        double moveUnitOrientation = getMovementAngleForDepth(groundUnitInformation);
        int movement = RandomNumberGenerator.getRandom(300);
        Coordinate vehicleCoordsForDepth = MathUtils.calcNextCoord(vehicleCoords, moveUnitOrientation, movement);       
        return vehicleCoordsForDepth;
    }

    private static double getMovementAngleForDepth(GroundUnitInformation groundUnitInformation) throws PWCGException
    {
        double moveUnitOrientation = getFacingAngle(groundUnitInformation);
        int roll = RandomNumberGenerator.getRandom(100);
        if(roll < 50)
        {
            moveUnitOrientation = MathUtils.adjustAngle(moveUnitOrientation, 180);
        }
        return moveUnitOrientation;
    }

    private static double getMovementAngleAlongFront(GroundUnitInformation groundUnitInformation) throws PWCGException
    {
        double facingAngle = getFacingAngle(groundUnitInformation);
        int roll = RandomNumberGenerator.getRandom(100);
        double moveUnitOrientation = MathUtils.adjustAngle (facingAngle, 270);             
        if(roll < 50)
        {
            moveUnitOrientation = MathUtils.adjustAngle (facingAngle, 90.0);        
        }
        return moveUnitOrientation;
    }

}
