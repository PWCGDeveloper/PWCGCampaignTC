package pwcg.gui.rofmap.debrief;

import pwcg.core.location.Coordinate;

public class DebriefMapPoint
{
	enum EventTypes
	{
		TAKEOFF,
		LANDING,
		DAMAGE,
		CRASH,
		CREW,
		WP;
	}
	
	public Coordinate coord = new Coordinate();
	public String desc;
	public EventTypes eventType;
}
