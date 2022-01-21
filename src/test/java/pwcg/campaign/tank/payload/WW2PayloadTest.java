package pwcg.campaign.tank.payload;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.tank.TankTypeInformation;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.platoon.ITankPlatoon;

@ExtendWith(MockitoExtension.class)
public class WW2PayloadTest
{
    @Mock ITankPlatoon unit;

    @BeforeEach
    public void setupTest() throws PWCGException
    {
    }

    @Test
    public void payloadTest() throws PWCGException
    {
        TankPayloadFactory payloadFactory = new TankPayloadFactory();        

        for (TankTypeInformation tankType : PWCGContext.getInstance().getTankTypeFactory().getAllTanks())
        {
            ITankPayload payloadGenerator = payloadFactory.createPayload(tankType.getType(), DateUtils.getDateYYYYMMDD("19420801"));
            runPayload(payloadGenerator);
        }
    }

    private void runPayload(ITankPayload payload) throws PWCGException
    {
        int payloadId = payload.createWeaponsPayload(unit);
        TankPayloadDesignation payloadDesignation = payload.getSelectedPayloadDesignation();
        Assertions.assertTrue (payloadDesignation.getPayloadId() == payloadId);
    }
}
