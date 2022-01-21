package pwcg.mission.platoon.tank;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class TankAilevelFactory
{    

    public static AiSkillLevel setAiSkillLevelForTank(int numInFormation, ICountry country) throws PWCGException
    {
        int aiLevel = calculateAiLevelForNumberInFormation(numInFormation);
        aiLevel = adjustAilevelForCountry(country, aiLevel);
        return AiSkillLevel.createAiSkilLLevel(aiLevel);
    }

    private static int calculateAiLevelForNumberInFormation(int numInFormation)
    {
        int aiLevel = AiSkillLevel.COMMON.getAiSkillLevel();
        if(numInFormation <= 1)
        {
            aiLevel = AiSkillLevel.VETERAN.getAiSkillLevel();
            int roll = RandomNumberGenerator.getRandom(100);
            if (roll < 20)
            {
                aiLevel = AiSkillLevel.ACE.getAiSkillLevel();
            }
        }
        else if(numInFormation == 2)
        {
            aiLevel = AiSkillLevel.COMMON.getAiSkillLevel();
            int roll = RandomNumberGenerator.getRandom(100);
            if (roll < 30)
            {
                aiLevel = AiSkillLevel.VETERAN.getAiSkillLevel();
            }
        }
        else
        {
            aiLevel = AiSkillLevel.NOVICE.getAiSkillLevel();
            int roll = RandomNumberGenerator.getRandom(100);
            if (roll < 30)
            {
                aiLevel = AiSkillLevel.COMMON.getAiSkillLevel();
            }
        }
        return aiLevel;
    }

    private static int adjustAilevelForCountry(ICountry country, int aiLevel)
    {
        int upgradeChance = 30;
        if(country.getCountry() == Country.GERMANY)
        {
            upgradeChance = 50;
        }
        else if(country.getCountry() == Country.RUSSIA)
        {
            upgradeChance = 0;
        }

        int roll = RandomNumberGenerator.getRandom(100);
        if (roll < upgradeChance)
        {
            ++aiLevel;
        }
        
        if (aiLevel < AiSkillLevel.NOVICE.getAiSkillLevel())
        {
            aiLevel = AiSkillLevel.NOVICE.getAiSkillLevel();
        }
        
        if (aiLevel > AiSkillLevel.ACE.getAiSkillLevel())
        {
            aiLevel = AiSkillLevel.ACE.getAiSkillLevel();
        }
        return aiLevel;
    }
 }
