package pwcg.product.bos.plane.payload;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.PlaneTypeFactory;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.flight.plane.PlanePayloadFactory;

@ExtendWith(MockitoExtension.class)
public class PlanePayloadFactoryTest
{	
	public PlanePayloadFactoryTest() throws PWCGException
	{
    	      
	}

    @Test
    public void validatePayloadsForAllPlanes() throws PWCGException 
    {
        PlanePayloadFactory planePayloadFactory = new PlanePayloadFactory();
        PlaneTypeFactory planeTypeFactory = PWCGContext.getInstance().getPlaneTypeFactory();

        for (PlaneType bosTankType : planeTypeFactory.getAllPlanes())
        {
            System.out.println(bosTankType.getType());
            
            IPlanePayload payload = planePayloadFactory.createPayload(bosTankType.getType(), DateUtils.getDateYYYYMMDD("19420801"));
            assert(payload != null);
        }
    }
}
