package pwcg.mission.unit.aaa;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.unit.ITankPlatoon;
import pwcg.mission.unit.IUnitPackage;
import pwcg.mission.unit.PlatoonInformation;

public class AAAPackage implements IUnitPackage

{
    private List<ITankPlatoon> packageUnits = new ArrayList<>();

    @Override
    public List<ITankPlatoon> createUnitPackage (PlatoonInformation platoonInformation) throws PWCGException 
    {

        ITankPlatoon patrolUnit = new AAAUnit (platoonInformation);
        patrolUnit.createUnit();

        packageUnits.add(patrolUnit);
        return packageUnits;
    }
}
