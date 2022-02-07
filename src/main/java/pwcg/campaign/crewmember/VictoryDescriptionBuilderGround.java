package pwcg.campaign.crewmember;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.BlockDefinition;
import pwcg.campaign.group.BlockDefinitionManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.mission.ground.building.PwcgBuildingIdentifier;
import pwcg.mission.ground.building.PwcgStructure;
import pwcg.mission.ground.vehicle.VehicleDefinition;
import pwcg.mission.ground.vehicle.VehicleDefinitionManager;

public class VictoryDescriptionBuilderGround extends VictoryDescriptionBuilderBase
{
    VictoryDescriptionBuilderGround (Campaign campaign, Victory victory)
    {
        super(campaign, victory);
    }

    // On <victory victory.getDate()> near <location>.
    // A <victory.getVictim() plane Name> of <victory.getVictim() company name> was destroyed by <victory.getVictor() name> of <victory.getVictor() company name>
    // <victory.getVictim() crew 1> was <killed/captured>.  <victory.getVictim() crew 2> was <killed/captured>
    // <victory.getVictor() crew 1> and <victory.getVictor() crew 1> were using a <victory.getVictor() plane Name>.
    /**
     * @return
     * @throws PWCGException
     */
    public String getVictoryDescriptionGroundToGroundFull() throws PWCGException
    {
        // Create the victory description
        String victoryDesc = "";

        String victimTankType = getVehicleDescription(victory.getVictim().getType(), victory.getVictim().getName());
        String victorTankType = getVehicleDescription(victory.getVictor().getType(), victory.getVictor().getName());

        // Line 1
        victoryDesc +=  "On " + DateUtils.getDateString(victory.getDate());
        if (!victory.getLocation().isEmpty())
        {
            victoryDesc +=  " near " + victory.getLocation();
        }
        victoryDesc +=  ".";

        // Line 2
        victoryDesc +=  "\n";
        victoryDesc +=  "A " + victimTankType + " was destroyed by ";
        victoryDesc += describeVictor();
        victoryDesc +=  ".";

        // Line 3
        victoryDesc +=  "\n";
        victoryDesc +=  victory.getVictor().getCrewMemberName();
        victoryDesc +=  " was using a " + victorTankType + ".";

        return victoryDesc;
    }


    // On <victory victory.getDate()> near <location>.
    // A <victory.getVictim() ground unit> was destroyed by by <victory.getVictor() name> of <victory.getVictor() company name>
    // <victory.getVictor() crew 1> and <victory.getVictor() crew 1> were using a <victory.getVictor() plane Name>.
    public String createVictoryDescriptionAirToGround() throws PWCGException
    {
        String victoryDesc = "";

        String victorTankType = getVehicleDescription(victory.getVictor().getType(), victory.getVictor().getName());

        // Line 1
        victoryDesc +=  "On " + DateUtils.getDateString(victory.getDate());
        if (!victory.getLocation().isEmpty())
        {
            victoryDesc +=  " near " + victory.getLocation();
        }
        victoryDesc +=  ".";

        // Line 2
        victoryDesc +=  "\n";
        victoryDesc +=  "A " + getGroundUnitName(victory.getVictim()) + " was destroyed by ";
        victoryDesc += describeVictor();
        victoryDesc += ".";

        // Line 3
        victoryDesc +=  "\n";
        victoryDesc +=  victory.getVictor().getCrewMemberName();
        victoryDesc +=  " was using a " + victorTankType + ".";

        return victoryDesc;
    }


    // On <victory victory.getDate()> near <location>.
    // A <victory.getVictim() plane Name> of <victory.getVictim() company name> was destroyed by a <ground unit name>
    // <victory.getVictim() crew 1> was <killed/captured>.  <victory.getVictim() crew 2> was <killed/captured>
    String createVictoryDescriptionGroundToAir() throws PWCGException
    {
        String victoryDesc = "";

        String victimPlaneType = getVehicleDescription(victory.getVictim().getType(), victory.getVictim().getName());

        if (!(victory.getVictim().getCrewMemberName().isEmpty()))
        {
            // Line 1
            victoryDesc +=  "On " + DateUtils.getDateString(victory.getDate());
            if (!victory.getLocation().isEmpty())
            {
                victoryDesc +=  " near " + victory.getLocation();
            }
            victoryDesc +=  ".";

            // Line 2
            victoryDesc +=  "\n";
            victoryDesc +=  "A " + victimPlaneType + " was destroyed by a " + getGroundUnitName(victory.getVictor()) + ".";
        }
        // If we do not have all of the information
        else
        {
            victoryDesc +=  victimPlaneType + " shot down by " + getGroundUnitName(victory.getVictor()) + "";
        }

        return victoryDesc;
    }

    // On <victory victory.getDate()> near <location>.
    // A <victory.getVictim() ground unit> was destroyed by a <ground unit name>
    public String createVictoryDescriptionGroundToGround() throws PWCGException
    {
        String victoryDesc = "";

        // Line 1
        victoryDesc +=  "On " + DateUtils.getDateString(victory.getDate());
        if (!victory.getLocation().isEmpty())
        {
            victoryDesc +=  " near " + victory.getLocation();
        }
        victoryDesc +=  ".";

        // Line 2
        victoryDesc +=  "A " + getGroundUnitName(victory.getVictim()) + " was destroyed by a " + getGroundUnitName(victory.getVictor()) + ".";

        return victoryDesc;
    }

    // On <victory victory.getDate()> near <location>.
    // A <victory.getVictim() ground unit> was destroyed by a <ground unit name>
    public String createVictoryDescriptionUnknownToGround()
    {
        String victoryDesc = "";

        // Line 1
        victoryDesc +=  "On " + DateUtils.getDateString(victory.getDate());
        if (!victory.getLocation().isEmpty())
        {
            victoryDesc +=  " near " + victory.getLocation();
        }
        victoryDesc +=  ".";

        // Line 2
        victoryDesc +=  "A " + victory.getVictim().getType() + " was destroyed.";

        return victoryDesc;
    }


    private String getGroundUnitName(VictoryEntity victoryEntity) throws PWCGException
    {
        PwcgStructure building = PwcgBuildingIdentifier.identifyBuilding(victoryEntity.getType());
        if (building != PwcgStructure.UNKNOWN)
        {
            return building.getDescription();
        }

        String vehicleNameFromType = getVehicleName(victoryEntity.getType());
        if (vehicleNameFromType != null)
        {
            return vehicleNameFromType;
        }

        String vehicleNameFromName = getVehicleName(victoryEntity.getName());
        if (vehicleNameFromName != null)
        {
            return vehicleNameFromName;
        }

        BlockDefinition blockDefinition = BlockDefinitionManager.getInstance().getBlockDefinition(victoryEntity.getType());
        if (blockDefinition != null)
        {
            return blockDefinition.getDesc();
        }

        if (VehicleDefinitionManager.isLocomotive(victoryEntity.getName()) || VehicleDefinitionManager.isLocomotive(victoryEntity.getType()))
        {
            return "Locomotive";
        }

        if (VehicleDefinitionManager.isTrainCar(victoryEntity.getName()) || VehicleDefinitionManager.isTrainCar(victoryEntity.getType()))
        {
            return "Train Car";
        }

        PWCGLogger.log(LogLevel.ERROR, "No vehicle match found for " + victoryEntity.getName() + " or " + victoryEntity.getType());
        return "vehicle";
    }


    private String getVehicleName(String vehicleDescriptor) throws PWCGException
    {
        VehicleDefinition vehicleDefinitionByName = PWCGContext.getInstance().getVehicleDefinitionManager().getVehicleDefinition(vehicleDescriptor);
        if (vehicleDefinitionByName != null)
        {
            return vehicleDefinitionByName.getDisplayName();
        }

        VehicleDefinition vehicleDefinitionByDisplayName = PWCGContext.getInstance().getVehicleDefinitionManager().getVehicleDefinition(vehicleDescriptor);
        if (vehicleDefinitionByDisplayName != null)
        {
            return vehicleDefinitionByDisplayName.getDisplayName();
        }

        return null;
    }
}
