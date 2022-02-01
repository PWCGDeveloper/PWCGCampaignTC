package pwcg.mission.io;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;

public class MissionFlightWriter 
{
    private Mission mission = null;

	public MissionFlightWriter (Mission mission)
	{
		this.mission = mission;
	}
	
	public void writeFlights(BufferedWriter writer) throws PWCGException
	{
        for (IFlight flight : mission.getFlights().getAiFlights())
        {
            flight.write(writer);
        }
	}
}
