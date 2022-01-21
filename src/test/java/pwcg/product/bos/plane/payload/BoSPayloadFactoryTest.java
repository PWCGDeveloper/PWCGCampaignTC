package pwcg.product.bos.plane.payload;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.tank.TankTypeInformation;
import pwcg.campaign.tank.TankTypeFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.flight.plane.PlanePayloadFactory;

@ExtendWith(MockitoExtension.class)
public class BoSPayloadFactoryTest
{	
	public BoSPayloadFactoryTest() throws PWCGException
	{
    	      
	}

    @Test
    public void validatePayloadsForAllPlanes() throws PWCGException 
    {
        PlanePayloadFactory bosPayloadFactory = new PlanePayloadFactory();
        TankTypeFactory planeTypeFactory = PWCGContext.getInstance().getTankTypeFactory();

        for (TankTypeInformation bosTankType : planeTypeFactory.getAllTanks())
        {
            System.out.println(bosTankType.getType());
            
            IPlanePayload payload = bosPayloadFactory.createPayload(bosTankType.getType(), DateUtils.getDateYYYYMMDD("19420801"));
            assert(payload != null);
        }
    }
}
