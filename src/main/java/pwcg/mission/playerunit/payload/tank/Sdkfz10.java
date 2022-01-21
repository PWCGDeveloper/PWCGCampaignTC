package pwcg.mission.playerunit.payload.tank;

import java.util.Date;

import pwcg.campaign.tank.TankTypeInformation;
import pwcg.campaign.tank.payload.ITankPayload;
import pwcg.campaign.tank.payload.TankPayload;
import pwcg.campaign.tank.payload.TankPayloadElement;
import pwcg.core.exception.PWCGException;
import pwcg.mission.platoon.ITankPlatoon;

public class Sdkfz10 extends TankPayload implements ITankPayload
{
    public Sdkfz10(TankTypeInformation tankType, Date date)
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
        Sdkfz10 clone = new Sdkfz10(getTankType(), getDate());
        return super.copy(clone);
    }

    @Override
    protected int createWeaponsPayloadForTank(ITankPlatoon unit) throws PWCGException
    {
        return 0;
    }
}
