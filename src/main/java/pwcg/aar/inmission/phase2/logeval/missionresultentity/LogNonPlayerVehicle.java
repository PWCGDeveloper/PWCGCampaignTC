package pwcg.aar.inmission.phase2.logeval.missionresultentity;

import pwcg.campaign.tank.PwcgRoleCategory;
import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.event.IAType12;

public class LogNonPlayerVehicle extends LogAIEntity
{
    private LogTurrets turrets = new LogTurrets();

    public LogNonPlayerVehicle(int sequenceNumber)
    {
        super(sequenceNumber);
        this.roleCategory = PwcgRoleCategory.GROUND_UNIT;
    }

    public LogTurret createTurret(IAType12 atype12) throws PWCGException
    {
        return turrets.createTurret(atype12, this);
    }

    public boolean ownsTurret(String turretId) throws PWCGException
    {
        return turrets.hasTurret(turretId);
    }
}
