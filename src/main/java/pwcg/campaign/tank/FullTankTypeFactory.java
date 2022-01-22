package pwcg.campaign.tank;

import java.util.Map;

import pwcg.campaign.io.json.TankIOJson;
import pwcg.core.exception.PWCGException;

public class FullTankTypeFactory extends TankTypeFactory
{
    public void initialize() throws PWCGException
    {
        Map<String, TankTypeInformation> allTankTypes = TankIOJson.readJson();
        for (TankTypeInformation tankType : allTankTypes.values())
        {
            tankTypes.put(tankType.getType(), tankType);
        }
        createTankArchTypes();
    }
}
