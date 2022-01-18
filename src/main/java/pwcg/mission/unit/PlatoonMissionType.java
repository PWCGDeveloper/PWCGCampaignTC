package pwcg.mission.unit;

import java.util.ArrayList;
import java.util.List;

public enum PlatoonMissionType
{
    ASSAULT(UnitMissionTypeCategory.TANK), 
    DEFENSE(UnitMissionTypeCategory.TANK), 
    INFANTRY_SUPPORT(UnitMissionTypeCategory.ASSAULT_GUN), 
    AAA(UnitMissionTypeCategory.AAA), 
    ANY(UnitMissionTypeCategory.INVALID);

    UnitMissionTypeCategory category = UnitMissionTypeCategory.INVALID;

    private PlatoonMissionType(UnitMissionTypeCategory category)
    {
        this.category = category;
    }

    public boolean isCategory(UnitMissionTypeCategory categoryToFind)
    {
        if (category == categoryToFind)
        {
            return true;
        }
        return false;
    }

    public static List<PlatoonMissionType> getUnitTypesByCategory(UnitMissionTypeCategory categoryToFind)
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
