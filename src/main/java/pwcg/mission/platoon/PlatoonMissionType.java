package pwcg.mission.platoon;

import java.util.ArrayList;
import java.util.List;

public enum PlatoonMissionType
{
    ASSAULT(PlatoonMissionTypeCategory.TANK), 
    COUNTER_ATTACK(PlatoonMissionTypeCategory.TANK), 
    INFANTRY_SUPPORT(PlatoonMissionTypeCategory.ASSAULT_GUN), 
    DEFENSE(PlatoonMissionTypeCategory.ASSAULT_GUN), 
    AAA(PlatoonMissionTypeCategory.AAA), 
    ANY(PlatoonMissionTypeCategory.INVALID);

    PlatoonMissionTypeCategory category = PlatoonMissionTypeCategory.INVALID;

    private PlatoonMissionType(PlatoonMissionTypeCategory category)
    {
        this.category = category;
    }

    public boolean isCategory(PlatoonMissionTypeCategory categoryToFind)
    {
        if (category == categoryToFind)
        {
            return true;
        }
        return false;
    }

    public static List<PlatoonMissionType> getUnitTypesByCategory(PlatoonMissionTypeCategory categoryToFind)
    {
        List<PlatoonMissionType> unitTypesByCategory = new ArrayList<>();
        for (PlatoonMissionType unitType : PlatoonMissionType.values())
        {
            if (unitType.category == categoryToFind)
            {
                unitTypesByCategory.add(unitType);
            }
        }
        return unitTypesByCategory;
    }
}
