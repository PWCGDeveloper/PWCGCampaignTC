package pwcg.mission.unit.defense;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.unit.ITankPlatoon;
import pwcg.mission.unit.IUnitPackage;
import pwcg.mission.unit.PlatoonInformation;

public class DefensePackage implements IUnitPackage

{
    private List<ITankPlatoon> packageUnits = new ArrayList<>();

    @Override
    public List<ITankPlatoon> createUnitPackage (PlatoonInformation platoonInformation) throws PWCGException 
    {

        ITankPlatoon unit = new DefenseUnit (platoonInformation);
        unit.createUnit();

        packageUnits.add(unit);
        return packageUnits;
    }
}
