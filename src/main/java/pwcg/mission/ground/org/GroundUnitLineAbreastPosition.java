package pwcg.mission.ground.org;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.ground.GroundUnitInformation;

public class GroundUnitLineAbreastPosition
{

    public static List<Coordinate> createVehicleStartPositions(GroundUnitInformation groundUnitInformation, 
                                                               int numVehicles, 
                                                               int unitFrontage) throws PWCGException 
    {
        List<Coordinate> spawnerLocations = new ArrayList<>();

        // Face towards enemy
        double facingAngle = MathUtils.calcAngle(groundUnitInformation.getPosition(), groundUnitInformation.getDestination());
        Orientation atGunOrient = new Orientation();
        atGunOrient.setyOri(facingAngle);
        
        // MGs are behind the lines
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
            spawnerLocations.add(vehicleCoords);
        }
        return spawnerLocations;       
    }   

}
