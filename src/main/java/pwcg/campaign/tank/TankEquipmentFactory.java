package pwcg.campaign.tank;

import java.util.Date;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.ground.vehicle.VehicleDefinition;
import pwcg.mission.ground.vehicle.VehicleDefinitionManager;

public class TankEquipmentFactory
{
    public static EquippedTank makeTankForCompany (Campaign campaign, String tankTypeName, int companyId) throws PWCGException
    {
        VehicleDefinitionManager vehicleDefinitionManager = PWCGContext.getInstance().getVehicleDefinitionManager();
        VehicleDefinition vehicleDefinition = vehicleDefinitionManager.getVehicleDefinitionByVehicleType(tankTypeName);

        TankTypeFactory tankTypeFactory = PWCGContext.getInstance().getTankTypeFactory();
        TankTypeInformation tankType = tankTypeFactory.createTankTypeByType(tankTypeName);   
        
        EquippedTank equippedTank = new EquippedTank(vehicleDefinition, tankType, campaign.getSerialNumber().getNextTankSerialNumber(), companyId, TankStatus.STATUS_DEPLOYED);

        return equippedTank;
    }

    public static EquippedTank makeTankForDepot (Campaign campaign, String tankTypeName) throws PWCGException
    {
        VehicleDefinitionManager vehicleDefinitionManager = PWCGContext.getInstance().getVehicleDefinitionManager();
        VehicleDefinition vehicleDefinition = vehicleDefinitionManager.getVehicleDefinitionByVehicleType(tankTypeName);

        TankTypeFactory tankTypeFactory = PWCGContext.getInstance().getTankTypeFactory();
        TankTypeInformation tankType = tankTypeFactory.createTankTypeByType(tankTypeName);   
        
        EquippedTank equippedTank = new EquippedTank(vehicleDefinition, tankType, campaign.getSerialNumber().getNextTankSerialNumber(), -1, TankStatus.STATUS_DEPOT);

        return equippedTank;
    }

    public static EquippedTank makeTankForBeforeCampaign (Campaign campaign, Side side, Date date) throws PWCGException
    {
        TankTypeFactory tankTypeFactory = PWCGContext.getInstance().getTankTypeFactory();
        List<TankTypeInformation> tankTypes = tankTypeFactory.createActiveTankTypesForDateAndSide(side, date);
        int index = RandomNumberGenerator.getRandom(tankTypes.size());
        TankTypeInformation tankType = tankTypes.get(index);
        
        VehicleDefinitionManager vehicleDefinitionManager = PWCGContext.getInstance().getVehicleDefinitionManager();
        VehicleDefinition vehicleDefinition = vehicleDefinitionManager.getVehicleDefinitionByVehicleType(tankType.getType());
        
        EquippedTank equippedTank = new EquippedTank(vehicleDefinition, tankType, campaign.getSerialNumber().getNextTankSerialNumber(), -1, TankStatus.STATUS_DEPOT);
        return equippedTank;
    }

}
