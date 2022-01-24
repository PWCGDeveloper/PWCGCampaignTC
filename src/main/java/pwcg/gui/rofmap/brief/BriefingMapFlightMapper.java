package pwcg.gui.rofmap.brief;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.CoordinateBox;
import pwcg.gui.rofmap.brief.model.BriefingUnit;
import pwcg.mission.Mission;
import pwcg.mission.platoon.ITankPlatoon;

public class BriefingMapFlightMapper
{
    private BriefingUnit briefingMissionHandler;
    private BriefingMapPanel mapPanel;

    public BriefingMapFlightMapper(BriefingUnit briefingMissionHandler, BriefingMapPanel mapPanel) throws PWCGException
    {
        this.briefingMissionHandler = briefingMissionHandler;
        this.mapPanel = mapPanel;
    }

    public void mapRequestedFlights() throws PWCGException
    {
        mapFlights();
        mapFlightBox();
    }

    private void mapFlights() throws PWCGException
    {
        Mission mission = briefingMissionHandler.getMission();
        mapPanel.clearVirtualPoints();

        for (ITankPlatoon playerPlatoon : mission.getPlatoons().getPlayerUnits())
        {
            mapPanel.makeMapPanelVirtualPoints(playerPlatoon);
        }
    }

    private void mapFlightBox() throws PWCGException
    {
        CoordinateBox briefingBorders = CoordinateBox.copy(briefingMissionHandler.getMission().getMissionBorders());
        briefingBorders.expandBox(5000);
        mapPanel.setMissionBorders(briefingBorders);
    }
}
