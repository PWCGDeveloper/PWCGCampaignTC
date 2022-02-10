package pwcg.mission.platoon.tank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.tank.TankTypeInformation;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.ground.vehicle.VehicleDefinition;
import pwcg.mission.ground.vehicle.VehicleDefinitionManager;
import pwcg.mission.ground.vehicle.VehicleRequestDefinition;
import pwcg.mission.platoon.PlatoonInformation;

public class AiTankMcuFactory
{
    private PlatoonInformation platoonInformation;

    public AiTankMcuFactory(PlatoonInformation platoonInformation)
    {
        this.platoonInformation = platoonInformation;
    }

    public List<TankMcu> createTanksForUnit(int numTanks) throws PWCGException
    {
        String archTypeForPlatoon = getTankArchTypeForPlatoon();

        VehicleDefinitionManager vehicleDefinitionManager = PWCGContext.getInstance().getVehicleDefinitionManager();
        List<TankMcu> tanksForPlatoon = new ArrayList<>();
        for (int index = 0; index < numTanks; ++index)
        {
            TankTypeInformation tankTypeForPlatoon = getTankTypeForPlatoon(archTypeForPlatoon);
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

    private String getTankArchTypeForPlatoon() throws PWCGException
    {
        VehicleRequestDefinition requestDefinition = new VehicleRequestDefinition(platoonInformation.getCountry().getCountry(), platoonInformation.getCampaign().getDate(), VehicleClass.Tank);
        VehicleDefinition vehicleDefinitionForArchType = PWCGContext.getInstance().getVehicleDefinitionManager().getAiVehicleDefinitionForRequest(requestDefinition);
        TankTypeInformation tankTypeForArchType = PWCGContext.getInstance().getAiTankTypeFactory().createTankTypeByAnyName(vehicleDefinitionForArchType.getVehicleType());
        String archTypeForPlatoon = tankTypeForArchType.getArchType();
        return archTypeForPlatoon;
    }

    private TankTypeInformation getTankTypeForPlatoon(String archTypeForPlatoon) throws PWCGException
    {
        List<TankTypeInformation> tankTypesForArchType = PWCGContext.getInstance().getAiTankTypeFactory().createActiveTankTypesForArchType(archTypeForPlatoon, platoonInformation.getCampaign().getDate());
        Collections.shuffle(tankTypesForArchType);
        TankTypeInformation tankTypeForPlatoon = tankTypesForArchType.get(0);
        return tankTypeForPlatoon;
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
