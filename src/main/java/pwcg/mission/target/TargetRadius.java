package pwcg.mission.target;

import pwcg.mission.flight.FlightTypes;
import pwcg.product.bos.config.TCProductSpecificConfiguration;

public class TargetRadius
{
    private double initialTargetRadius = 10000.0;
    private double maxTargetRadius = 20000.0;
    
    public void calculateTargetRadius(FlightTypes flightType, double missionBoxRadius)
    {
        TCProductSpecificConfiguration productSpecific =new TCProductSpecificConfiguration();

        double productRadiusInitialAdditional = productSpecific.getAdditionalInitialTargetRadius(flightType);
        initialTargetRadius = missionBoxRadius + productRadiusInitialAdditional;
        
        double productRadiusMaxAdditional = productSpecific.getAdditionalMaxTargetRadius(flightType);
        maxTargetRadius = missionBoxRadius + productRadiusMaxAdditional;
    }

    public double getInitialTargetRadius()
    {
        return initialTargetRadius;
    }

    public double getMaxTargetRadius()
    {
        return maxTargetRadius;
    }
}
