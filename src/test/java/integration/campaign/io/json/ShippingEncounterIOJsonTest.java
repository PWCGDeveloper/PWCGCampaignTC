package integration.campaign.io.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.io.json.ShipEncounterZonesIOJson;
import pwcg.campaign.shipping.ShipEncounterZones;
import pwcg.core.exception.PWCGException;

@ExtendWith(MockitoExtension.class)
public class ShippingEncounterIOJsonTest
{
    @Test
    public void readJsonKubanTest() throws PWCGException
    {
        
        ShipEncounterZones shipEncounterZones = ShipEncounterZonesIOJson.readJson("Kuban");
        Assertions.assertTrue (shipEncounterZones.getShipEncounterZones().size() > 0);
    }
}
