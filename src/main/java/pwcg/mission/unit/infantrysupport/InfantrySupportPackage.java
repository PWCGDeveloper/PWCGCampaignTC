package pwcg.mission.unit.infantrysupport;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.unit.IPlayerUnit;
import pwcg.mission.unit.IUnitPackage;
import pwcg.mission.unit.UnitInformation;

public class InfantrySupportPackage implements IUnitPackage

{
    private List<IPlayerUnit> packageUnits = new ArrayList<>();

    @Override
    public List<IPlayerUnit> createUnitPackage (UnitInformation unitInformation) throws PWCGException 
    {

        IPlayerUnit patrolUnit = new InfantrySupportUnit (unitInformation);
        patrolUnit.createUnit();

        packageUnits.add(patrolUnit);
        return packageUnits;
    }
}
