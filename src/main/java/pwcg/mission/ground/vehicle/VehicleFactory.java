package pwcg.mission.ground.vehicle;

import java.util.Date;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.GroundUnitInformation;

public class VehicleFactory
{
    public static IVehicle createVehicle(ICountry country, Date date, VehicleClass vehicleClass) throws PWCGException
    {
        VehicleRequestDefinition requestDefinition = new VehicleRequestDefinition(country.getCountry(), date, vehicleClass);
        VehicleDefinition vehicleDefinition = PWCGContext.getInstance().getVehicleDefinitionManager().getVehicleDefinitionForRequest(requestDefinition);
        IVehicle vehicle = createVehicleFromDefinition(country, date, vehicleDefinition);
        return vehicle;
    }

    public static IVehicle createVehicleFromDefinition(ICountry country, Date date, VehicleDefinition vehicleDefinition) throws PWCGException
    {
        IVehicle vehicle = new Vehicle(vehicleDefinition, country.getCountry());
        return vehicle;
    }

    public static TrainLocomotive createLocomotive(ICountry country, Date date) throws PWCGException
    {
        VehicleRequestDefinition requestDefinition = new VehicleRequestDefinition(country.getCountry(), date, VehicleClass.TrainLocomotive);
        VehicleDefinition vehicleDefinition = PWCGContext.getInstance().getVehicleDefinitionManager().getVehicleDefinitionForRequest(requestDefinition);
        TrainLocomotive locomotive = new TrainLocomotive(vehicleDefinition, country.getCountry());
        return locomotive;
    }

    public static IVehicle createSpecificVehicle(GroundUnitInformation pwcgGroundUnitInformation) throws PWCGException
    {
        ICountry country = pwcgGroundUnitInformation.getCountry();
        Date date = pwcgGroundUnitInformation.getDate();

        VehicleDefinition vehicleDefinition = PWCGContext.getInstance().getVehicleDefinitionManager().getVehicleDefinition(pwcgGroundUnitInformation.getRequestedUnitType());
        
        IVehicle vehicle = createVehicleFromDefinition(country, date, vehicleDefinition);
        return vehicle;
    }
}
