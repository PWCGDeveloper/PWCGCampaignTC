package pwcg.mission.ground.vehicle;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.IPWCGObject;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.mcu.McuTREntity;

public interface IVehicle extends IPWCGObject
{
    public void makeVehicleFromDefinition(ICountry vehicleCountry) throws PWCGException;
    public void populateEntity() throws PWCGException;
    public McuTREntity getEntity();
    public void setOrientation(Orientation orientation);
    public void setAiLevel(AiSkillLevel aiLevel);
    public void setCountry(ICountry country);
    public void setPosition(Coordinate position);
    public void setEngageable(int engageable);
    public String getDescription();
    public String getScript();
    public ICountry getCountry() throws PWCGException;
    public Coordinate getPosition();
    public Orientation getOrientation();
    public int getIndex();
    public int getLinkTrId();
    public String getType();
    public String getName();
}
