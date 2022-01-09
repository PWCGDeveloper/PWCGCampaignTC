package pwcg.mission.unit.defense;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.unit.IPlayerUnit;
import pwcg.mission.unit.IUnitPackage;
import pwcg.mission.unit.UnitInformation;

public class DefensePackage implements IUnitPackage

{
    private List<IPlayerUnit> packageUnits = new ArrayList<>();

    @Override
    public List<IPlayerUnit> createUnitPackage (UnitInformation unitInformation) throws PWCGException 
    {

        IPlayerUnit unit = new DefenseUnit (unitInformation);
        unit.createUnit();

        packageUnits.add(unit);
        return packageUnits;
    }
}
