package pwcg.campaign.factory;

import pwcg.campaign.api.IArmedServiceManager;
import pwcg.product.bos.country.TCServiceManager;

public class ArmedServiceFactory
{
	static IArmedServiceManager serviceManager;
    public static IArmedServiceManager createServiceManager()
    {
        serviceManager = TCServiceManager.getInstance();
        return serviceManager;
    }
}
