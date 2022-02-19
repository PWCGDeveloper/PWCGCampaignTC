package pwcg.mission.platoon;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.platoon.tank.AiTankMcuFactory;
import pwcg.mission.platoon.tank.TankMcu;

public class AiPlatoon extends TankPlatoon
{
    public AiPlatoon(PlatoonInformation platoonInformation)
    {
        super(platoonInformation);
    }

    @Override
    protected void buildTanks() throws PWCGException
    {
        int numTanks = 4;
        int roll = RandomNumberGenerator.getRandom(100);
        if(roll < 30)
        {
            numTanks = 3;
        }

        AiTankMcuFactory tankMcuFactory = new AiTankMcuFactory(platoonInformation);
        List<TankMcu> tanks = tankMcuFactory.createTanksForPlatoon(numTanks);
        platoonVehicles.setTanks(tanks);
        platoonVehicles.setFuelForUnit(1.0);
    }

    @Override
    public boolean isPlayerPlatoon()
    {
        return false;
    }
}
