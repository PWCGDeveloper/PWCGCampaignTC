package integration.campaign.io.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.group.GroundStructureGroup;
import pwcg.campaign.io.json.GroundObjectIOJson;
import pwcg.core.exception.PWCGException;

@ExtendWith(MockitoExtension.class)
public class GroundObjectIOJsonTest
{
    @Test
    public void readJsonMoscowTest() throws PWCGException
    {
        
        String mapName = "Moscow";
        validateGroundStructures(mapName);
    }

    @Test
    public void readJsonStalingradTest() throws PWCGException
    {
        
        String mapName = "Stalingrad";
        validateGroundStructures(mapName);
    }

    @Test
    public void readJsonKubanTest() throws PWCGException
    {
        
        String mapName = "Kuban";
        validateGroundStructures(mapName);
    }

    @Test
    public void readJsonBoddenplatteTest() throws PWCGException
    {
        
        String mapName = "Bodenplatte";
        validateGroundStructures(mapName);        
    }

    private GroundStructureGroup validateGroundStructures(String mapName) throws PWCGException, PWCGException
    {
        GroundStructureGroup groundStructures = GroundObjectIOJson.readJson(mapName);
        Assertions.assertTrue (groundStructures.getRailroadStations().size() > 0);
        Assertions.assertTrue (groundStructures.getBridges().size() > 0);
        Assertions.assertTrue (groundStructures.getStandaloneBlocks().size() > 0);
        return groundStructures;
    }

}
