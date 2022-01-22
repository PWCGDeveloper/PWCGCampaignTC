package pwcg.mission.ground.vehicle;

import java.util.Date;

import org.junit.jupiter.api.Test;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class VehicleFactoryBoSTest
{
    @Test
    public void vehicleCreateTest() throws PWCGException
    {
        createVehicles(CountryFactory.makeCountryByCountry(Country.RUSSIA), DateUtils.getDateYYYYMMDD("19411001"));
        createVehicles(CountryFactory.makeCountryByCountry(Country.GERMANY), DateUtils.getDateYYYYMMDD("19411001"));
        createVehicles(CountryFactory.makeCountryByCountry(Country.BRITAIN), DateUtils.getDateYYYYMMDD("19440901"));
        createVehicles(CountryFactory.makeCountryByCountry(Country.USA), DateUtils.getDateYYYYMMDD("19440901"));
    }
    
    public void createVehicles(ICountry country, Date date) throws PWCGException
    {
        for (VehicleClass vehicleClass: VehicleClass.getAllVehicleClasses())
        {
            if (!vehicleClass.isStatic())
            {
                IVehicle vehicle = VehicleFactory.createVehicle(country, date, vehicleClass);
                assert(vehicle != null);
            }
        }
    }
    

    @Test
    public void locomotiveCreateTest() throws PWCGException
    {
        createLocomotive(CountryFactory.makeCountryByCountry(Country.RUSSIA), DateUtils.getDateYYYYMMDD("19411001"));
        createLocomotive(CountryFactory.makeCountryByCountry(Country.GERMANY), DateUtils.getDateYYYYMMDD("19411001"));
        createLocomotive(CountryFactory.makeCountryByCountry(Country.BRITAIN), DateUtils.getDateYYYYMMDD("19440901"));
        createLocomotive(CountryFactory.makeCountryByCountry(Country.USA), DateUtils.getDateYYYYMMDD("19440901"));
    }
    
    public void createLocomotive(ICountry country, Date date) throws PWCGException
    {
        IVehicle vehicle = VehicleFactory.createLocomotive(country, date);
        assert(vehicle != null);
    }

}
