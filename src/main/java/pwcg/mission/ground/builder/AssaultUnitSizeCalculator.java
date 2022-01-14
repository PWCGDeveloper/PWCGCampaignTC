package pwcg.mission.ground.builder;

import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.org.GroundUnitNumberCalculator;

public class AssaultUnitSizeCalculator
{

    protected int calcNumUnits(GroundUnitInformation groundUnitInformation) throws PWCGException
    {
        if (groundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_TINY)
        {
            return GroundUnitNumberCalculator.calcNumUnits(1, 1);
        }
        else if (groundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_LOW)
        {
            return GroundUnitNumberCalculator.calcNumUnits(2, 3);
        }
        else if (groundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_MEDIUM)
        {
            return GroundUnitNumberCalculator.calcNumUnits(3, 6);
        }
        else if (groundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_HIGH)
        {
            return GroundUnitNumberCalculator.calcNumUnits(5, 10);
        }
        
        throw new PWCGException ("No unit size provided for ground unit");
    }

}
