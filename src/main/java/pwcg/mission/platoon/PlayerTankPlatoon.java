package pwcg.mission.platoon;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.platoon.tank.PlayerTankMcuFactory;
import pwcg.mission.platoon.tank.TankMcu;

public class PlayerTankPlatoon extends TankPlatoon
{
    public PlayerTankPlatoon(PlatoonInformation platoonInformation)
    {
        super(platoonInformation);
    }

    protected void buildTanks() throws PWCGException
    {
        int numTanks = platoonInformation.getParticipatingPlayersForCompany().size();
        if(numTanks < 4)
        {
            numTanks = 4;
        }
                
        PlayerTankMcuFactory tankMcuFactory = new PlayerTankMcuFactory(platoonInformation);        
        List<TankMcu> tanks = tankMcuFactory.createTanksForUnit(numTanks);
        platoonVehicles.setTanks(tanks);
        platoonVehicles.setFuelForUnit(1.0);
    }

    @Override
    public boolean isPlayerPlatoon()
    {
        return true;
    }
}
