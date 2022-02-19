package pwcg.campaign.resupply.depot;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.tank.TankTypeInformation;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.RandomNumberGenerator;

public class EquipmentWeightCalculator
{
    private Date campaignDate;
    private Map<String, Integer> weightedTankOdds = new HashMap<>();

    public EquipmentWeightCalculator (Date campaignDate)
    {
        this.campaignDate = campaignDate;
    }

    public void determineTankWeightsForPlayerTanks(List<TankTypeInformation> tankTypes) throws PWCGException
    {
        for (TankTypeInformation tankType : tankTypes)
        {
            if(tankType.isPlayer())
            {
                Integer tankWeight = determineTankWeight(tankType);
                weightedTankOdds.put(tankType.getType(), tankWeight);
            }
        }
    }

    public void determineTankWeightsForAiTanks(List<TankTypeInformation> tankTypes) throws PWCGException
    {
        for (TankTypeInformation tankType : tankTypes)
        {
            Integer tankWeight = determineTankWeight(tankType);
            weightedTankOdds.put(tankType.getType(), tankWeight);
        }
    }

    public String getTankTypeFromWeight()
    {
        int totalWeight = determineTotalWeight(weightedTankOdds);
        int accumulatedWeight = 0;
        int roll = RandomNumberGenerator.getRandom(totalWeight);
        String tankTypeDefault = "";
        for (String tankTypeName : weightedTankOdds.keySet())
        {
            tankTypeDefault = tankTypeName;

            Integer weightForThisTankType = weightedTankOdds.get(tankTypeName);
            accumulatedWeight += weightForThisTankType;
            if (roll < accumulatedWeight)
            {
                return tankTypeName;
            }
        }

        return tankTypeDefault;
    }

    private int determineTotalWeight(Map<String, Integer> weightedPlaneOdds)
    {
        int totalWeight = 0;
        for (Integer weight : weightedPlaneOdds.values())
        {
            totalWeight += weight;
        }
        return totalWeight;
    }

    private Integer determineTankWeight(TankTypeInformation tankType) throws PWCGException
    {
        Integer daysSinceIntroduction = DateUtils.daysDifference(tankType.getIntroduction(), campaignDate);
        Integer weight = daysSinceIntroduction;
        if (weight > 100)
        {
            weight = 100;
        }

        return weight;
    }

    public Map<String, Integer> getWeightedPlaneOdds()
    {
        return weightedTankOdds;
    }
}
