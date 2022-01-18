package pwcg.mission.ground.unittypes.artillery;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.org.GroundUnit;
import pwcg.mission.ground.org.GroundUnitNumberCalculator;
import pwcg.mission.ground.vehicle.VehicleClass;

public class GroundAAMachineGunBattery extends GroundUnit
{
    static private int AA_MG_ATTACK_AREA = 800;

    public GroundAAMachineGunBattery(GroundUnitInformation pwcgGroundUnitInformation)
    {
        super(VehicleClass.AAAMachineGun, pwcgGroundUnitInformation);
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

    protected List<Coordinate> createVehicleStartPositions() throws PWCGException 
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
                    spawnPosition.setXPos(spawnPosition.getXPos() + 100);
                }
                else if (i == 1)
                {
                    spawnPosition.setXPos(spawnPosition.getXPos() - 100);
                }
                else if (i == 2)
                {
                    spawnPosition.setZPos(spawnPosition.getZPos() - 100);             
                }
                else if (i == 3)
                {
                    spawnPosition.setZPos(spawnPosition.getZPos() + 100);             
                }
            }
            spawnerLocations.add(spawnPosition);
        }
        return spawnerLocations;
    }

    protected int calcNumUnits() throws PWCGException
    {
        return GroundUnitNumberCalculator.calcNumUnits(1, 2);
    }

    protected void addAspects() throws PWCGException
    {
        super.addAAAFireAspect( AA_MG_ATTACK_AREA);
    }
}
