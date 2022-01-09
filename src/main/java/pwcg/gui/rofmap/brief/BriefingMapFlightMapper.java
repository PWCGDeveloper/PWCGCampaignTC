package pwcg.gui.rofmap.brief;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.CoordinateBox;
import pwcg.gui.rofmap.brief.model.BriefingUnit;
import pwcg.mission.Mission;
import pwcg.mission.unit.IPlayerUnit;

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

        for (IPlayerUnit playerUnit : mission.getUnits().getPlayerUnits())
        {
            mapPanel.makeMapPanelVirtualPoints(playerUnit);
        }

        for (IPlayerUnit playerUnit : mission.getUnits().getAiUnits())
        {
            mapFlightAndLinkedFlights(playerUnit);
        }
    }

    private void mapFlightAndLinkedFlights(IPlayerUnit playerUnit) throws PWCGException
    {
        mapPanel.makeMapPanelVirtualPoints(playerUnit);
    }

    private void mapFlightBox() throws PWCGException
    {
        CoordinateBox briefingBorders = CoordinateBox.copy(briefingMissionHandler.getMission().getMissionBorders());
        briefingBorders.expandBox(5000);
        mapPanel.setMissionBorders(briefingBorders);
    }
}
