package integration.campaign.io.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.io.json.LocationIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.LocationSet;

@ExtendWith(MockitoExtension.class)
public class LocationIOJsonTest
{
    @Test
    public void readJsonMoscowTest() throws PWCGException
    {
        
        String directory = System.getProperty("user.dir") + "\\TCData\\Input\\Moscow\\19411001\\";
        LocationSet locationSet = LocationIOJson.readJson(directory, "FrontLines");
        Assertions.assertTrue (locationSet.getLocations().size() > 0);
    }

    @Test
    public void readJsonStalingradTest() throws PWCGException
    {
        
        String directory = System.getProperty("user.dir") + "\\TCData\\Input\\Stalingrad\\19421011\\";
        LocationSet locationSet = LocationIOJson.readJson(directory, "FrontLines");
        Assertions.assertTrue (locationSet.getLocations().size() > 0);
    }

    @Test
    public void readJsonKubanTest() throws PWCGException
    {
        
        String directory = System.getProperty("user.dir") + "\\TCData\\Input\\Kuban\\19420601\\";
        LocationSet locationSet = LocationIOJson.readJson(directory, "FrontLines");
        Assertions.assertTrue (locationSet.getLocations().size() > 0);
    }

    @Test
    public void readJsonBodenplatteTest() throws PWCGException
    {
        
        String directory = System.getProperty("user.dir") + "\\TCData\\Input\\Bodenplatte\\19440901\\";
        LocationSet locationSet = LocationIOJson.readJson(directory, "FrontLines");
        Assertions.assertTrue (locationSet.getLocations().size() > 0);
    }
}
