package pwcg.mission.unit.aaa;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.unit.ITankUnit;
import pwcg.mission.unit.IUnitPackage;
import pwcg.mission.unit.UnitInformation;

public class AAAPackage implements IUnitPackage

{
    private List<ITankUnit> packageUnits = new ArrayList<>();

    @Override
    public List<ITankUnit> createUnitPackage (UnitInformation unitInformation) throws PWCGException 
    {

        ITankUnit patrolUnit = new AAAUnit (unitInformation);
        patrolUnit.createUnit();

        packageUnits.add(patrolUnit);
        return packageUnits;
    }
}
