package pwcg.mission.unit;

import java.util.List;

import pwcg.core.exception.PWCGException;

public interface IUnitPackage
{
    List<ITankPlatoon> createUnitPackage(PlatoonInformation platoonInformation) throws PWCGException;
}
