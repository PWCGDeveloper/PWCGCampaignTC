package pwcg.gui.rofmap;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.List;

import pwcg.campaign.context.MapArea;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.gui.dialogs.PWCGMonitorSupport;
import pwcg.gui.image.MapImageCache;
import pwcg.gui.rofmap.brief.BriefingContext;
import pwcg.gui.rofmap.brief.model.BriefingData;
import pwcg.gui.utils.ImagePanel;
import pwcg.mission.Mission;

public abstract class MapPanelZoomedBase extends ImagePanel implements ActionListener, IMouseCallback
{
    protected static final long serialVersionUID = 1L;

    private Dimension largeMapSize;

    private Mission mission;
    private MapImageCache mapImageCache;
    private ZoomedMapConversionInformation conversionInformation = new ZoomedMapConversionInformation();

    public MapPanelZoomedBase(Mission mission) throws PWCGException
    {
        this.mission = mission;
        this.mapImageCache = new MapImageCache();
        mapImageCache.loadCurrentMap();

        PWCGMouseClickListener mouseClickListener = new PWCGMouseClickListener(this);
        PWCGMouseMotionListener mouseMotionListener = new PWCGMouseMotionListener(this);
        MouseWheelListener mouseWheelListener = new PWCGMouseWheelListener(this);

        addMouseListener(mouseClickListener);
        addMouseMotionListener(mouseMotionListener);
        this.addMouseWheelListener(mouseWheelListener);
    }


    public void drawDisplayMap() throws PWCGException
    {
        BriefingData briefingData = BriefingContext.getInstance().getBriefingData();
        if (briefingData.getDisplayBufferedMapImage() == null)
        {
            buildMapBackground();
        }

        BufferedImage displayBufferedMapImage = briefingData.getDisplayBufferedMapImage();
        Image displayMapImage = briefingData.getDisplayMapImage();

        Graphics2D g2d = displayBufferedMapImage.createGraphics();
        g2d.drawImage(displayMapImage, 0, 0, null);
        g2d.dispose();

        super.setImage(displayBufferedMapImage);

        Dimension smallMapSize = getImageSize(image);

        setPreferredSize(smallMapSize);
        setMinimumSize(smallMapSize);
        setMaximumSize(smallMapSize);
        setSize(smallMapSize);
        setLayout(null);

        refresh();
    }

    private void buildMapBackground() throws PWCGException
    {
        String mapImageFileName = PWCGContext.getInstance().getCurrentMap().getMapName() + "Map150";
        BufferedImage largeMapImage = mapImageCache.getMapImage(mapImageFileName);
        largeMapSize = getImageSize(largeMapImage);

        setMapImageToMissionBox(largeMapImage);
    }

    private void setMapImageToMissionBox(BufferedImage mapImage) throws PWCGException
    {
        double widthMeters = mission.getMissionBorders().getBoxWidth();
        double heightMeters = mission.getMissionBorders().getBoxHeight();
        Coordinate missionCenter = mission.getMissionBorders().getCenter();

        Coordinate upperLeftPositionOfSmallMap = new Coordinate(missionCenter.getXPos() + (heightMeters/2), 0, missionCenter.getZPos() - (widthMeters/2));
        Point imagePointUpperLeft = coordinateToPointLargeMap(upperLeftPositionOfSmallMap);

        Coordinate lowerRightPositionOfSmallMap = new Coordinate(missionCenter.getXPos() - (heightMeters/2), 0, missionCenter.getZPos() + (widthMeters/2));
        Point imagePointLowerRight = coordinateToPointLargeMap(lowerRightPositionOfSmallMap);

        int width = Double.valueOf(imagePointLowerRight.getX() - imagePointUpperLeft.getX()).intValue();
        int height = Double.valueOf(imagePointLowerRight.getY() - imagePointUpperLeft.getY()).intValue();

        int upperLeftX = Double.valueOf(imagePointUpperLeft.getX()).intValue();
        int upperLeftY = Double.valueOf(imagePointUpperLeft.getY()).intValue();

        Point upperLeftOfSmallMap = new Point(upperLeftX, upperLeftY);
        conversionInformation.setSmallMapStartLocation(upperLeftOfSmallMap);
        conversionInformation.setLargeMapSize(new Dimension(mapImage.getWidth(), mapImage.getHeight()));

        BufferedImage mapSegmentImage = mapImage.getSubimage(upperLeftX, upperLeftY, width, height);

        setMapImageToScreen(mapSegmentImage);
    }

    private void setMapImageToScreen(BufferedImage mapSegmentImage)
    {
        Dimension smallMapSize = getImageSize(mapSegmentImage);

        double displayMapHeight = PWCGMonitorSupport.getPWCGFrameSize().getHeight() - 200;
        double smallToDisplayRatio = displayMapHeight / smallMapSize.getHeight();
        Double newHeight = Double.valueOf(smallMapSize.getHeight() * smallToDisplayRatio);
        Double newWidth = Double.valueOf(smallMapSize.getWidth() * smallToDisplayRatio);

        Image displayMapImage = mapSegmentImage.getScaledInstance(newWidth.intValue(), newHeight.intValue(), Image.SCALE_SMOOTH);
        BufferedImage displayBufferedMapImage = new BufferedImage(newWidth.intValue(), newHeight.intValue(),  BufferedImage.TYPE_INT_ARGB);

        double displayToSmallRatio = Double.valueOf(smallMapSize.getHeight()) / newHeight;
        conversionInformation.setSmallToDisplayMapRatio(smallToDisplayRatio);
        conversionInformation.setDisplayToSmallMapRatio(displayToSmallRatio);
        conversionInformation.setSmallMapSize(smallMapSize);

        BriefingData briefingData = BriefingContext.getInstance().getBriefingData();
        briefingData.setDisplayBufferedMapImage(displayBufferedMapImage);
        briefingData.setDisplayMapImage(displayMapImage);
    }

    private Point coordinateToPointLargeMap(Coordinate position)
    {
        MapArea mapArea = PWCGContext.getInstance().getCurrentMap().getMapArea();
        double ratioWidth = largeMapSize.width / mapArea.getzMax();
        double ratioHeight = largeMapSize.height / mapArea.getxMax();

        Point point = new Point();
        point.x = Double.valueOf((position.getZPos() * ratioWidth)).intValue();
        point.y = largeMapSize.height - Double.valueOf((position.getXPos() * ratioHeight)).intValue();

        return point;
    }

    @Override
    public void increaseZoom()
    {
    }

    @Override
    public void decreaseZoom()
    {
    }

    public Coordinate pointToCoordinate(Point point) throws PWCGException
    {
        Coordinate coord = displayMapPointToCoordinate(point);
        return coord;
    }

    public Dimension getMapSize()
    {
        return getImageSize(image);
    }

    protected void paintBaseMapWithMajorGroups(Graphics g) throws PWCGException
    {
        paintBaseMap(g);
    }

    protected void paintBaseMap(Graphics g) throws PWCGException
    {
        g.drawImage(image, 0, 0, null);
    }

    public Point getBestPoint(List<Point> usedPoints, Point point)
    {
        Point bestPoint = new Point();
        bestPoint.x = point.x;
        bestPoint.y = point.y;

        boolean good = true;
        int numTries = 0;
        do
        {
            for (Point usedPoint : usedPoints)
            {
                if (Math.abs(usedPoint.y - bestPoint.y) < 30)
                {
                    if (Math.abs(usedPoint.x - bestPoint.x) < 70)
                    {
                        good = false;
                        bestPoint.y += 30;

                        break;
                    }
                }
            }

            ++numTries;

            if (numTries > 3)
            {
                good = true;
            }
        }
        while (!good);

        return bestPoint;
    }

    public void refresh()
    {
        this.revalidate();
        this.repaint();
    }

    public void makeVisible(boolean isVisible)
    {
        this.setVisible(isVisible);
    }

    @Override
    public abstract void paintComponent(Graphics g);

    @Override
    public abstract void mouseMovedCallback(MouseEvent e);

    @Override
    public void mouseDraggedCallback(MouseEvent mouseEvent)
    {
    }

    @Override
    public void leftClickCallback(MouseEvent mouseEvent)
    {
    }

    @Override
    public void leftClickReleasedCallback(MouseEvent mouseEvent) throws PWCGException
    {
    }

    protected void drawArrow(Graphics g, int x1, int y1, int x2, int y2)
    {
        final int ARR_SIZE = 10;

        Graphics2D g2d = (Graphics2D) g.create();

        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx * dx + dy * dy);
        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g2d.transform(at);

        // Draw horizontal arrow starting in (0, 0)
        g2d.drawLine(0, 0, len, 0);
        g2d.fillPolygon(new int[]
                { len, len - ARR_SIZE, len - ARR_SIZE, len }, new int[]
                        { 0, -ARR_SIZE, ARR_SIZE, 0 }, 4);
    }

    public Point coordinateToDisplayMapPoint(Coordinate position)
    {
        ZoomedMapConverter converter = new ZoomedMapConverter(PWCGContext.getInstance().getCurrentMap(), conversionInformation);
        return converter.coordinateToSmallMapPoint(position);
    }

    public Coordinate displayMapPointToCoordinate(Point displayMapPoint) throws PWCGException
    {
        ZoomedMapConverter converter = new ZoomedMapConverter(PWCGContext.getInstance().getCurrentMap(), conversionInformation);
        return converter.smallMapPointToCoordinate(displayMapPoint);
    }

    @Override
    public abstract void rightClickCallback(MouseEvent e);

    public abstract void rightClickReleasedCallback(MouseEvent e);

    @Override
    public abstract void centerClickCallback(MouseEvent e);

    public abstract Point upperLeft();
}
