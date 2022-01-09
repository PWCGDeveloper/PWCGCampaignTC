package pwcg.mission.unit;

import java.util.List;

import pwcg.core.exception.PWCGException;

public interface IUnitPackage
{
    List<IPlayerUnit> createUnitPackage(UnitInformation unitInformation) throws PWCGException;
}
