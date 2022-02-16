package pwcg.gui.rofmap.brief;

import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.platoon.ITankPlatoon;

public class BriefingMapPlatoonMapper
{
    private BriefingMapPanel mapPanel;

    public BriefingMapPlatoonMapper(BriefingMapPanel mapPanel) throws PWCGException
    {
        this.mapPanel = mapPanel;
    }

    public void mapRequestedFlights(Mission mission) throws PWCGException
    {
        mapPlatoons(mission);
    }

    private void mapPlatoons(Mission mission) throws PWCGException
    {
        mapPanel.clearVirtualPoints();
        for (ITankPlatoon playerPlatoon : mission.getPlatoons().getPlatoons())
        {
            mapPanel.makeMapPanelAiPoints(playerPlatoon);
        }
    }
}
