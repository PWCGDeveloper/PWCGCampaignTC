package pwcg.product.bos.plane;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.tank.TankType;
import pwcg.campaign.tank.TankTypeFactory;
import pwcg.core.exception.PWCGException;

@ExtendWith(MockitoExtension.class)
public class BoSPlaneFactoryTest
{	
	public BoSPlaneFactoryTest() throws PWCGException
	{
    	      
	}

	@Test
	public void testTankTypeCreation() throws PWCGException
	{
		TankTypeFactory planeTypeFactory = PWCGContext.getInstance().getTankTypeFactory();
		
		for (TankType planeType : planeTypeFactory.getAllTanks())
		{
			TankType plane = planeTypeFactory.getTankById(planeType.getType());
			Assertions.assertTrue (plane.getType().equals(planeType.getType()));
			Assertions.assertTrue (plane.getArchType() != null);
		}
	}
}
