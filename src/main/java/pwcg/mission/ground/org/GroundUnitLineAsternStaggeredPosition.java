package pwcg.mission.ground.org;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.ground.GroundUnitInformation;

public class GroundUnitLineAsternStaggeredPosition
{

    public static List<Coordinate> createVehicleStartPositions(GroundUnitInformation groundUnitInformation, 
                                                               int numVehicles) throws PWCGException 
    {
        List<Coordinate> spawnerLocations = new ArrayList<>();

        // Face towards enemy
        double facingAngle = MathUtils.calcAngle(groundUnitInformation.getPosition(), groundUnitInformation.getDestination());
        Orientation atGunOrient = new Orientation();
        atGunOrient.setyOri(facingAngle);
        
        double initialPlacementAngle = MathUtils.adjustAngle (facingAngle, 180.0);      
        Coordinate startCoords = MathUtils.calcNextCoord(groundUnitInformation.getPosition(), initialPlacementAngle, 25.0);
        
        // Direction in which subsequent units will be placed
        double unitSpacing = 25;
        double lineAsternOrientation = initialPlacementAngle;             
        for (int i = 0; i < numVehicles; ++i)
        {
            Coordinate vehicleCoords = MathUtils.calcNextCoord(startCoords, lineAsternOrientation, unitSpacing * i);
            // Stagger every other unit
            if (i%2 != 0)
            {
                double angleToSide = MathUtils.adjustAngle(lineAsternOrientation, 90);
                vehicleCoords = MathUtils.calcNextCoord(vehicleCoords, angleToSide, 10.0);
            }

            spawnerLocations.add(vehicleCoords);
        }
        return spawnerLocations;       
    }   

}
