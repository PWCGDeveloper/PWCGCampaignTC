package pwcg.gui.rofmap.brief;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.image.ImageCache;
import pwcg.gui.rofmap.MapPanelZoomedBase;
import pwcg.gui.rofmap.brief.model.BriefingMapPoint;
import pwcg.gui.rofmap.brief.model.BriefingPlatoonParameters;
import pwcg.gui.rofmap.brief.model.IBriefingPlatoon;
import pwcg.gui.utils.MapPointInfoPopup;
import pwcg.mission.Mission;
import pwcg.mission.ground.builder.ArmoredPlatoonResponsiveRoute;
import pwcg.mission.ground.org.GroundUnitElement;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.platoon.ITankPlatoon;
import pwcg.mission.target.FrontSegmentDefinition;

public class BriefingMapPanel extends MapPanelZoomedBase implements ActionListener
{
    private static final long serialVersionUID = 1L;
    public static int NO_MAP_POINT_SELECTED = -1;

    private Mission mission;
    private BriefingMapGUI parent;

    public BriefingMapPanel(BriefingMapGUI parent) throws PWCGException
    {
        super(BriefingContext.getInstance().getBriefingData().getMission());

        this.parent = parent;
        this.mission = BriefingContext.getInstance().getBriefingData().getMission();
    }

    @Override
    public void paintComponent(Graphics g)
    {
        try
        {
            super.paintBaseMapWithMajorGroups(g);

            g.setColor(ColorMap.ALLIED_AIRFIELD_PINK);
            drawGroundUnits(g, mission.getGroundUnitBuilder().getAssault().getGroundUnitsForSide(Side.ALLIED));

            g.setColor(ColorMap.GERMAN_NAVY_GRAY);
            drawGroundUnits(g, mission.getGroundUnitBuilder().getAssault().getGroundUnitsForSide(Side.AXIS));

            g.setColor(Color.GREEN);
            drawResponsiveRoutes(g, mission.getPlatoons().getPlatoons());

            IBriefingPlatoon playerBriefingPlatoon = BriefingContext.getInstance().getBriefingData().getActiveBriefingPlayerPlatoon();
            if(playerBriefingPlatoon.getSide() == Side.ALLIED)
            {
                drawMultiplePlatoonWaypoints(g, ColorMap.RUSSIAN_RED,
                        BriefingContext.getInstance().getBriefingData().getBriefingPlatoonsForSide(Side.ALLIED));
            }
            else
            {
                drawMultiplePlatoonWaypoints(g, ColorMap.AXIS_BLACK,
                        BriefingContext.getInstance().getBriefingData().getBriefingPlatoonsForSide(Side.AXIS));
            }

            drawSelectedPlatoonWaypoints(g, ColorMap.BELGIAN_GOLD);

            drawAssaults(g);

            parent.refreshMapScreen();
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
        }
    }

    private void drawSelectedPlatoonWaypoints(Graphics g, Color color) throws PWCGException
    {
        g.setColor(color);

        BriefingPlatoonParameters briefingPlatoonParameters = BriefingContext.getInstance().getBriefingData().getActiveBriefingMapPlatoon().getBriefingPlatoonParameters();
        drawPlatoonWaypoints(g, briefingPlatoonParameters);
    }

    private void drawMultiplePlatoonWaypoints(Graphics g, Color color, List<IBriefingPlatoon> briefingPlatoons) throws PWCGException
    {
        g.setColor(color);

        IBriefingPlatoon selectedBriefingPlatoon = BriefingContext.getInstance().getBriefingData().getActiveBriefingMapPlatoon();
        for (IBriefingPlatoon briefingPlatoon : briefingPlatoons)
        {
            if (selectedBriefingPlatoon.getCompanyId() != briefingPlatoon.getCompanyId())
            {
                drawPlatoonWaypoints(g, briefingPlatoon.getBriefingPlatoonParameters());
            }
        }
    }

    private void drawPlatoonWaypoints(Graphics g, BriefingPlatoonParameters briefingPlatoonParameters)
    {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));

        BriefingMapPoint previousMapPoint = null;
        for (BriefingMapPoint mapPoint : briefingPlatoonParameters.getBriefingMapMapPoints())
        {
            if (previousMapPoint != null)
            {
                Point prevPoint = super.coordinateToDisplayMapPoint(previousMapPoint.getPosition());
                Point point = super.coordinateToDisplayMapPoint(mapPoint.getPosition());

                g2.draw(new Line2D.Float(prevPoint.x, prevPoint.y, point.x, point.y));
            }

            Point point = super.coordinateToDisplayMapPoint(mapPoint.getPosition());

            Ellipse2D.Double circle = new Ellipse2D.Double(point.x - 6, point.y - 6, 12, 12);
            g2.fill(circle);

            previousMapPoint = mapPoint;
        }
    }

    private void drawResponsiveRoutes(Graphics g, List<ITankPlatoon> platoons)
    {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));

        for (ITankPlatoon platoon : platoons)
        {
            for (ArmoredPlatoonResponsiveRoute responsiveRoute : platoon.getResponsiveRoutes())
            {
                Point unitPoint = super.coordinateToDisplayMapPoint(responsiveRoute.getTargetPosition());
                Ellipse2D.Double circle = new Ellipse2D.Double(unitPoint.x - 6, unitPoint.y - 6, 12, 12);
                g2.fill(circle);
            }
        }
    }

    private void drawGroundUnits(Graphics g, List<IGroundUnit> groundUnits) throws PWCGException
    {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));

        for (IGroundUnit groundUnit : groundUnits)
        {
            for (GroundUnitElement groundUnitElement : groundUnit.getGroundElements())
            {
                Point unitPoint = super.coordinateToDisplayMapPoint(groundUnitElement.getVehicleStartLocation());
                Ellipse2D.Double circle = new Ellipse2D.Double(unitPoint.x - 6, unitPoint.y - 6, 12, 12);
                g2.fill(circle);
            }
        }
    }

    private void drawAssaults(Graphics g) throws PWCGException
    {
        for (FrontSegmentDefinition assaultDefinition : mission.getBattleManager().getMissionAssaultDefinitions())
        {
            BufferedImage arrowImage = null;
            String imagePath = PWCGContext.getInstance().getDirectoryManager().getPwcgImagesDir() + "Overlay\\";
            if (assaultDefinition.getAssaultingCountry().getSide() == Side.ALLIED)
            {
                imagePath += "RedArrow.png";
            }
            else
            {
                imagePath += "BlueArrow.png";
            }

            double assaultToDefenseDirection = MathUtils.calcAngle(assaultDefinition.getAssaultPosition(), assaultDefinition.getDefensePosition());
            arrowImage = ImageCache.getInstance().getRotatedImage(imagePath, Double.valueOf(assaultToDefenseDirection).intValue());

            Point mapAssaultPointRectangleFrontPoint = coordinateToDisplayMapPoint(assaultDefinition.getDefensePosition());
            mapAssaultPointRectangleFrontPoint.x -= (arrowImage.getHeight() / 2);
            mapAssaultPointRectangleFrontPoint.y -= (arrowImage.getHeight() / 2);

            g.drawImage(arrowImage, mapAssaultPointRectangleFrontPoint.x, mapAssaultPointRectangleFrontPoint.y, null);
        }
    }

    @Override
    public void mouseMovedCallback(MouseEvent e)
    {
        if (mission.getFinalizer().isFinalized())
        {
            return;
        }

        int movedIndex = determineSelectedWaypointIndex(e.getX(), e.getY());
        if (movedIndex != NO_MAP_POINT_SELECTED)
        {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
        else
        {
            setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        }

        super.mouseDraggedCallback(e);

    }

    @Override
    public void mouseDraggedCallback(MouseEvent mouseEvent)
    {
        if (!mission.getFinalizer().isFinalized())
        {
            BriefingPlatoonParameters briefingPlatoonParameters =
                    BriefingContext.getInstance().getBriefingData().getActiveBriefingMapPlatoon().getBriefingPlatoonParameters();
            BriefingMapPoint selectedMapPoint = briefingPlatoonParameters.getSelectedMapPoint();
            if (selectedMapPoint != null)
            {
                Point point = new Point();
                point.x = mouseEvent.getX();
                point.y = mouseEvent.getY();

                try
                {
                    Coordinate updatedPosition = this.pointToCoordinate(point);
                    briefingPlatoonParameters.updatePosition(updatedPosition);
                }
                catch (Exception e)
                {
                }

                refresh();
            }
        }
    }

    @Override
    public void leftClickCallback(MouseEvent mouseEvent)
    {
        if (mission.getFinalizer().isFinalized())
        {
            super.leftClickCallback(mouseEvent);
        }
        else
        {
            int selectedMapPointIndex = determineSelectedWaypointIndex(mouseEvent.getX(), mouseEvent.getY());
            BriefingPlatoonParameters briefingPlatoonParameters = BriefingContext.getInstance().getBriefingData().getActiveBriefingMapPlatoon().getBriefingPlatoonParameters();
            briefingPlatoonParameters.setSelectedMapPointIndex(selectedMapPointIndex);
            if (selectedMapPointIndex == NO_MAP_POINT_SELECTED)
            {
                super.leftClickCallback(mouseEvent);
            }
        }
    }

    @Override
    public void rightClickCallback(MouseEvent e)
    {
        try
        {
            int selectedMapPointIndex = determineSelectedWaypointIndex(e.getX(), e.getY());
            if (selectedMapPointIndex != NO_MAP_POINT_SELECTED)
            {
                WaypointInformationPopup menu = new WaypointInformationPopup(selectedMapPointIndex);
                menu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
        catch (Exception exp)
        {
            PWCGLogger.logException(exp);
        }
    }

    @Override
    public void centerClickCallback(MouseEvent e)
    {
        if (!mission.getFinalizer().isFinalized())
        {
            int selectedMapPointIndex = determineSelectedWaypointIndex(e.getX(), e.getY());
            BriefingPlatoonParameters briefingPlatoonParameters = BriefingContext.getInstance().getBriefingData().getActiveBriefingMapPlatoon().getBriefingPlatoonParameters();
            briefingPlatoonParameters.setActionMapPointIndex(selectedMapPointIndex);
            if (selectedMapPointIndex >= 0)
            {
                WaypointActionPopup menu = new WaypointActionPopup(this);
                menu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
        else
        {
            try
            {
                Point point = new Point();
                point.x = e.getX();
                point.y = e.getY();
                Coordinate coordinate = pointToCoordinate(point);
                MapPointInfoPopup menu = new MapPointInfoPopup(this, coordinate);
                menu.show(e.getComponent(), e.getX(), e.getY());
            }
            catch (Exception exp)
            {
                PWCGLogger.logException(exp);
            }
        }

    }

    @Override
    public void leftClickReleasedCallback(MouseEvent mouseEvent) throws PWCGException
    {
        super.leftClickReleasedCallback(mouseEvent);

        if (mission.getFinalizer().isFinalized())
        {
            return;
        }

        BriefingPlatoonParameters briefingPlatoonParameters = BriefingContext.getInstance().getBriefingData().getActiveBriefingMapPlatoon().getBriefingPlatoonParameters();
        if (briefingPlatoonParameters.getSelectedMapPointIndex() != NO_MAP_POINT_SELECTED)
        {
            Point point = new Point();
            point.x = mouseEvent.getX();
            point.y = mouseEvent.getY();
            Coordinate newCoord = this.pointToCoordinate(point);
            briefingPlatoonParameters.updatePosition(newCoord);

            refresh();
        }

        briefingPlatoonParameters.setSelectedMapPointIndex(NO_MAP_POINT_SELECTED);
        parent.refreshMapScreen();
    }

    @Override
    public void rightClickReleasedCallback(MouseEvent e)
    {
        if (mission.getFinalizer().isFinalized())
        {
            return;
        }

    }

    private int determineSelectedWaypointIndex(int x, int y)
    {
        int selectedMapPointIndex = NO_MAP_POINT_SELECTED;
        int lastValidIndex = NO_MAP_POINT_SELECTED;
        BriefingPlatoonParameters briefingPlatoonParameters = BriefingContext.getInstance().getBriefingData().getActiveBriefingMapPlatoon().getBriefingPlatoonParameters();
        for (BriefingMapPoint mapPoint :  briefingPlatoonParameters.getBriefingMapMapPoints())
        {
            ++lastValidIndex;

            Point point = super.coordinateToDisplayMapPoint(mapPoint.getPosition());

            if ((Math.abs(point.x - x) < 10) &&
                    (Math.abs(point.y - y) < 10))
            {
                if(mapPoint.isEditable())
                {
                    selectedMapPointIndex = lastValidIndex;
                }
                break;
            }
        }

        return selectedMapPointIndex;
    }

    @Override
    public Point upperLeft()
    {
        Point upperLeft = new Point();

        upperLeft.x = 10000000;
        upperLeft.y = 10000000;

        BriefingPlatoonParameters briefingPlatoonParameters = BriefingContext.getInstance().getBriefingData().getActiveBriefingMapPlatoon().getBriefingPlatoonParameters();
        for (BriefingMapPoint mapPoint :  briefingPlatoonParameters.getBriefingMapMapPoints())
        {
            Point point = super.coordinateToDisplayMapPoint(mapPoint.getPosition());

            if (point.x < upperLeft.x)
            {
                upperLeft.x = point.x;
            }
            if (point.y < upperLeft.y)
            {
                upperLeft.y = point.y;
            }
        }

        return upperLeft;
    }

    @Override
    public void actionPerformed(ActionEvent arg0)
    {
        try
        {
            String action = arg0.getActionCommand();
            BriefingPlatoonParameters briefingPlatoonParameters = BriefingContext.getInstance().getBriefingData().getActiveBriefingMapPlatoon().getBriefingPlatoonParameters();
            if (action.contains("Add"))
            {
                if (briefingPlatoonParameters.getActionMapPointIndex() >= 0)
                {
                    parent.waypointAddedNotification();
                }
            }
            else if (action.contains("Remove"))
            {
                if (briefingPlatoonParameters.getActionMapPointIndex() >= 0)
                {
                    BriefingMapPoint selectedActionPoint = briefingPlatoonParameters.getSelectedActionMapPoint();
                    parent.waypointRemovedNotification(selectedActionPoint.getWaypointID());
                }
            }
            else if (action.contains("Cancel"))
            {
            }
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
        }

    }
}
