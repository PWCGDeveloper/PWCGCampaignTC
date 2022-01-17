package pwcg.campaign;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.core.exception.PWCGException;
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
        TCServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
        ArmedService armedService = armedServiceManager.getArmedServiceByName(GermanServiceBuilder.WEHRMACHT_NAME);
        Assertions.assertTrue (armedService.getServiceId() == TCServiceManager.WEHRMACHT);
    }

    @Test
    public void testGetArmedServiceByService() throws PWCGException
    {
        TCServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
        ArmedService armedService = armedServiceManager.getArmedService(TCServiceManager.BRITISH_ARMY);
        Assertions.assertTrue (armedService.getName().equals(BritishServiceBuilder.BRITISH_ARMY_NAME));
    }

    @Test
    public void testGetBritishArmedServiceByIdTest() throws PWCGException
    {
        TCServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
        ArmedService armedService = armedServiceManager.getArmedServiceById(TCServiceManager.BRITISH_ARMY);
        Assertions.assertTrue (armedService.getName().equals(BritishServiceBuilder.BRITISH_ARMY_NAME));
    }

    @Test
    public void testGetSSVArmedServiceByIdTest() throws PWCGException
    {
        TCServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
        ArmedService armedService = armedServiceManager.getArmedServiceById(TCServiceManager.SSV);
        Assertions.assertTrue (armedService.getName().equals(RussianServiceBuilder.SSV_NAME));
    }

    @Test
    public void testGetUSArmyArmedServiceByIdTest() throws PWCGException
    {
        TCServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
        ArmedService armedService = armedServiceManager.getArmedServiceById(TCServiceManager.US_ARMY);
        Assertions.assertTrue (armedService.getName().equals(AmericanServiceBuilder.US_ARMY_NAME));
    }

    @Test
    public void testGetArmedService() throws PWCGException
    {
        TCServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
        ArmedService armedService = armedServiceManager.getArmedService(TCServiceManager.BRITISH_ARMY);
        Assertions.assertTrue (armedService.getName().equals(BritishServiceBuilder.BRITISH_ARMY_NAME));
    }

    @Test
    public void testGetAllArmedService() throws PWCGException
    {
        TCServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
        List<ArmedService> armedServices = armedServiceManager.getAllArmedServices();
        Assertions.assertTrue (armedServices.size() == 4);
    }

    @Test
    public void testGetAxisArmedService() throws PWCGException
    {
        TCServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
        List<ArmedService> armedServices = armedServiceManager.getAxisServices();
        Assertions.assertTrue (armedServices.size() == 1);
    }

    @Test
    public void testGetAlliedArmedService() throws PWCGException
    {
        TCServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
        List<ArmedService> armedServices = armedServiceManager.getAlliedServices();
        Assertions.assertTrue (armedServices.size() == 3);
    }
}
