package integration.campaign.io.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.io.json.CargoRoutesIOJson;
import pwcg.campaign.shipping.CargoRoutes;
import pwcg.core.exception.PWCGException;

@ExtendWith(MockitoExtension.class)
public class CargoRoutesIOJsonTest
{
    @Test
    public void readJsonKubanTest() throws PWCGException
    {
        
        CargoRoutes cargoRoutes = CargoRoutesIOJson.readJson("Kuban");
        Assertions.assertTrue (cargoRoutes.getRouteDefinitions().size() > 0);
    }
}
