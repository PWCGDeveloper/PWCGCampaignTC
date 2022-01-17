package pwcg.mission.utils;

import org.junit.jupiter.api.Assertions;

import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.ground.GroundUnitPositionDuplicateDetector;

public class GroundUnitPositionVerifier
{
    public static void verifyGroundUnitPositionsAndAssert (Mission mission) throws PWCGException
    {
        verifyNoDUplicatePositions(mission);
    }

    private static void verifyNoDUplicatePositions(Mission mission) throws PWCGException
    {
        GroundUnitPositionDuplicateDetector duplicateDetector = new GroundUnitPositionDuplicateDetector();
        boolean noDuplicates = duplicateDetector.verifyMissionGroundUnitPositionsNotDuplicated(
                mission.getGroundUnitBuilder().getAllMissionGroundUnits(), mission.getGroundUnitBuilder().getAllMissionGroundUnits());
        Assertions.assertTrue (noDuplicates);
    }
}
