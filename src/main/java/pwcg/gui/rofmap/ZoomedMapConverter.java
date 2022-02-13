package pwcg.gui.rofmap;

import java.awt.Dimension;
import java.awt.Point;

import pwcg.campaign.context.MapArea;
import pwcg.campaign.context.PWCGMap;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;

public class ZoomedMapConverter
{
    private PWCGMap map;
    private ZoomedMapConversionInformation conversionInformation = new ZoomedMapConversionInformation();

    public ZoomedMapConverter(PWCGMap map, ZoomedMapConversionInformation conversionInformation)
    {
        this.map = map;
        this.conversionInformation = conversionInformation;
    }

    public Coordinate smallMapPointToCoordinate(Point smallMapPosition) throws PWCGException
    {
        Double convertedPointX = smallMapPosition.getX() * conversionInformation.getDisplayMapToSmallMapRatio();
        Double convertedPointY = smallMapPosition.getY() * conversionInformation.getDisplayMapToSmallMapRatio();

        convertedPointX += conversionInformation.getSmallMapStartLocation().getX();
        convertedPointY += conversionInformation.getSmallMapStartLocation().getY();

        Point convertedMapPoint = new Point();
        convertedMapPoint.setLocation(convertedPointX, convertedPointY);

        return pointToLargeMapCoordinate(convertedMapPoint);
    }

    public Point coordinateToSmallMapPoint(Coordinate position)
    {
        Point largeMapPoint = coordinateToPointLargeMap(position);

        int scaledX = Double.valueOf((largeMapPoint.x - conversionInformation.getSmallMapStartLocation().x) *
                conversionInformation.getSmallMapToDisplayMapRatio()).intValue();
        int scaledY = Double.valueOf((largeMapPoint.y - conversionInformation.getSmallMapStartLocation().y) *
                conversionInformation.getSmallMapToDisplayMapRatio()).intValue();

        Point smallMapPoint = new Point(scaledX, scaledY);
        return smallMapPoint;
    }

    private Point coordinateToPointLargeMap(Coordinate position)
    {
        MapArea mapArea = map.getMapArea();
        double ratioWidth = conversionInformation.getLargeMapSize().getWidth() / mapArea.getzMax();
        double ratioHeight = conversionInformation.getLargeMapSize().getHeight() / mapArea.getxMax();

        Point point = new Point();
        point.x = Double.valueOf((position.getZPos() * ratioWidth)).intValue();
        point.y = Double.valueOf(conversionInformation.getLargeMapSize().getHeight()).intValue() -
                Double.valueOf((position.getXPos() * ratioHeight)).intValue();

        return point;
    }


    private Coordinate pointToLargeMapCoordinate(Point point) throws PWCGException
    {
        Dimension largeMapSize = conversionInformation.getLargeMapSize();
        MapArea mapArea = map.getMapArea();
        double ratioWidth = largeMapSize.width / mapArea.getzMax();
        double ratioHeight = largeMapSize.height / mapArea.getxMax();

        double zpos = Double.valueOf(point.x) / ratioWidth;
        int invertedY = largeMapSize.height - point.y;
        double xpos = Double.valueOf(invertedY) / ratioHeight;

        return new Coordinate(xpos, 0.0, zpos);
    }
}
