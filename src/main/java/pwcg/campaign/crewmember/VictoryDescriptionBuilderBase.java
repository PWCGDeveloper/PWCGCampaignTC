package pwcg.campaign.crewmember;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.tank.TankTypeInformation;
import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.vehicle.VehicleDefinition;

public abstract class VictoryDescriptionBuilderBase
{
    protected Victory victory;
    protected Campaign campaign;

    public VictoryDescriptionBuilderBase (Campaign campaign, Victory victory)
    {
        this.victory = victory;
        this.campaign = campaign;
    }

    protected String describeVictor() {
        String victorDesc;
        if (victory.getVictor().isGunner())
        {
            victorDesc = "a gunner using with " + victory.getVictor().getCrewMemberName();
        }
        else
        {
            victorDesc = victory.getVictor().getCrewMemberName();
        }
        if (victory.getVictor().getCompanyName() != null && !(victory.getVictor().getCompanyName().isEmpty()))
        {
            victorDesc += " of " + victory.getVictor().getCompanyName();
        }
        return victorDesc;
    }

    protected String getVehicleDescription(String vehicleType, String vehicleName) throws PWCGException
    {
        String vehicleDisplayName = "";
        vehicleDisplayName = getVehicleDisplayName(vehicleType);
        if (vehicleDisplayName.isEmpty())
        {
            vehicleDisplayName = getVehicleDisplayName(vehicleName);
            if (vehicleDisplayName.isEmpty())
            {
                vehicleDisplayName = "enemy vehicle";
            }
        }


        return vehicleDisplayName;
    }

    private String getVehicleDisplayName(String vehicleType) throws PWCGException
    {
        TankTypeInformation tank = PWCGContext.getInstance().getFullTankTypeFactory().createTankTypeByAnyName(vehicleType);
        if (tank != null)
        {
            return tank.getDisplayName();
        }

        VehicleDefinition vehicleDefinition = PWCGContext.getInstance().getVehicleDefinitionManager().getVehicleDefinition(vehicleType);
        if (vehicleDefinition != null)
        {
            return vehicleDefinition.getDisplayName();
        }
        return "";
    }
}
