package pwcg.campaign;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.api.IArmedServiceManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.product.bos.country.AmericanServiceBuilder;
import pwcg.product.bos.country.BritishServiceBuilder;
import pwcg.product.bos.country.GermanServiceBuilder;
import pwcg.product.bos.country.RussianServiceBuilder;
import pwcg.product.bos.country.TCServiceManager;

@ExtendWith(MockitoExtension.class)
public class ArmedServiceManagerTest
{
    public ArmedServiceManagerTest() throws PWCGException
    {
        
    }

    @Test
    public void testGetArmedServiceByNameTest() throws PWCGException
    {
        IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
        ArmedService armedService = armedServiceManager.getArmedServiceByName(GermanServiceBuilder.WEHRMACHT_NAME,
                DateUtils.getDateYYYYMMDD("19420801"));
        Assertions.assertTrue (armedService.getServiceId() == TCServiceManager.WEHRMACHT);
    }

    @Test
    public void testGetArmedServiceByService() throws PWCGException
    {
        IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
        ArmedService armedService = armedServiceManager.getArmedService(TCServiceManager.BRITISH_ARMY);
        Assertions.assertTrue (armedService.getName().equals(BritishServiceBuilder.BRITISH_ARMY_NAME));
    }

    @Test
    public void testGetBritishArmedServiceByIdTest() throws PWCGException
    {
        IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
        ArmedService armedService = armedServiceManager.getArmedServiceById(TCServiceManager.BRITISH_ARMY, DateUtils.getDateYYYYMMDD("19440901"));
        Assertions.assertTrue (armedService.getName().equals(BritishServiceBuilder.BRITISH_ARMY_NAME));
    }

    @Test
    public void testGetSSVArmedServiceByIdTest() throws PWCGException
    {
        IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
        ArmedService armedService = armedServiceManager.getArmedServiceById(TCServiceManager.SSV, DateUtils.getDateYYYYMMDD("19420801"));
        Assertions.assertTrue (armedService.getName().equals(RussianServiceBuilder.SSV_NAME));
    }

    @Test
    public void testGetUSArmyArmedServiceByIdTest() throws PWCGException
    {
        IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
        ArmedService armedService = armedServiceManager.getArmedServiceById(TCServiceManager.US_ARMY, DateUtils.getDateYYYYMMDD("19440901"));
        Assertions.assertTrue (armedService.getName().equals(AmericanServiceBuilder.US_ARMY_NAME));
    }

    @Test
    public void testGetArmedService() throws PWCGException
    {
        IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
        ArmedService armedService = armedServiceManager.getArmedService(TCServiceManager.BRITISH_ARMY);
        Assertions.assertTrue (armedService.getName().equals(BritishServiceBuilder.BRITISH_ARMY_NAME));
    }

    @Test
    public void testGetAllArmedService() throws PWCGException
    {
        IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
        List<ArmedService> armedServices = armedServiceManager.getAllArmedServices();
        Assertions.assertTrue (armedServices.size() == 4);
    }

    @Test
    public void testGetAxisArmedService() throws PWCGException
    {
        IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
        List<ArmedService> armedServices = armedServiceManager.getAxisServices(DateUtils.getDateYYYYMMDD("19420801"));
        Assertions.assertTrue (armedServices.size() == 1);
    }

    @Test
    public void testGetAlliedArmedService() throws PWCGException
    {
        IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
        List<ArmedService> armedServices = armedServiceManager.getAlliedServices(DateUtils.getDateYYYYMMDD("19440901"));
        Assertions.assertTrue (armedServices.size() == 3);
    }
}
