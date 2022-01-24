package pwcg.mission.playerunit.payload.tank;

import java.util.Date;

import pwcg.campaign.tank.TankTypeInformation;
import pwcg.campaign.tank.payload.ITankPayload;
import pwcg.campaign.tank.payload.TankPayload;
import pwcg.campaign.tank.payload.TankPayloadElement;
import pwcg.core.exception.PWCGException;
import pwcg.mission.platoon.ITankPlatoon;

public class Sdkfz184Payload extends TankPayload implements ITankPayload
{
    public Sdkfz184Payload(TankTypeInformation tankType, Date date)
    {
        super(tankType, date);
    }

    protected void initialize()
	{        
        setAvailablePayload(0, "1", TankPayloadElement.STANDARD);
	}
      
    @Override
    public ITankPayload copy()
    {
        Sdkfz184Payload clone = new Sdkfz184Payload(getTankType(), getDate());
        return super.copy(clone);
    }

    @Override
    protected int createWeaponsPayloadForTank(ITankPlatoon unit) throws PWCGException
    {
        return 0;
    }
}
