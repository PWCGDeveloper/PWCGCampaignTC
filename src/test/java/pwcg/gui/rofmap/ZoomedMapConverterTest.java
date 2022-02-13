package pwcg.gui.rofmap;

import java.awt.Dimension;
import java.awt.Point;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.context.MapArea;
import pwcg.campaign.context.PWCGMap;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ZoomedMapConverterTest
{
    @Mock private PWCGMap map;

    ZoomedMapConversionInformation conversionInformation = new ZoomedMapConversionInformation();

    @BeforeEach
    public void setup()
    {
        conversionInformation.setDisplayToSmallMapRatio(60.0 / 100.0);
        conversionInformation.setSmallToDisplayMapRatio(100.0 / 60.0);
        conversionInformation.setLargeMapSize(new Dimension(10000, 10000));
        conversionInformation.setSmallMapSize(new Dimension(600, 600));
        conversionInformation.setSmallMapStartLocation(new Point(4600, 5600));

        MapArea mapArea = new TestMapArea();
        Mockito.when(map.getMapArea()).thenReturn(mapArea);
    }

    @Test
    public void testConversionFromCoordinateToSmallMap()
    {
        ZoomedMapConverter converter = new ZoomedMapConverter(map, conversionInformation);
        Coordinate position = new Coordinate(80000, 0, 102000);
        Point onSmallMap = converter.coordinateToSmallMapPoint(position);
        Assertions.assertTrue(onSmallMap.x > 825 && onSmallMap.x < 835);
        Assertions.assertTrue(onSmallMap.y > 660 && onSmallMap.y < 670);
    }

    @Test
    public void testConversionFromSmallMapToCoordinate() throws PWCGException
    {
        ZoomedMapConverter converter = new ZoomedMapConverter(map, conversionInformation);
        Point smallMapPoint = new Point(833, 666);
        Coordinate position = converter.smallMapPointToCoordinate(smallMapPoint);
        Assertions.assertTrue(position.getZPos() > 101900 && position.getZPos()< 102100);
        Assertions.assertTrue(position.getXPos() > 79900 && position.getXPos() < 80100);
    }

    public class TestMapArea extends MapArea
    {
        public TestMapArea ()
        {
            xMin = 0.0;
            xMax = 200000;
            zMin = 0.0;
            zMax = 200000;
        }
    }
}
