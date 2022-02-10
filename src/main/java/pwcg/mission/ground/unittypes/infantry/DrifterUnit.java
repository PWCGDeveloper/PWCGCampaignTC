package pwcg.mission.ground.unittypes.infantry;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.org.GroundUnit;
import pwcg.mission.ground.org.GroundUnitNumberCalculator;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.ground.vehicle.VehicleDefinition;
import pwcg.mission.ground.vehicle.VehicleRequestDefinition;

public class DrifterUnit extends GroundUnit
{
    public DrifterUnit(GroundUnitInformation pwcgGroundUnitInformation)
    {
        super(VehicleClass.Drifter, pwcgGroundUnitInformation);
    }   

    @Override
    public void createGroundUnit() throws PWCGException
    {
        super.createSpawnTimer();
        
        VehicleRequestDefinition requestDefinition = new VehicleRequestDefinition(groundUnitInformation.getCountry().getCountry(), groundUnitInformation.getDate(), vehicleClass);
        VehicleDefinition vehicleDefinition = PWCGContext.getInstance().getVehicleDefinitionManager().getAiVehicleDefinitionForRequest(requestDefinition);

        List<Coordinate> vehicleStartPositions = createVehicleStartPositions(vehicleDefinition);
        super.createVehiclesFromDefinition(vehicleStartPositions, vehicleDefinition);
        super.linkElements();
    }

    private List<Coordinate> createVehicleStartPositions(VehicleDefinition vehicleDefinition) throws PWCGException 
    {
        List<Coordinate> spawnerLocations = new ArrayList<>();

        int numDrifter = calcNumUnits(vehicleDefinition);

        // Face towards orientation
        double drifterFacingAngle = MathUtils.adjustAngle(groundUnitInformation.getOrientation().getyOri(), 180.0);
        Orientation drifterOrient = new Orientation();
        drifterOrient.setyOri(drifterFacingAngle);
        
        Coordinate drifterCoords = groundUnitInformation.getPosition().copy();

        double drifterSpacing = 30.0;
        if (vehicleDefinition.getVehicleLength() > drifterSpacing)
        {
            drifterSpacing = vehicleDefinition.getVehicleLength();
        }
        
        // Direction in which subsequent units will be placed
        double placementOrientation = groundUnitInformation.getOrientation().getyOri();        

        for (int i = 0; i < numDrifter; ++i)
        {   
            drifterCoords.setYPos(0.0);
            spawnerLocations.add(drifterCoords);

            drifterCoords = MathUtils.calcNextCoord(drifterCoords, placementOrientation, drifterSpacing);
        }
        return spawnerLocations;       
    }

    private int calcNumUnits(VehicleDefinition vehicleDefinition) throws PWCGException
    {
        int numUnits = GroundUnitNumberCalculator.calcNumUnits(2, 3);
        return numUnits;
    }
}