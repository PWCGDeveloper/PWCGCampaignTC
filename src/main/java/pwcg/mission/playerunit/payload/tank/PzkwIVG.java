package pwcg.mission.playerunit.payload.tank;

import java.util.Date;

import pwcg.campaign.tank.TankType;
import pwcg.campaign.tank.payload.ITankPayload;
import pwcg.campaign.tank.payload.TankPayload;
import pwcg.campaign.tank.payload.TankPayloadElement;
import pwcg.core.exception.PWCGException;
import pwcg.mission.playerunit.PlayerUnit;

public class PzkwIVG extends TankPayload implements ITankPayload
{
    public PzkwIVG(TankType tankType, Date date)
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
        PzkwIVG clone = new PzkwIVG(getTankType(), getDate());
        return super.copy(clone);
    }

    @Override
    protected int createWeaponsPayloadForTank(PlayerUnit unit) throws PWCGException
    {
        return 0;
    }
}