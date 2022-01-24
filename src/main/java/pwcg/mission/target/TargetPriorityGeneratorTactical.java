package pwcg.mission.target;

import java.util.Arrays;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightInformation;

public class TargetPriorityGeneratorTactical
{
    static final int MAX_WEIGHTED_ENTRIES = 1000;

    public static List<TargetType> getTargetTypePriorities(FlightInformation flightInformation) throws PWCGException
    {
        return Arrays.asList(TargetType.TARGET_INFANTRY);
    }
}
