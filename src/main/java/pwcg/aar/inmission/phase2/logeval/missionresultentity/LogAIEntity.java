package pwcg.aar.inmission.phase2.logeval.missionresultentity;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.tank.PwcgRoleCategory;
import pwcg.campaign.tank.TankTypeInformation;
import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.event.IAType12;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.mission.ground.vehicle.VehicleDefinition;

public abstract class LogAIEntity extends LogBase
{
    protected String id = "";
    protected String name = CrewMember.UNKNOWN_CREW_NAME;
    protected String vehicleType = "";
    protected PwcgRoleCategory roleCategory = PwcgRoleCategory.OTHER;
    protected ICountry country;

    public LogAIEntity(int sequenceNumber)
    {
        super(sequenceNumber);
    }

    public void initializeEntityFromEvent(IAType12 atype12) throws PWCGException
    {
        id = atype12.getId();
        country = atype12.getCountry();
        name = atype12.getName();

        VehicleDefinition vehicle = PWCGContext.getInstance().getVehicleDefinitionManager().getVehicleDefinition(atype12.getType());
        if (vehicle != null)
        {
            vehicleType = vehicle.getVehicleType();
        }
        else
        {
            PWCGLogger.log(LogLevel.ERROR, "Unknown vehicle type in logs: " + atype12.getType());
        }

        TankTypeInformation tank = PWCGContext.getInstance().getFullTankTypeFactory().createTankTypeByAnyName(atype12.getType());
        if (tank != null)
        {
            roleCategory = tank.determinePrimaryRoleCategory();
        }

    }

    public String getId()
    {
        return id;
    }

    public String getVehicleType()
    {
        return vehicleType;
    }

    public ICountry getCountry()
    {
        return country;
    }

    public PwcgRoleCategory getRoleCategory()
    {
        return roleCategory;
    }

    public String getName()
    {
        return name;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setRoleCategory(PwcgRoleCategory roleCategory)
    {
        this.roleCategory = roleCategory;
    }

    public void setVehicleType(String vehicleType)
    {
        this.vehicleType = vehicleType;
    }

    public void setCountry(ICountry country)
    {
        this.country = country;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
