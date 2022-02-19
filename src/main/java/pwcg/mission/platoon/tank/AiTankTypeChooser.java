package pwcg.mission.platoon.tank;

import java.util.Date;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.resupply.depot.EquipmentWeightCalculator;
import pwcg.campaign.tank.PwcgRoleCategory;
import pwcg.campaign.tank.TankTypeInformation;
import pwcg.core.exception.PWCGException;
import pwcg.mission.platoon.PlatoonInformation;

public class AiTankTypeChooser
{
    private PlatoonInformation platoonInformation;

    public AiTankTypeChooser(PlatoonInformation platoonInformation)
    {
        this.platoonInformation = platoonInformation;
    }

    public TankTypeInformation getTankTypeForPlatoon() throws PWCGException
    {
        Date date = platoonInformation.getCampaign().getDate();
        Side side = platoonInformation.getCompany().getCountry().getSide();
        PwcgRoleCategory rolecategory = platoonInformation.getCompany().getCompanyPrimaryRoleForMission(date);
        List<TankTypeInformation> tankTypesForPlatoon = PWCGContext.getInstance().getAiTankTypeFactory().getTanksForRoleCategory(
                date, side, rolecategory);

        return chooseTankType(tankTypesForPlatoon);
    }

    private TankTypeInformation chooseTankType(List<TankTypeInformation> tankTypesForPlatoon) throws PWCGException
    {
        EquipmentWeightCalculator equipmentWeightCalculator = new EquipmentWeightCalculator(platoonInformation.getCampaign().getDate());
        equipmentWeightCalculator.determineTankWeightsForAiTanks(tankTypesForPlatoon);
        String tankTypeName = equipmentWeightCalculator.getTankTypeFromWeight();

        return getSelectedTankType(tankTypesForPlatoon, tankTypeName);
    }

    private TankTypeInformation getSelectedTankType(List<TankTypeInformation> tankTypesForPlatoon, String tankTypeName)
    {
        for (TankTypeInformation tankType : tankTypesForPlatoon)
        {
            if (tankType.getType().equals(tankTypeName))
            {
                return tankType;
            }
        }
        return null;
    }
}
