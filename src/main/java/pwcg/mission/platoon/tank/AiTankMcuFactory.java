package pwcg.mission.platoon.tank;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.tank.TankTypeInformation;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.vehicle.VehicleDefinition;
import pwcg.mission.ground.vehicle.VehicleDefinitionManager;
import pwcg.mission.platoon.PlatoonInformation;

public class AiTankMcuFactory
{
    private PlatoonInformation platoonInformation;

    public AiTankMcuFactory(PlatoonInformation platoonInformation)
    {
        this.platoonInformation = platoonInformation;
    }

    public List<TankMcu> createTanksForPlatoon(int numTanks) throws PWCGException
    {
        TankTypeInformation tankTypeForPlatoon = getTankTypeForPlatoon();

        VehicleDefinitionManager vehicleDefinitionManager = PWCGContext.getInstance().getVehicleDefinitionManager();
        List<TankMcu> tanksForPlatoon = new ArrayList<>();
        for (int index = 0; index < numTanks; ++index)
        {
            VehicleDefinition vehicleDefinitionToBuildTankFrom = vehicleDefinitionManager.getVehicleDefinition(tankTypeForPlatoon.getType());

            TankMcu tank = new TankMcu(vehicleDefinitionToBuildTankFrom, tankTypeForPlatoon, platoonInformation.getCountry());
            if (index > 0)
            {
                TankMcu leadTank = tanksForPlatoon.get(0);
                tank.setTarget(leadTank.getLinkTrId());
            }
            tanksForPlatoon.add(tank);
        }

        initializeTankParameters(tanksForPlatoon);
        return tanksForPlatoon;
    }

    private TankTypeInformation getTankTypeForPlatoon() throws PWCGException
    {
        AiTankTypeChooser tankTypeChooser = new AiTankTypeChooser(platoonInformation);
        return tankTypeChooser.getTankTypeForPlatoon();
    }

    private void initializeTankParameters(List<TankMcu> tanksForPlatoon) throws PWCGException
    {
        int numInFormation = 1;
        for (TankMcu tank : tanksForPlatoon)
        {
            setPlaceInFormation(numInFormation, tank);
            setAiSkillLevelForTank(tank, numInFormation);
            ++numInFormation;
        }
    }

    private void setPlaceInFormation(int numInFormation, TankMcu aiTank)
    {
        aiTank.setNumberInFormation(numInFormation);
    }

    private void setAiSkillLevelForTank(TankMcu tank, int numInFormation) throws PWCGException
    {
        AiSkillLevel aiLevel = TankAilevelFactory.setAiSkillLevelForTank(numInFormation, platoonInformation.getCountry());
        tank.setAiLevel(aiLevel);

    }
}
