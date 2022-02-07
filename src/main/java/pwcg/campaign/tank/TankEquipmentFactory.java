package pwcg.campaign.tank;

import java.util.Date;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.ground.vehicle.VehicleDefinition;
import pwcg.mission.ground.vehicle.VehicleDefinitionManager;

public class TankEquipmentFactory
{
    public static EquippedTank makeTankForCompany (Campaign campaign, String tankTypeName, Company company) throws PWCGException
    {
        VehicleDefinitionManager vehicleDefinitionManager = PWCGContext.getInstance().getVehicleDefinitionManager();
        VehicleDefinition vehicleDefinition = vehicleDefinitionManager.getVehicleDefinition(tankTypeName);

        ITankTypeFactory tankTypeFactory = PWCGContext.getInstance().getPlayerTankTypeFactory();
        TankTypeInformation tankType = tankTypeFactory.createTankTypeByAnyName(tankTypeName);

        EquippedTank equippedTank = new EquippedTank(vehicleDefinition, tankType, campaign.getSerialNumber().getNextTankSerialNumber(), company, TankStatus.STATUS_DEPLOYED);

        return equippedTank;
    }

    public static EquippedTank makeTankForDepot (Campaign campaign, String tankTypeName, Country country) throws PWCGException
    {
        VehicleDefinitionManager vehicleDefinitionManager = PWCGContext.getInstance().getVehicleDefinitionManager();
        VehicleDefinition vehicleDefinition = vehicleDefinitionManager.getVehicleDefinition(tankTypeName);

        ITankTypeFactory tankTypeFactory = PWCGContext.getInstance().getPlayerTankTypeFactory();
        TankTypeInformation tankType = tankTypeFactory.createTankTypeByAnyName(tankTypeName);

        EquippedTank equippedTank = new EquippedTank(vehicleDefinition, tankType, campaign.getSerialNumber().getNextTankSerialNumber(), TankStatus.STATUS_DEPOT, country);

        return equippedTank;
    }

    public static EquippedTank makeVictimTankForBeforeCampaign (Campaign campaign, Country country, Date date) throws PWCGException
    {
        ITankTypeFactory tankTypeFactory = PWCGContext.getInstance().getFullTankTypeFactory();
        List<TankTypeInformation> tankTypes = tankTypeFactory.createActiveTankTypesForDateAndSide(CountryFactory.makeCountryByCountry(country).getSide(), date);
        int index = RandomNumberGenerator.getRandom(tankTypes.size());
        TankTypeInformation tankType = tankTypes.get(index);

        VehicleDefinitionManager vehicleDefinitionManager = PWCGContext.getInstance().getVehicleDefinitionManager();
        VehicleDefinition vehicleDefinition = vehicleDefinitionManager.getVehicleDefinition(tankType.getType());

        EquippedTank equippedTank = new EquippedTank(vehicleDefinition, tankType, campaign.getSerialNumber().getNextTankSerialNumber(), TankStatus.STATUS_DESTROYED, country);
        return equippedTank;
    }

}
