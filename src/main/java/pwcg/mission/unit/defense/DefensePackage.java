package pwcg.mission.unit.defense;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.unit.ITankUnit;
import pwcg.mission.unit.IUnitPackage;
import pwcg.mission.unit.UnitInformation;

public class DefensePackage implements IUnitPackage

{
    private List<ITankUnit> packageUnits = new ArrayList<>();

    @Override
    public List<ITankUnit> createUnitPackage (UnitInformation unitInformation) throws PWCGException 
    {

        ITankUnit unit = new DefenseUnit (unitInformation);
        unit.createUnit();

        packageUnits.add(unit);
        return packageUnits;
    }
}
