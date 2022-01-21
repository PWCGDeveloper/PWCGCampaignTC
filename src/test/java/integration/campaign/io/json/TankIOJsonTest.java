package integration.campaign.io.json;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.io.json.TankIOJson;
import pwcg.campaign.tank.TankTypeInformation;
import pwcg.core.exception.PWCGException;

@ExtendWith(MockitoExtension.class)
public class TankIOJsonTest
{
    @Test
    public void readJsonTest() throws PWCGException
    {
        
        Map<String, TankTypeInformation> tanks = TankIOJson.readJson();
        validate(tanks);
    }

    private void validate(Map<String, TankTypeInformation> tanks)
    {
        Assertions.assertTrue (tanks.size() > 0);
        for (TankTypeInformation tank : tanks.values())
        {
            assert(tank.getRoleCategories().size() > 0);
        }
    }
}
