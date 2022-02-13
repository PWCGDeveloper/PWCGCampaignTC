package pwcg.gui.rofmap.brief;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.CoordinateBox;
import pwcg.gui.rofmap.brief.model.BriefingPlatoon;
import pwcg.mission.Mission;
import pwcg.mission.platoon.ITankPlatoon;

public class BriefingMapPlatoonMapper
{
    private BriefingPlatoon briefingMissionHandler;
    private BriefingMapPanel mapPanel;

    public BriefingMapPlatoonMapper(BriefingPlatoon briefingMissionHandler, BriefingMapPanel mapPanel) throws PWCGException
    {
        this.briefingMissionHandler = briefingMissionHandler;
        this.mapPanel = mapPanel;
    }

    public void mapRequestedFlights() throws PWCGException
    {
        mapPlatoons();
    }

    private void mapPlatoons() throws PWCGException
    {
        Mission mission = briefingMissionHandler.getMission();
        mapPanel.clearVirtualPoints();
        for (ITankPlatoon playerPlatoon : mission.getPlatoons().getAiPlatoons())
        {
            mapPanel.makeMapPanelAiPoints(playerPlatoon);
        }
    }
}
