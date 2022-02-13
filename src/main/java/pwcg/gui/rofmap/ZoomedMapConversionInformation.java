package pwcg.gui.rofmap;

import java.awt.Dimension;
import java.awt.Point;

public class ZoomedMapConversionInformation
{
    private double displayToSmallMapRatio = 1.0;
    private double smallToDisplayMapRatio = 1.0;
    private Point smallMapStartLocation = new Point();
    private Dimension smallMapSize = new Dimension();
    private Dimension largeMapSize = new Dimension();

    public void setDisplayToSmallMapRatio(double displayMapToSmallMapRatio)
    {
        this.displayToSmallMapRatio = displayMapToSmallMapRatio;
    }

    public void setSmallToDisplayMapRatio(double smallMapToDisplayMapRatio)
    {
        this.smallToDisplayMapRatio = smallMapToDisplayMapRatio;
    }

    public void setLargeMapSize(Dimension size)
    {
        this.largeMapSize.setSize(size.getWidth(), size.getHeight());
    }

    public void setSmallMapSize(Dimension size)
    {
        this.smallMapSize.setSize(size.getWidth(), size.getHeight());
    }

    public void setSmallMapStartLocation(Point smallMapStartLocation)
    {
        this.smallMapStartLocation.x = smallMapStartLocation.x;
        this.smallMapStartLocation.y = smallMapStartLocation.y;
    }

    public double getDisplayMapToSmallMapRatio()
    {
        return displayToSmallMapRatio;
    }

    public double getSmallMapToDisplayMapRatio()
    {
        return smallToDisplayMapRatio;
    }

    public Point getSmallMapStartLocation()
    {
        return smallMapStartLocation;
    }

    public Dimension getSmallMapSize()
    {
        return smallMapSize;
    }

    public Dimension getLargeMapSize()
    {
        return largeMapSize;
    }
}
