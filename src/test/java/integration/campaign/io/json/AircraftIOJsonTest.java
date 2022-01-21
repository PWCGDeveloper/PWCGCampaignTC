package integration.campaign.io.json;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.io.json.AircraftIOJson;
import pwcg.campaign.tank.TankTypeInformation;
import pwcg.core.exception.PWCGException;

@ExtendWith(MockitoExtension.class)
public class AircraftIOJsonTest
{
    @Test
    public void readJsonTest() throws PWCGException
    {
        
        Map<String, TankTypeInformation> aircraft = AircraftIOJson.readJson();
        validate(aircraft);
    }
    
    @Test
    public void readJsonBoSTest() throws PWCGException
    {
        
        Map<String, TankTypeInformation> aircraft = AircraftIOJson.readJson();
        validate(aircraft);
    }

    private void validate(Map<String, TankTypeInformation> aircraft)
    {
        Assertions.assertTrue (aircraft.size() > 0);
        for (TankTypeInformation planeType : aircraft.values())
        {
            assert(planeType.getRoleCategories().size() > 0);
        }
    }
}
