package pwcg.mission.ground.unittypes.artillery;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.org.GroundUnit;
import pwcg.mission.ground.org.GroundUnitNumberCalculator;
import pwcg.mission.ground.vehicle.VehicleClass;

public class GroundAAArtilleryBattery extends GroundUnit
{
    static private int AA_ARTY_ATTACK_AREA = 5000;


    public GroundAAArtilleryBattery(GroundUnitInformation pwcgGroundUnitInformation)
    {
        super(VehicleClass.AAAArtillery, pwcgGroundUnitInformation);
    }

    @Override
    public void createGroundUnit() throws PWCGException 
    {
        super.createSpawnTimer();
        List<Coordinate> vehicleStartPositions = createVehicleStartPositions();
        super.createVehicles(vehicleStartPositions);
        addAspects();
        super.linkElements();
    }

    private List<Coordinate> createVehicleStartPositions() throws PWCGException 
    {
        List<Coordinate> spawnerLocations = new ArrayList<>();
        
        int numAAA = calcNumUnits();
        for (int i = 0; i < numAAA; ++i)
        {
            Coordinate spawnPosition = groundUnitInformation.getPosition().copy();
            if (numAAA == 1)
            {
                spawnPosition = groundUnitInformation.getPosition().copy();
            }
            else
            {
                if (i == 0)
                {
                    spawnPosition.setXPos(spawnPosition.getXPos() + 200);
                }
                else if (i == 1)
                {
                    spawnPosition.setXPos(spawnPosition.getXPos() - 200);
                }
                else if (i == 2)
                {
                    spawnPosition.setZPos(spawnPosition.getZPos() - 200);             
                }
                else if (i == 3)
                {
                    spawnPosition.setZPos(spawnPosition.getZPos() + 200);             
                }
            }
            spawnerLocations.add(spawnPosition);
        }
        return spawnerLocations;
    }

    private int calcNumUnits() throws PWCGException
    {
        return GroundUnitNumberCalculator.calcNumUnits(1, 2);
    }

    private void addAspects() throws PWCGException
    {
        super.addAAAFireAspect(AA_ARTY_ATTACK_AREA);
    }
}
