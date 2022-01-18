package pwcg.mission.unit.assault;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.unit.ITankPlatoon;
import pwcg.mission.unit.IUnitPackage;
import pwcg.mission.unit.PlatoonInformation;

public class AssaultPackage implements IUnitPackage

{
    private List<ITankPlatoon> packageUnits = new ArrayList<>();

    @Override
    public List<ITankPlatoon> createUnitPackage (PlatoonInformation platoonInformation) throws PWCGException 
    {

        ITankPlatoon patrolUnit = new AssaultUnit (platoonInformation);
        patrolUnit.createUnit();

        packageUnits.add(patrolUnit);
        return packageUnits;
    }
}
