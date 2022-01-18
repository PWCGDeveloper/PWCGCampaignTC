package pwcg.mission.ground.unittypes.artillery;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.org.GroundAspectAreaFire;
import pwcg.mission.ground.org.GroundUnit;
import pwcg.mission.ground.org.GroundUnitElement;
import pwcg.mission.ground.org.GroundUnitLineAbreastPosition;
import pwcg.mission.ground.org.GroundUnitNumberCalculator;
import pwcg.mission.ground.org.IGroundAspect;
import pwcg.mission.ground.vehicle.VehicleClass;

public class GroundArtilleryBattery extends GroundUnit
{
    private static final int ARTILLERY_SPACING = 40;
    
    public GroundArtilleryBattery(GroundUnitInformation pwcgGroundUnitInformation)
    {
        super(VehicleClass.ArtilleryHowitzer, pwcgGroundUnitInformation);
    }   

    @Override
    public void createGroundUnit() throws PWCGException 
    {
        super.createSpawnTimer();
        int numVehicles = calcNumUnits();
        List<Coordinate> vehicleStartPositions = GroundUnitLineAbreastPosition.createVehicleStartPositions(
                groundUnitInformation, numVehicles, (numVehicles + 2) * ARTILLERY_SPACING);
        super.createVehicles(vehicleStartPositions);
        addAspects();
        super.linkElements();
    }
    
    private int calcNumUnits() throws PWCGException
    {
        return GroundUnitNumberCalculator.calcNumUnits(3, 5);
    }

    private void addAspects() throws PWCGException
    {
        super.addIndirectFireAspect();
    }

    public void setTargetPosition(Coordinate targetPosition)
    {
        for (GroundUnitElement groundElement : this.getGroundElements())
        {
            for (IGroundAspect aspect : groundElement.getAspectsOfGroundUnit())
            {
                if (aspect instanceof GroundAspectAreaFire)
                {
                    GroundAspectAreaFire areaFireElement = (GroundAspectAreaFire)aspect;
                    areaFireElement.setTargetPosition(targetPosition);
                }
            }
        }
    }
}	

