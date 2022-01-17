package pwcg.campaign.factory;

import pwcg.product.bos.country.TCServiceManager;

public class ArmedServiceFactory
{
	static TCServiceManager serviceManager;
    public static TCServiceManager createServiceManager()
    {
        serviceManager = TCServiceManager.getInstance();
        return serviceManager;
    }
}
