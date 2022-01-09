package pwcg.campaign.factory;

import pwcg.campaign.api.IMissionAltitudeGenerator;
import pwcg.product.bos.config.TCMissionAltitudeGenerator;

public class MissionAltitudeGeneratorFactory
{
    public static IMissionAltitudeGenerator createMissionAltitudeGenerator()
    {
        return new TCMissionAltitudeGenerator();
    }
}
